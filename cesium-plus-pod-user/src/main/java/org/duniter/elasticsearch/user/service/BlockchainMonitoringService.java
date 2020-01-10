package org.duniter.elasticsearch.user.service;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
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


import org.duniter.core.service.CryptoService;
import org.duniter.core.util.websocket.WebsocketClientEndpoint;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

/**
 * Created by Benoit on 30/03/2015.
 */
public class BlockchainMonitoringService extends AbstractService  {


    private final AdminService adminService;

    @Inject
    public BlockchainMonitoringService(Duniter4jClient client, PluginSettings pluginSettings, CryptoService cryptoService,
                                       BlockchainService blockchainService,
                                       AdminService adminService) {
        super("duniter.blockchain.monitoring", client, pluginSettings, cryptoService);
        this.adminService = adminService;

        // Should notify admin when connection to node is DOWN ?
        if (pluginSettings.enableBlockchainAdminEventIndexation()) {
            blockchainService.registerConnectionListener(createConnectionListeners());
        }
    }

    /* -- internal method -- */

    /**
     * Create a listener that notify admin when the Duniter node connection is lost or retrieve
     */
    private WebsocketClientEndpoint.ConnectionListener createConnectionListeners() {
        return new WebsocketClientEndpoint.ConnectionListener() {
            private boolean errorNotified = false;

            @Override
            public void onSuccess() {
                // Send notify on reconnection
                if (errorNotified) {
                    errorNotified = false;
                    adminService.notifyAdmin(UserEvent.newBuilder(UserEvent.EventType.INFO, UserEventCodes.NODE_BMA_UP.name())
                            .setMessage(I18n.n("duniter.user.event.NODE_BMA_UP"),
                                    pluginSettings.getDuniterNodeHost(),
                                    String.valueOf(pluginSettings.getDuniterNodePort()),
                                    pluginSettings.getClusterName())
                            .build());
                }
            }

            @Override
            public void onError(Exception e, long lastTimeUp) {
                if (errorNotified) return; // already notify

                // Wait 1 min, then notify admin (once)
                long now = System.currentTimeMillis() / 1000;
                boolean wait = now - lastTimeUp < 60;
                if (!wait) {
                    errorNotified = true;
                    adminService.notifyAdmin(UserEvent.newBuilder(UserEvent.EventType.ERROR, UserEventCodes.NODE_BMA_DOWN.name())
                            .setMessage(I18n.n("duniter.user.event.NODE_BMA_DOWN"),
                                    pluginSettings.getDuniterNodeHost(),
                                    String.valueOf(pluginSettings.getDuniterNodePort()),
                                    pluginSettings.getClusterName(),
                                    String.valueOf(lastTimeUp))
                            .build());
                }
            }
        };
    }


}
