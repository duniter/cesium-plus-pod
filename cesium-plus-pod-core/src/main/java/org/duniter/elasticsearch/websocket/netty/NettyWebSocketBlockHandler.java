package org.duniter.elasticsearch.websocket.netty;

/*
 * #%L
 * Duniter4j :: ElasticSearch Plugin
 * %%
 * Copyright (C) 2014 - 2016 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.google.common.collect.ImmutableList;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.json.JsonAttributeParser;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.http.netty.NettyWebSocketServer;
import org.duniter.elasticsearch.http.netty.websocket.NettyBaseWebSocketEndpoint;
import org.duniter.elasticsearch.http.netty.websocket.NettyWebSocketSession;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.CurrencyService;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;

import javax.websocket.CloseReason;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class NettyWebSocketBlockHandler extends NettyBaseWebSocketEndpoint implements ChangeService.ChangeListener {

    private final static String PATH = WEBSOCKET_PATH + "/block";

    private final static JsonAttributeParser<Long> numberAttributeParser = new JsonAttributeParser<>(BlockchainBlock.PROPERTY_NUMBER, Long.class);
    private final static JsonAttributeParser<String> hashAttributeParser = new JsonAttributeParser<>(BlockchainBlock.PROPERTY_HASH, String.class);

    private static ESLogger logger = null;
    private static BlockchainService blockchainService;
    private static CurrencyService currencyService;
    private static boolean isReady = false;


    public static class Init {
        @Inject
        public Init(PluginSettings pluginSettings,
                    NettyWebSocketServer webSocketServer,
                    CurrencyService currencyService,
                    BlockchainService blockchainService,
                    ThreadPool threadPool) {
            logger = Loggers.getLogger("duniter.ws.block", pluginSettings.getSettings(), new String[0]);

            NettyWebSocketBlockHandler.currencyService = currencyService;
            NettyWebSocketBlockHandler.blockchainService = blockchainService;

            webSocketServer.addEndpoint(PATH, NettyWebSocketBlockHandler.class);

            threadPool.scheduleOnClusterReady(() -> isReady = true);
        }
    }

    private NettyWebSocketSession session;
    private String currency;
    private List<ChangeSource> sources;
    private String lastBlockstampSent;

    @Override
    public void onOpen(NettyWebSocketSession session){

        this.session = session;
        this.lastBlockstampSent = null;

        if (!isReady) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.SERVICE_RESTART, "Pod is not ready"));
            } catch (IOException e) {
                // silent
            }
            return;
        }

        // Use given currency or default currency
        try {
            currency = currencyService.safeGetCurrency(session.getPathParameters().get("currency"));
        } catch (Exception e) {
            logger.debug(String.format("Cannot open websocket session on {/ws/block}: %s", e.getMessage()), e);
        }

        // Failed if no currency on this pod, or if pod is not ready yet
        if (StringUtils.isBlank(currency)) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing currency to listen"));
            } catch (IOException e) {
                // silent
            }
            return;
        }

        logger.debug(String.format("[%s] Opening websocket session on {/ws/block}, id {%s}", currency, session.getId()));

        // After opening, sent the current block
        BytesReference currentBlock = blockchainService.getCurrentBlockAsBytes(currency);
        if (currentBlock != null) sendJson(currentBlock);

        // Listening changes
        this.sources = ImmutableList.of(new ChangeSource(currency, BlockchainService.BLOCK_TYPE));
        ChangeService.registerListener(this);
    }

    @Override
    public void onChange(ChangeEvent event) {
        switch (event.getOperation()) {
            case CREATE:
                sendSourceIfNotNull(event);
                break;
            default:
                // Ignoring (if delete or update)
        }
    }

    @Override
    public String getId() {
        return session == null ? null : session.getId();
    }

    @Override
    public Collection<ChangeSource> getChangeSources() {
        return sources;
    }

    @Override
    public void onMessage(String message) {
        // Ignoring
    }

    @Override
    public void onClose(CloseReason reason) {
        if (reason != null && logger.isDebugEnabled()) logger.debug("Closing websocket: "+reason);
        ChangeService.unregisterListener(this);
        this.session = null;
        this.lastBlockstampSent = null;
    }

    public void onError(Throwable t) {
        logger.error(String.format("[%s] Error on websocket endpoint {%s} session {%s}", currency, PATH, (session == null ? null : session.getId())), t);
        if (this.session != null) {
            try {
                this.session.close(new CloseReason(CloseReason.CloseCodes.NO_STATUS_CODE, "Unexpected error"));
            } catch (IOException e) {
                // silent
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        onError(e.getCause());
    }

    /* -- internal methods -- */

    protected void sendSourceIfNotNull(ChangeEvent event) {

        if (!event.hasSource()) return; // Skip

        try {
            String sourceText = event.getSourceText();

            Long number = numberAttributeParser.getValue(sourceText);
            String hash = hashAttributeParser.getValue(sourceText);

            // Check if not already sent
            String blocktamp = String.format("%s-%s", number, hash);
            if (!blocktamp.equals(this.lastBlockstampSent)) {
                this.lastBlockstampSent = blocktamp;
                session.sendText(sourceText);
            }

        } catch(Throwable e) {
            logger.error(String.format("[%s] Cannot sent websocket response {%s} to session {%s}: %s", currency, PATH, session.getId(), e.getMessage()), e);
        }

    }

    protected void sendJson(BytesReference bytes) {
        try {
            XContentBuilder builder = new XContentBuilder(JsonXContent.jsonXContent, new BytesStreamOutput());
            builder.rawValue(bytes);
            session.sendText(builder.string());
        } catch (IOException e) {
            throw new TechnicalException("Error while generating JSON from source", e);
        }
    }

}
