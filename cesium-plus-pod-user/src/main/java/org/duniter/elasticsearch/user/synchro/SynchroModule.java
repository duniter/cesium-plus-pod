package org.duniter.elasticsearch.user.synchro;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
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

import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.elasticsearch.service.PeerService;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.synchro.group.SynchroGroupCommentAction;
import org.duniter.elasticsearch.user.synchro.group.SynchroGroupRecordAction;
import org.duniter.elasticsearch.user.synchro.history.SynchroHistoryIndexAction;
import org.duniter.elasticsearch.user.synchro.invitation.SynchroInvitationCertificationIndexAction;
import org.duniter.elasticsearch.user.synchro.message.SynchroMessageInboxIndexAction;
import org.duniter.elasticsearch.user.synchro.message.SynchroMessageOutboxIndexAction;
import org.duniter.elasticsearch.user.synchro.page.SynchroPageCommentAction;
import org.duniter.elasticsearch.user.synchro.page.SynchroPageRecordAction;
import org.duniter.elasticsearch.user.synchro.user.SynchroUserProfileAction;
import org.duniter.elasticsearch.user.synchro.user.SynchroUserSettingsAction;
import org.duniter.elasticsearch.user.websocket.WebsocketUserEventEndPoint;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;

public class SynchroModule extends AbstractModule implements Module {

    public static class Init {

        @Inject
        public Init(PeerService peerService, PluginSettings pluginSettings) {
            if (pluginSettings.enableSynchro()) {
                // Make sure PeerService will index ES_USER_API peers
                peerService.addIncludeEndpointApi(EndpointApi.ES_USER_API);
            }
        }
    }

    @Override protected void configure() {

        bind(Init.class).asEagerSingleton();

        // History
        bind(SynchroHistoryIndexAction.class).asEagerSingleton();

        // User
        bind(SynchroUserProfileAction.class).asEagerSingleton();
        bind(SynchroUserSettingsAction.class).asEagerSingleton();

        // Message
        bind(SynchroMessageInboxIndexAction.class).asEagerSingleton();
        bind(SynchroMessageOutboxIndexAction.class).asEagerSingleton();

        // Page
        bind(SynchroPageRecordAction.class).asEagerSingleton();
        bind(SynchroPageCommentAction.class).asEagerSingleton();

        // Group
        bind(SynchroGroupRecordAction.class).asEagerSingleton();
        bind(SynchroGroupCommentAction.class).asEagerSingleton();

        // Invitation
        bind(SynchroInvitationCertificationIndexAction.class).asEagerSingleton();

    }

}