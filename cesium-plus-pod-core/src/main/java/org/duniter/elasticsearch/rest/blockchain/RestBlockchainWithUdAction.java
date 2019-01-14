package org.duniter.elasticsearch.rest.blockchain;

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
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.XContentThrowableRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.BlockchainService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;

import static org.elasticsearch.ExceptionsHelper.detailedMessage;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestBlockchainWithUdAction extends BaseRestHandler {

    private BlockchainService blockchainService;

    @Inject
    public RestBlockchainWithUdAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                      BlockchainService blockchainService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/with/ud");

        controller.registerHandler(RestRequest.Method.GET, "/blockchain/with/ud", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/with/ud", this);

        this.blockchainService = blockchainService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("index");

        try {
            XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                    .startObject("result")
                    .field("blocks", blockchainService.getBlockNumberWithUd(currency))
                    .endObject()
                    .endObject();
            channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
        } catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/with/ud]: %s", ioe.getMessage()), ioe);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}