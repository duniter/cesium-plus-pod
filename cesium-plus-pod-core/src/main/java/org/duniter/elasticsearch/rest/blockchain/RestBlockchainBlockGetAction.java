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

import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.CurrencyService;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.*;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestBlockchainBlockGetAction extends BaseRestHandler {

    private Client client;
    private CurrencyService currencyService;

    @Inject
    public RestBlockchainBlockGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                        CurrencyService currencyService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/block/[0-9]+");
        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/blockchain/current");

        controller.registerHandler(RestRequest.Method.GET, "/blockchain/block/{number}", this);
        controller.registerHandler(RestRequest.Method.GET, "/blockchain/current", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/block/{number}", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/blockchain/current", this);

        this.client = client;
        this.currencyService = currencyService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) {
        String currency = currencyService.safeGetCurrency(request.param("index"));
        String number = request.param("number");
        boolean isCurrent = StringUtils.isBlank(number);
        String[] includes = request.paramAsStringArray("_source", null);
        String[] excludes = request.paramAsStringArray("_source_exclude", null);

        try {
            GetResponse response = client.prepareGet(currency, BlockDao.TYPE, isCurrent ? "current" : number)
                    .setFetchSource(includes, excludes)
                    .execute().actionGet();

            BytesStreamOutput bso = new BytesStreamOutput();
            response.getSourceAsBytesRef().writeTo(bso);
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, XContentType.JSON.restContentType(), bso.bytes()));
        }
        catch(IOException ioe) {
            if (isCurrent)
                throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/current]: %s", ioe.getMessage()), ioe);
            else
                throw new TechnicalException(String.format("Error while generating JSON for [/blockchain/block/%s]: %s", number, ioe.getMessage()), ioe);
        }
    }
}