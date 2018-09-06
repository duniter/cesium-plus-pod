package org.duniter.elasticsearch.dao.impl;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
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


import com.fasterxml.jackson.core.JsonProcessingException;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.AbstractDao;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.dao.MovementDao;
import org.duniter.elasticsearch.model.Movement;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;

/**
 * Created by Benoit on 30/03/2015.
 */
public class MovementDaoImpl extends AbstractDao implements MovementDao {

    public MovementDaoImpl(){
        super("duniter.dao.movement");
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public void create(Movement operation, boolean wait) {
        Preconditions.checkNotNull(operation);
        Preconditions.checkArgument(StringUtils.isNotBlank(operation.getCurrency()));
        Preconditions.checkNotNull(operation.getIssuer());
        Preconditions.checkNotNull(operation.getRecipient());
        Preconditions.checkNotNull(operation.getAmount());

        // Serialize into JSON
        try {
            String json = getObjectMapper().writeValueAsString(operation);

            // Preparing
            IndexRequestBuilder request = client.prepareIndex(operation.getCurrency(), TYPE)
                    .setRefresh(false)
                    .setSource(json);

            // Execute
            client.safeExecuteRequest(request, wait);
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    public boolean isExists(String currencyName, String id) {
        return client.isDocumentExists(currencyName, TYPE, id);
    }

    public void update(Movement operation, boolean wait) {
        Preconditions.checkNotNull(operation);
        Preconditions.checkArgument(StringUtils.isNotBlank(operation.getCurrency()));
        Preconditions.checkNotNull(operation.getIssuer());
        Preconditions.checkNotNull(operation.getRecipient());
        Preconditions.checkNotNull(operation.getAmount());

        // Serialize into JSON
        try {
            String json = getObjectMapper().writeValueAsString(operation);

            // Preparing
            UpdateRequestBuilder request = client.prepareUpdate(operation.getCurrency(), TYPE, operation.getId())
                    .setRefresh(true)
                    .setDoc(json);

            // Execute
            client.safeExecuteRequest(request, wait);
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    @Override
    public void delete(String currency, String id, boolean wait) {
        Preconditions.checkNotNull(currency);
        Preconditions.checkNotNull(id);

        // Preparing request
        DeleteRequestBuilder request = client.prepareDelete(currency, TYPE, id);

        // Execute
        client.safeExecuteRequest(request, wait);
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // --- BLOCK properties ---

                    // currency
                    .startObject(Movement.PROPERTY_CURRENCY)
                    .field("type", "string")
                    .endObject()

                    // medianTime
                    .startObject(Movement.PROPERTY_MEDIAN_TIME)
                    .field("type", "long")
                    .endObject()

                    // --- TX properties ---

                    // version
                    .startObject(Movement.PROPERTY_VERSION)
                    .field("type", "integer")
                    .endObject()

                    // issuer
                    .startObject(Movement.PROPERTY_ISSUER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // recipient
                    .startObject(Movement.PROPERTY_RECIPIENT)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // amount
                    .startObject(Movement.PROPERTY_AMOUNT)
                    .field("type", "long")
                    .endObject()

                    // unitbase
                    .startObject(Movement.PROPERTY_UNITBASE)
                    .field("type", "integer")
                    .endObject()

                    // comment
                    .startObject(Movement.PROPERTY_COMMENT)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // --- OTHER properties ---

                    // is UD ?
                    .startObject(Movement.PROPERTY_IS_UD)
                    .field("type", "boolean")
                    .field("index", "not_analyzed")
                    .endObject()

                    // reference
                    .startObject(Movement.PROPERTY_REFERENCE)
                        .field("type", "nested")
                        .field("dynamic", "false")
                        .startObject("properties")
                            // reference.index
                            .startObject(Movement.Reference.PROPERTY_INDEX)
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            // reference.index
                            .startObject(Movement.Reference.PROPERTY_TYPE)
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject(Movement.Reference.PROPERTY_ID)
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject(Movement.Reference.PROPERTY_HASH)
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject(Movement.Reference.PROPERTY_ANCHOR)
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                        .endObject()
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for block operation index: " + ioe.getMessage(), ioe);
        }
    }

    public BulkRequestBuilder bulkDeleteByBlock(final String currency,
                                                final String number,
                                                final String hash,
                                                BulkRequestBuilder bulkRequest,
                                                final int bulkSize,
                                                final boolean flushAll) {

        Preconditions.checkNotNull(currency);
        Preconditions.checkNotNull(number);
        Preconditions.checkNotNull(bulkRequest);
        Preconditions.checkArgument(bulkSize > 0);

        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currency)
                .setTypes(TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        // Query = filter on reference
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(Movement.PROPERTY_REFERENCE + "." + Movement.Reference.PROPERTY_INDEX, currency))
                .filter(QueryBuilders.termQuery(Movement.PROPERTY_REFERENCE + "." + Movement.Reference.PROPERTY_TYPE, BlockDao.TYPE))
                .filter(QueryBuilders.termQuery(Movement.PROPERTY_REFERENCE + "." + Movement.Reference.PROPERTY_ID, number));
        if (StringUtils.isNotBlank(hash)) {
            boolQuery.filter(QueryBuilders.termQuery(Movement.PROPERTY_REFERENCE + "." + Movement.Reference.PROPERTY_HASH, hash));
        }

        searchRequest.setQuery(QueryBuilders.nestedQuery(Movement.PROPERTY_REFERENCE, QueryBuilders.constantScoreQuery(boolQuery)));

        // Execute query, while there is some data
        return client.bulkDeleteFromSearch(currency, TYPE, searchRequest, bulkRequest, bulkSize, flushAll);
    }

    public BulkRequestBuilder bulkDeleteByBlock(final BlockchainBlock block,
                                                BulkRequestBuilder bulkRequest,
                                                final int bulkSize,
                                                final boolean flushAll) {
        Preconditions.checkNotNull(block);

        return bulkDeleteByBlock(block.getCurrency(), String.valueOf(block.getNumber()), block.getHash(), bulkRequest, bulkSize, flushAll);
    }

    /* -- Internal methods -- */

}
