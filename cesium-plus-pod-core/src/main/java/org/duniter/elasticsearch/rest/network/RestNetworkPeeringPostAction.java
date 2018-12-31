package org.duniter.elasticsearch.rest.network;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.http.entity.ContentType;
import org.duniter.core.client.model.bma.NetworkPeering;
import org.duniter.core.client.model.bma.NetworkPeerings;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.rest.XContentThrowableRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.NetworkService;
import org.duniter.elasticsearch.service.PeerService;
import org.duniter.elasticsearch.service.ServiceLocator;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.*;
import org.nuiton.i18n.I18n;
import org.yaml.snakeyaml.util.UriEncoder;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNetworkPeeringPostAction extends BaseRestHandler {


    private NetworkService networkService;

    @Inject
    public RestNetworkPeeringPostAction(Settings settings, PluginSettings pluginSettings, RestController controller, Client client,
                                        RestSecurityController securityController,
                                        NetworkService networkService) {
        super(settings, controller, client);

        if (StringUtils.isBlank(pluginSettings.getClusterRemoteHost())) {
            logger.warn(String.format("The cluster address can not be published on the network. /\\!\\\\ Fill in the options [cluster.remote.xxx] in the configuration (recommended)."));
        }
        else {
            securityController.allow(RestRequest.Method.POST, "/network/peering");
            controller.registerHandler(RestRequest.Method.POST, "/network/peering", this);
        }

        this.networkService = networkService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {

        try {
            Properties content = new Properties();
            content.load(new StringReader(request.content().toUtf8()));

            String peerDocument = content.getProperty("peer");
            if (StringUtils.isBlank(peerDocument)) {
                throw new TechnicalException("Inavlid request: 'peer' property not found");
            }

            // Decode content
            peerDocument = UriEncoder.decode(peerDocument);
            logger.debug("Received peer document: " + peerDocument);

            NetworkPeering peering = networkService.checkAndSavePeering(peerDocument);

            channel.sendResponse(new BytesRestResponse(
                    RestStatus.OK,
                    ContentType.APPLICATION_JSON.toString(),
                    getObjectMapper()
                            .writerWithDefaultPrettyPrinter() // enable pretty printer
                            .writeValueAsBytes(peering)));
        }
        catch(Exception e) {
            logger.debug("Error while parsing peer document: " + e.getMessage());
            channel.sendResponse(new XContentThrowableRestResponse(request, e));
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}