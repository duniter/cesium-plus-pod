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
import org.apache.http.entity.ContentType;
import org.duniter.core.client.model.bma.NetworkPeering;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.NetworkService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.nuiton.i18n.I18n;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNetworkPeeringGetAction extends BaseRestHandler {


    private NetworkService networkService;

    @Inject
    public RestNetworkPeeringGetAction(Settings settings, PluginSettings pluginSettings, RestController controller, Client client, RestSecurityController securityController,
                                       NetworkService networkService) {
        super(settings, controller, client);

        if (StringUtils.isBlank(pluginSettings.getClusterRemoteHost())) {
            logger.warn(String.format("The cluster address can not be published on the network. /\\!\\\\ Fill in the options [cluster.remote.xxx] in the configuration (recommended)."));
        }
        else {
            securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/network/peering");

            controller.registerHandler(RestRequest.Method.GET, "/network/peering", this);
            controller.registerHandler(RestRequest.Method.GET, "/{currency}/network/peering", this);
        }

        this.networkService = networkService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("currency");
        NetworkPeering peering = networkService.getLastPeering(currency);

        try {
            channel.sendResponse(new BytesRestResponse(RestStatus.OK,
                    ContentType.APPLICATION_JSON.toString(),
                    getObjectMapper()
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(peering)));
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/network/peering]: %s", ioe.getMessage()), ioe);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}