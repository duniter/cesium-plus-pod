package org.duniter.elasticsearch.rest.wot;

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
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.BlockchainService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestWotLookupGetAction extends BaseRestHandler {

    private Client client;
    private BlockchainService blockchainService;

    @Inject
    public RestWotLookupGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                  BlockchainService blockchainService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/wot/lookup/[^/]+");

        controller.registerHandler(RestRequest.Method.GET, "/wot/lookup/{uid}", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/wot/lookup/{uid}", this);

        this.client = client;
        this.blockchainService = blockchainService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("index");
        String uid = request.param("uid");

        try {
            // TODO: implement
            XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                    .field("partials", false)
                    .field("results", new Object[0])
                    .endObject();
            channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/wot/lookup]: %s", ioe.getMessage()), ioe);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}