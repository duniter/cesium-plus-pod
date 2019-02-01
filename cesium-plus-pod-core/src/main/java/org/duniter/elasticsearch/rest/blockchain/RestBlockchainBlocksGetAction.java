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
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.CurrencyService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestBlockchainBlocksGetAction extends BaseRestHandler {

    private CurrencyService currencyService;

    @Inject
    public RestBlockchainBlocksGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                         CurrencyService currencyService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/blocks/[0-9]+/[0-9]+");

        controller.registerHandler(RestRequest.Method.GET, "/blockchain/blocks/{count}/{from}", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/blocks/{count}/{from}", this);

        this.currencyService = currencyService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) {
        String currency = currencyService.safeGetCurrency(request.param("index"));
        int count = request.paramAsInt("count", 100);
        int from = request.paramAsInt("from", 0);
        String[] includes = request.paramAsStringArray("_source", null);
        String[] excludes = request.paramAsStringArray("_source_exclude", null);

        try {
            SearchRequestBuilder req = client.prepareSearch(currency)
                    .setTypes(BlockDao.TYPE)
                    .setFrom(0)
                    .setSize(count)
                    .setQuery(QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery(BlockchainBlock.PROPERTY_NUMBER).gte(from))))
                    .setFetchSource(includes, excludes)
                    .addSort(BlockchainBlock.PROPERTY_NUMBER, SortOrder.ASC);
            SearchResponse resp = req.execute().actionGet();

            BytesStreamOutput bso = new BytesStreamOutput();

            boolean first = true;
            bso.write('[');
            for (SearchHit hit: resp.getHits().getHits()) {
                BytesReference bytes = hit.getSourceRef();
                if (bytes != null) {
                    if (!first) bso.write(',');
                    hit.getSourceRef().writeTo(bso);
                    first = false;
                }
            }
            bso.write(']');
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, XContentType.JSON.restContentType(), bso.bytes()));
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/blocks/<count>/<from>]: %s", ioe.getMessage()), ioe);
        }
    }
}