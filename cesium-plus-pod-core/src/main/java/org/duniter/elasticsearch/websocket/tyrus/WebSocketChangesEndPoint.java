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

/*
    Copyright 2015 ForgeRock AS

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.http.tyrus.TyrusWebSocketServer;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeEvents;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ServerEndpoint(value = "/_changes")
public class WebSocketChangesEndPoint implements ChangeService.ChangeListener{

    public static Collection<ChangeSource> DEFAULT_SOURCES = null;

    private static ESLogger logger;
    private static ThreadPool threadPool;
    private Session session;
    private Map<String, ChangeSource> sources;
    private String sessionId;

    public static class Init {

        @Inject
        public Init(TyrusWebSocketServer webSocketServer, PluginSettings pluginSettings,
                    ThreadPool threadPoolInstance) {
            logger = Loggers.getLogger("duniter.ws.changes", pluginSettings.getSettings(), new String[0]);
            threadPool = threadPoolInstance;

            // Init default sources
            final String[] sourcesStr = pluginSettings.getWebSocketChangesListenSource();
            List<ChangeSource> sources = new ArrayList<>();
            for(String sourceStr : sourcesStr) {
                sources.add(new ChangeSource(sourceStr));
            }
            DEFAULT_SOURCES = sources;

            // Register endpoint
            webSocketServer.addEndPoint(WebSocketChangesEndPoint.class);
        }
    }


    @OnOpen
    public void onOpen(Session session) {
        if (logger.isDebugEnabled())
            logger.debug(String.format("Opening websocket session id {%s}. Waiting for sources...", session.getId()));

        synchronized (this) {
            this.session = session;
            this.sessionId = "duniter.ws.changes#" + session.getId();
            this.sources = null;
        }

        // Wait 10s that sources
        threadPool.schedule(() -> checkHasSourceOrClose(), 30, TimeUnit.SECONDS);
    }

    @Override
    public void onChange(ChangeEvent changeEvent) {
        session.getAsyncRemote().sendText(ChangeEvents.toJson(changeEvent));
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public Collection<ChangeSource> getChangeSources() {
        if (MapUtils.isEmpty(sources)) return DEFAULT_SOURCES;
        return sources.values();
    }

    @OnMessage
    public void onMessage(String message) {
        addSourceFilter(message);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        if (logger.isDebugEnabled())  {
            if (reason != null && reason.getCloseCode() != CloseReason.CloseCodes.GOING_AWAY)
                logger.debug(String.format("Closing websocket session, id {%s} - reason {%s}: %s",  sessionId,  reason.getCloseCode(), reason.getReasonPhrase()));
            else
                logger.debug(String.format("Closing websocket session, id {%s}"));
        }
        synchronized (this) {
            ChangeService.unregisterListener(this);
        }
        this.session = null;
    }

    @OnError
    public void onError(Throwable t) {
        logger.error(String.format("Error on websocket session, id {%s}", sessionId), t);
    }


    /* -- internal methods -- */

    private void checkHasSourceOrClose() {
        synchronized (this) {
            if (session != null && MapUtils.isEmpty(sources)) {
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Missing changes sources to listen (must be send < 20s after connection)"));
                }
                catch (IOException e) {
                    logger.error(String.format("Failed to close Web socket session, id {%s}", sessionId), e);
                    ChangeService.unregisterListener(this); // Make sure to unregister anyway
                }
            }
        }
    }

    private void addSourceFilter(String filter) {

        ChangeSource source = new ChangeSource(filter);
        if (source.isEmpty()) {
            if (logger.isDebugEnabled()) logger.debug("Rejecting changes filter (seems to be empty): " + filter);
            return;
        }

        String sourceKey = source.toString();
        synchronized (this) {
            if (sources == null || !sources.containsKey(sourceKey)) {
                if (logger.isDebugEnabled())
                    logger.debug(String.format("Adding changes {%s}, id {%s}", filter, sessionId));
                if (sources == null) {
                    sources = Maps.newHashMap();
                    sources.put(sourceKey, source);
                    ChangeService.registerListener(this);
                }
                else {
                    // Replace the sourceKey, then refresh the listener registration
                    sources.put(sourceKey, source);
                    ChangeService.refreshListener(this);
                }
            }
        }
    }
}
