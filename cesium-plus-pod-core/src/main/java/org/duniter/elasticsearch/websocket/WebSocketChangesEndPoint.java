package org.duniter.elasticsearch.websocket;

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
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeEvents;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/_changes")
public class WebSocketChangesEndPoint implements ChangeService.ChangeListener{

    public static String PATH_PARAM_INDEX = "index";
    public static String PATH_PARAM_TYPE = "type";

    public static Collection<ChangeSource> DEFAULT_SOURCES = null;

    public static class Init {

        @Inject
        public Init(WebSocketServer webSocketServer, PluginSettings pluginSettings) {
            webSocketServer.addEndPoint(WebSocketChangesEndPoint.class);
            final String[] sourcesStr = pluginSettings.getWebSocketChangesListenSource();
            List<ChangeSource> sources = new ArrayList<>();
            for(String sourceStr : sourcesStr) {
                sources.add(new ChangeSource(sourceStr));
            }
            DEFAULT_SOURCES = sources;
        }
    }

    private final ESLogger log = Loggers.getLogger("duniter.ws.changes");
    private Session session;
    private Map<String, ChangeSource> sources;

    @OnOpen
    public void onOpen(Session session) {
        log.debug("Connected ... " + session.getId());
        this.session = session;
        this.sources = null;
        ChangeService.registerListener(this);
    }

    @Override
    public void onChange(ChangeEvent changeEvent) {
        session.getAsyncRemote().sendText(ChangeEvents.toJson(changeEvent));
    }

    @Override
    public String getId() {
        return session == null ? null : session.getId();
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
        log.debug("Closing websocket: "+reason);
        ChangeService.unregisterListener(this);
        this.session = null;
    }

    @OnError
    public void onError(Throwable t) {
        log.error("Error on websocket "+(session == null ? null : session.getId()), t);
    }


    /* -- internal methods -- */

    private void addSourceFilter(String filter) {

        ChangeSource source = new ChangeSource(filter);
        if (source.isEmpty()) {
            log.debug("Rejecting changes filter (seems to be empty): " + filter);
            return;
        }

        String sourceKey = source.toString();
        if (sources == null || !sources.containsKey(sourceKey)) {
            log.debug("Adding changes filter: " + filter);
            if (sources == null) {
                sources = Maps.newHashMap();
            }
            sources.put(sourceKey, source);
            ChangeService.refreshListener(this);
        }
    }
}
