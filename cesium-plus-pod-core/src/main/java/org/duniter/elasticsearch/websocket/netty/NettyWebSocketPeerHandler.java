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
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.PeerDao;
import org.duniter.elasticsearch.http.netty.NettyWebSocketServer;
import org.duniter.elasticsearch.http.netty.websocket.NettyBaseWebSocketEndpoint;
import org.duniter.elasticsearch.http.netty.websocket.NettyWebSocketSession;
import org.duniter.elasticsearch.service.CurrencyService;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;

import javax.websocket.CloseReason;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class NettyWebSocketPeerHandler extends NettyBaseWebSocketEndpoint implements ChangeService.ChangeListener {

    private final static String PATH = WEBSOCKET_PATH + "/peer";

    private static ESLogger logger = null;
    private static CurrencyService currencyService;
    private static boolean isReady = false;

    public static class Init {
        @Inject
        public Init(Settings settings,
                    NettyWebSocketServer webSocketServer,
                    CurrencyService currencyService,
                    ThreadPool threadPool) {
            logger = Loggers.getLogger("duniter.ws.peer", settings, new String[0]);

            NettyWebSocketPeerHandler.currencyService = currencyService;

            webSocketServer.addEndpoint(PATH, NettyWebSocketPeerHandler.class);

            threadPool.scheduleOnClusterReady(() -> isReady = true);
        }
    }

    private NettyWebSocketSession session;
    private String currency;
    private List<ChangeSource> sources;

    @Override
    public void onOpen(NettyWebSocketSession session){

        this.session = session;

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
            logger.debug(String.format("Cannot open websocket session on {%s}: %s", PATH, e.getMessage()), e);
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

        logger.debug(String.format("[%s] Opening websocket session on {%s}, id {%s}", currency, PATH, session.getId()));

        // Listening changes
        this.sources = ImmutableList.of(new ChangeSource(currency, PeerDao.TYPE));
        ChangeService.registerListener(this);
    }

    @Override
    public void onChange(ChangeEvent event) {
        switch (event.getOperation()) {
            case CREATE:
            //case INDEX:
                sendSourceIfNotNull(event);
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

    @Override
    public void onMessage(String message) {
        // Ignoring
    }

    @Override
    public void onClose(CloseReason reason) {
        logger.debug("Closing websocket: "+reason);
        ChangeService.unregisterListener(this);
        this.session = null;
    }

    public void onError(Throwable t) {
        logger.error(String.format("[%s] Error on websocket endpoint {%s} session {%s}", currency, PATH, (session == null ? null : session.getId())), t);
    }

    /* -- internal methods -- */

    protected void sendSourceIfNotNull(ChangeEvent event) {

        if (!event.hasSource()) return; // Skip

        try {
            session.sendText(event.getSourceText());

        } catch(Exception e) {
            logger.error(String.format("[%s] Cannot sent websocket response {%s} to session {%s}: %s", currency, PATH, session.getId(), e.getMessage()), e);
        }

    }

}
