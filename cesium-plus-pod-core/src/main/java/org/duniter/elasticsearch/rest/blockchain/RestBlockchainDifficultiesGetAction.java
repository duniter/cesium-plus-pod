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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.CurrencyService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestBlockchainDifficultiesGetAction extends BaseRestHandler {

    private Client client;
    private CurrencyService currencyService;
    private BlockchainService blockchainService;

    @Inject
    public RestBlockchainDifficultiesGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                               CurrencyService currencyService,
                                               BlockchainService blockchainService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/difficulties");

        controller.registerHandler(RestRequest.Method.GET, "/blockchain/difficulties", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/difficulties", this);

        this.client = client;
        this.currencyService = currencyService;
        this.blockchainService = blockchainService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) {
        String currency = currencyService.safeGetCurrency(request.param("index"));


        try {
            int currentBlockNumber = blockchainService.getMaxBlockNumber(currency);

            final XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                    .field("block", currentBlockNumber + 1)
                    .startArray("levels");
            Map<String, Integer> difficulties = blockchainService.getDifficulties(currency);

            if (MapUtils.isNotEmpty(difficulties)) {
                // Sort by level
                List<Map.Entry<String, Integer>> difficultyEntries = Lists.newArrayList(difficulties.entrySet());
                difficultyEntries.sort(Map.Entry.comparingByValue());

                // Add as json object
                for (Map.Entry<String, Integer> level : difficultyEntries) {
                    builder.startObject()
                            .field("uid", level.getKey())
                            .field("level", level.getValue())
                            .endObject();
                }
            }
            builder.endArray().endObject();
            channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
        } catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/difficulties]: %s", ioe.getMessage()), ioe);
        }
    }
}