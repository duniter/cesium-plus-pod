package org.duniter.elasticsearch.user.websocket.netty;

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
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.http.netty.NettyWebSocketServer;
import org.duniter.elasticsearch.http.netty.websocket.NettyBaseWebSocketEndpoint;
import org.duniter.elasticsearch.http.netty.websocket.NettyWebSocketSession;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.service.UserEventService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.nuiton.i18n.I18n;

import javax.websocket.CloseReason;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;

public class NettyWebSocketUserEventHandler extends NettyBaseWebSocketEndpoint implements UserEventService.UserEventListener {

    private final static String PATH = WEBSOCKET_PATH + "/event/user/{pubkey}/{locale}";

    private static ESLogger logger;
    public static Locale defaultLocale;
    public static ObjectMapper mapper;

    public static class Init {

        @Inject
        public Init(NettyWebSocketServer webSocketServer, PluginSettings pluginSettings) {
            logger = Loggers.getLogger("duniter.ws.event", pluginSettings.getSettings(), new String[0]);

            // Default locale
            defaultLocale = pluginSettings.getI18nLocale();
            if (defaultLocale == null) defaultLocale = new Locale("en", "GB");

            // Define a static mapper
            mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            // Register endpoint
            webSocketServer.addEndpoint(PATH, NettyWebSocketUserEventHandler.class);
        }
    }

    private static final String PATH_PARAM_PUBKEY = "pubkey";
    private static final String PATH_PARAM_LOCALE = "locale";
    private final static Pattern PUBKEY_PATTERN = Pattern.compile(Constants.Regex.PUBKEY);

    private NettyWebSocketSession session;
    private String pubkey;
    private Locale locale;

    @OnOpen
    public void onOpen(NettyWebSocketSession session){
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

        logger.debug(I18n.t("duniter4j.ws.user.open", pubkey, session.getId(), locale.toString()));
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

            session.sendText(json);
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
        logger.debug("Received message: "+message);
    }

    @Override
    public void onClose(CloseReason reason) {
        logger.debug("Closing websocket: "+reason);
        UserEventService.unregisterListener(this);
        this.session = null;
    }

    @OnError
    public void onError(Throwable t) {
        logger.error("Error on websocket "+(session == null ? null : session.getId()), t);
    }

}
