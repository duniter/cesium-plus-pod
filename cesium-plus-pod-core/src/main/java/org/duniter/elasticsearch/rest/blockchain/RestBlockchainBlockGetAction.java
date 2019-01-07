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
import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.rest.AbstractRestPostIndexAction;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.CurrencyService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.*;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestBlockchainBlockGetAction extends BaseRestHandler {

    private BlockchainService blockchainService;

    @Inject
    public RestBlockchainBlockGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                        BlockchainService blockchainService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/block/[0-9]+");
        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/current");

        controller.registerHandler(RestRequest.Method.GET, "/blockchain/block/{number}", this);
        controller.registerHandler(RestRequest.Method.GET, "/blockchain/current", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/block/{number}", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/current", this);

        this.blockchainService = blockchainService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("index");
        int number = request.paramAsInt("number", -1);
        boolean isCurrent = (number == -1);

        BlockchainBlock block;
        if (isCurrent) {
            block = blockchainService.getCurrentBlock(currency);
        }
        else {
            block = blockchainService.getBlockById(currency, number);
        }

        try {
            channel.sendResponse(new BytesRestResponse(RestStatus.OK,
                    ContentType.APPLICATION_JSON.toString(),
                    getObjectMapper()
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(block)));
        }
        catch(IOException ioe) {
            if (isCurrent)
                throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/current]: %s", ioe.getMessage()), ioe);
            else
                throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/block/%s]: %s", number, ioe.getMessage()), ioe);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}