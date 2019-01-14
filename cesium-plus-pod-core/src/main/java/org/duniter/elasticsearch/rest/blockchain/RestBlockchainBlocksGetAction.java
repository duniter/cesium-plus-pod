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

import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.CurrencyService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestBlockchainBlocksGetAction extends BaseRestHandler {

    private Client client;
    private CurrencyService currencyService;

    @Inject
    public RestBlockchainBlocksGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                         CurrencyService currencyService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/blocks/[0-9]+/[0-9]+");

        controller.registerHandler(RestRequest.Method.GET, "/blockchain/blocks/{count}/{from}", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/blocks/{count}/{from}", this);

        this.client = client;
        this.currencyService = currencyService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) {
        String currency = currencyService.safeGetCurrency(request.param("index"));
        int count = request.paramAsInt("count", 100);
        int from = request.paramAsInt("from", 0);

        try {
            SearchRequestBuilder req = client.prepareSearch(currency)
                    .setTypes(BlockDao.TYPE)
                    .setFrom(0)
                    .setSize(count)
                    .setQuery(QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery(BlockchainBlock.PROPERTY_NUMBER).lte(from))))
                    .setFetchSource(true)
                    .addSort(BlockchainBlock.PROPERTY_NUMBER, SortOrder.ASC);

            XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startArray();

            SearchResponse resp = req.execute().actionGet();
            for (SearchHit hit: resp.getHits().getHits()) {
                builder.rawValue(hit.getSourceRef());
            }
            builder.endArray();

            channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/blocks/<count>/<from>]: %s", ioe.getMessage()), ioe);
        }
    }
}