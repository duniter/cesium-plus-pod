package org.duniter.elasticsearch.websocket.tyrus;

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


import com.fasterxml.jackson.databind.util.ByteBufferBackedOutputStream;
import com.google.common.collect.ImmutableList;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.http.tyrus.TyrusWebSocketServer;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.CurrencyService;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;

@ServerEndpoint(value = "/block")
public class WebSocketBlockEndPoint implements ChangeService.ChangeListener{

    private static BlockchainService blockchainService;
    private static CurrencyService currencyService;
    private static boolean isReady = false;
    private static ESLogger logger = null;

    public static class Init {


        @Inject
        public Init(TyrusWebSocketServer webSocketServer,
                    CurrencyService currencyService,
                    BlockchainService blockchainService,
                    ThreadPool threadPool) {
            webSocketServer.addEndPoint(WebSocketBlockEndPoint.class);
            WebSocketBlockEndPoint.currencyService = currencyService;
            WebSocketBlockEndPoint.blockchainService = blockchainService;
            logger = Loggers.getLogger("duniter.ws.block");

            //server.addLifecycleListener();
            threadPool.scheduleOnClusterReady(() -> {
                isReady = true;
            });
        }
    }

    private Session session;
    private String currency;
    private List<ChangeSource> sources;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;

        if (!isReady) {
            session.close(new CloseReason(CloseReason.CloseCodes.SERVICE_RESTART, "Pod is not ready"));
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
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Missing currency to listen"));
            return;
        }

        logger.debug(String.format("[%s] Opening websocket session on {/ws/block}, id {%s}", currency, session.getId()));

        // After opening, sent the current block
        sendBinary(blockchainService.getCurrentBlockAsBytes(currency));

        // Listening changes
        this.sources = ImmutableList.of(new ChangeSource(currency, BlockchainService.BLOCK_TYPE));
        ChangeService.registerListener(this);
    }

    @Override
    public void onChange(ChangeEvent changeEvent) {
        switch (changeEvent.getOperation()) {
            case CREATE:
            case INDEX:
                if (changeEvent.hasSource()) {
                    sendBinary(changeEvent.getSource());
                }
                break;
            default:
                // Ignoring (if delete)
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

    @OnMessage
    public void onMessage(String message) {
        // Ignoring
    }

    @OnClose
    public void onClose(CloseReason reason) {
        logger.debug("Closing websocket: "+reason);
        ChangeService.unregisterListener(this);
        this.session = null;
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("Error on websocket endpoint /ws/block "+(session == null ? null : session.getId()), t);
    }

    /* -- internal methods -- */

    protected void sendBinary(BytesReference source) {
        try {
            ByteBuffer bf = ByteBuffer.allocate(1024*1000);
            XContentBuilder builder = new XContentBuilder(JsonXContent.jsonXContent, new ByteBufferBackedOutputStream(bf));
            builder.rawValue(source);
            session.getAsyncRemote().sendBinary(bf);
        } catch(IOException e) {
            logger.error(String.format("[%s] Cannot sent response to session {%s} on {/ws/block}: %s", currency, session.getId(), e.getMessage()), e);
        }

    }
}
