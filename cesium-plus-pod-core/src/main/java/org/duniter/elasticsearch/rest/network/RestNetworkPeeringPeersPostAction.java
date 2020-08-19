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
import org.duniter.core.client.model.bma.NetworkPeering;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.rest.JacksonJsonRestResponse;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.XContentThrowableRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.NetworkService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.StringReader;
import java.util.Properties;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNetworkPeeringPeersPostAction extends BaseRestHandler {


    private NetworkService networkService;

    @Inject
    public RestNetworkPeeringPeersPostAction(Settings settings, PluginSettings pluginSettings, RestController controller, Client client,
                                             RestSecurityController securityController,
                                             NetworkService networkService) {
        super(settings, controller, client);

        if (StringUtils.isBlank(pluginSettings.getClusterRemoteHost())) {
            logger.warn(String.format("The cluster address can not be published on the network. /\\!\\\\ Fill in the options [cluster.remote.xxx] in the configuration (recommended)."));
        }
        else {
            securityController.allow(RestRequest.Method.POST, "(/[^/]+)?/network/peering/peers");

            controller.registerHandler(RestRequest.Method.POST, "/network/peering/peers", this);
            controller.registerHandler(RestRequest.Method.POST, "/{currency}/network/peering/peers", this);
        }

        this.networkService = networkService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {

        String currency = request.param("currency");

        try {
            Properties content = new Properties();
            content.load(new StringReader(request.content().toUtf8()));

            String peerDocument = content.getProperty("peer");
            if (StringUtils.isBlank(peerDocument)) {
                XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                        .field("ucode", 1103)
                        .field("message", "Requires a peer")
                        .endObject();
                channel.sendResponse(new XContentRestResponse(request, RestStatus.BAD_REQUEST, builder));
            }
            else {

                // Decode content, if need
                //logger.debug("Converting peer document:\n" + peerDocument);
                //peerDocument = UriEncoder.decode(peerDocument);

                if (logger.isDebugEnabled()) {
                    logger.debug("Received peer document:\n" + peerDocument);
                }

                NetworkPeering peering = networkService.checkAndSavePeering(currency, peerDocument);

                channel.sendResponse(new JacksonJsonRestResponse(request, RestStatus.OK, peering));
            }
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