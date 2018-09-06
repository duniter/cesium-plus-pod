package org.duniter.elasticsearch.user.websocket;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.duniter.core.client.model.bma.Constants;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.service.UserEventService;
import org.duniter.elasticsearch.websocket.WebSocketServer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.nuiton.i18n.I18n;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

@ServerEndpoint(value = "/event/user/{pubkey}/{locale}")
public class WebsocketUserEventEndPoint implements UserEventService.UserEventListener {

    public static Locale defaultLocale;
    public static ObjectMapper mapper;

    public static class Init {

        @Inject
        public Init(WebSocketServer webSocketServer, PluginSettings pluginSettings) {
            webSocketServer.addEndPoint(WebsocketUserEventEndPoint.class);
            defaultLocale = pluginSettings.getI18nLocale();
            if (defaultLocale == null) defaultLocale = new Locale("en", "GB");

            // Define a static mapper
            mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }

    private static final String PATH_PARAM_PUBKEY = "pubkey";
    private static final String PATH_PARAM_LOCALE = "locale";
    private final static Pattern PUBKEY_PATTERN = Pattern.compile(Constants.Regex.PUBKEY);

    private final ESLogger log = Loggers.getLogger("duniter.ws.user.event");
    private Session session;
    private String pubkey;
    private Locale locale;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        this.pubkey = session.getPathParameters() != null ? session.getPathParameters().get(PATH_PARAM_PUBKEY) : null;
        this.locale = session.getPathParameters() != null ? new Locale(session.getPathParameters().get(PATH_PARAM_LOCALE)) : defaultLocale;

        if (StringUtils.isBlank(pubkey) || !PUBKEY_PATTERN.matcher(pubkey).matches()) {
            try {
                this.session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "Invalid pubkey"));
            } catch (IOException e) {
                // silent
            }
            return;
        }

        log.debug(I18n.t("duniter4j.ws.user.open", pubkey, session.getId(), locale.toString()));
        UserEventService.registerListener(this);
    }

    @Override
    public void onEvent(UserEvent event) {
        try {
            UserEvent copy = new UserEvent(event);
            copy.setMessage(copy.getLocalizedMessage(locale));
            String json = mapper.writeValueAsString(copy);

            // Force to serialized 'id' (skip @JsonIgnore) - fix #12
            json = "{\"id\":\""+event.getId()+"\"," + json.substring(1);

            session.getAsyncRemote().sendText(json);
        } catch(IOException e) {
            // silent
        }
    }

    @Override
    public String getId() {
        return session == null ? null : session.getId();
    }

    @Override
    public String getPubkey() {
        return pubkey;
    }

    @OnMessage
    public void onMessage(String message) {
        log.debug("Received message: "+message);
    }

    @OnClose
    public void onClose(CloseReason reason) {
        log.debug("Closing websocket: "+reason);
        UserEventService.unregisterListener(this);
        this.session = null;
    }

    @OnError
    public void onError(Throwable t) {
        log.error("Error on websocket "+(session == null ? null : session.getId()), t);
    }

}
