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
import com.google.common.collect.Lists;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.json.JsonSyntaxException;
import org.duniter.elasticsearch.dao.AbstractDao;
import org.duniter.elasticsearch.dao.BlockDao;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Benoit on 30/03/2015.
 */
public class BlockDaoImpl extends AbstractDao implements BlockDao {


    public BlockDaoImpl(){
        super("duniter.dao.block");
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public void create(BlockchainBlock block, boolean wait) {
        Preconditions.checkNotNull(block);
        Preconditions.checkArgument(StringUtils.isNotBlank(block.getCurrency()));
        Preconditions.checkNotNull(block.getHash());
        Preconditions.checkNotNull(block.getNumber());

        // Serialize into JSON
        try {
            String json = getObjectMapper().writeValueAsString(block);

            // Preparing
            IndexRequestBuilder request = client.prepareIndex(block.getCurrency(), TYPE)
                    .setId(block.getNumber().toString())
                    .setSource(json);

            // Execute
            client.safeExecuteRequest(request, wait);
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     *
     * @param currencyName
     * @param id the block id
     * @param json block as JSON
     */
    public void create(String currencyName, String id, byte[] json, boolean wait) {
        Preconditions.checkNotNull(currencyName);
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(json);
        Preconditions.checkArgument(json.length > 0);

        // Preparing indexBlocksFromNode
        IndexRequestBuilder request = client.prepareIndex(currencyName, TYPE)
                .setId(id)
                .setRefresh(true)
                .setSource(json);

        // Execute
        client.safeExecuteRequest(request, wait);
    }

    public boolean isExists(String currencyName, String id) {
        return client.isDocumentExists(currencyName, TYPE, id);
    }

    public void update(BlockchainBlock block, boolean wait) {
        Preconditions.checkNotNull(block);
        Preconditions.checkArgument(StringUtils.isNotBlank(block.getCurrency()));
        Preconditions.checkNotNull(block.getHash());
        Preconditions.checkNotNull(block.getNumber());

        // Serialize into JSON
        // WARN: must use GSON, to have same JSON result (e.g identities and joiners field must be converted into String)
        try {
            String json = getObjectMapper().writeValueAsString(block);

            // Preparing
            UpdateRequestBuilder request = client.prepareUpdate(block.getCurrency(), TYPE, block.getNumber().toString())
                    .setRefresh(true)
                    .setDoc(json);

            // Execute
            client.safeExecuteRequest(request, wait);
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     *
     * @param currencyName
     * @param id the block id
     * @param json block as JSON
     */
    public void update(String currencyName, String id, byte[] json, boolean wait) {
        Preconditions.checkNotNull(currencyName);
        Preconditions.checkNotNull(json);
        Preconditions.checkArgument(json.length > 0);

        // Preparing indexBlocksFromNode
        UpdateRequestBuilder request = client.prepareUpdate(currencyName, TYPE, id)
                .setRefresh(true)
                .setDoc(json);

        // Execute
        client.safeExecuteRequest(request, wait);
    }

    public List<BlockchainBlock> findBlocksByHash(String currencyName, String query) {
        String[] queryParts = query.split("[\\t ]+");

        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(TYPE)
                .setFetchSource(true)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // If only one term, search as prefix
        if (queryParts.length == 1) {
            searchRequest.setQuery(QueryBuilders.prefixQuery("hash", query));
        }

        // If more than a word, search on terms match
        else {
            searchRequest.setQuery(QueryBuilders.matchQuery("hash", query));
        }

        // Sort as score/memberCount
        searchRequest.addSort("_score", SortOrder.DESC)
                .addSort("number", SortOrder.DESC);

        // Highlight matched words
        searchRequest.setHighlighterTagsSchema("styled")
                .addHighlightedField("hash")
                .addFields("hash")
                .addFields("*", "_source");

        // Execute query
        SearchResponse searchResponse = searchRequest.execute().actionGet();

        // Read query result
        return toBlocks(searchResponse, true);
    }

    public List<BlockchainBlock> getBlocksByIds(String currencyName, Collection<String> ids) {
        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(TYPE)
                .setSize(ids.size())
                .setFetchSource(true)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // If only one term, search as prefix
        searchRequest.setQuery(QueryBuilders.idsQuery(TYPE).addIds(ids));

        // Sort as id
        searchRequest.addSort("_id", SortOrder.ASC);

        // Execute query
        SearchResponse searchResponse = searchRequest.execute().actionGet();

        // Read query result
        return toBlocks(searchResponse, false);
    }

    public int getMaxBlockNumber(String currencyName) {
        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // Get max(number)
        searchRequest.addAggregation(AggregationBuilders.max("max_number").field("number"));

        // Execute query
        SearchResponse searchResponse = searchRequest.execute().actionGet();

        // Read query result
        Max result = searchResponse.getAggregations().get("max_number");
        if (result == null) {
            return -1;
        }

        return (result.getValue() == Double.NEGATIVE_INFINITY)
                ? -1
                : (int)result.getValue();
    }


    public BlockchainBlock getBlockById(String currencyName, String id) {
        return client.getSourceById(currencyName, TYPE, id, BlockchainBlock.class);
    }

    /**
     * Delete blocks from a start number (using bulk)
     * @param currencyName
     * @param fromNumber
     */
    public void deleteRange(final String currencyName, final int fromNumber, final int toNumber) {

        int bulkSize = pluginSettings.getIndexBulkSize();

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int number=fromNumber; number<=toNumber; number++) {

            bulkRequest.add(
                    client.prepareDelete(currencyName, TYPE, String.valueOf(number))
            );

            // Flush the bulk if not empty
            if ((fromNumber - number % bulkSize) == 0) {
                client.flushDeleteBulk(currencyName, TYPE, bulkRequest);
                bulkRequest = client.prepareBulk();
            }
        }

        // last flush
        client.flushDeleteBulk(currencyName, TYPE, bulkRequest);
    }

    @Override
    public void deleteById(String currencyName, String number) {
        client.prepareDelete(currencyName, TYPE, number).execute().actionGet();
    }



    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // currency
                    .startObject("currency")
                    .field("type", "string")
                    .endObject()

                    // version
                    .startObject("version")
                    .field("type", "integer")
                    .endObject()

                    // time
                    .startObject("time")
                    .field("type", "long")
                    .endObject()

                    // medianTime
                    .startObject("medianTime")
                    .field("type", "long")
                    .endObject()

                    // number
                    .startObject("number")
                    .field("type", "integer")
                    .endObject()

                    // nonce
                    .startObject("nonce")
                    .field("type", "long")
                    .endObject()

                    // hash
                    .startObject("hash")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // issuer
                    .startObject("issuer")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // previous hash
                    .startObject("previousHash")
                    .field("type", "string")
                    .endObject()

                    // membersCount
                    .startObject("membersCount")
                    .field("type", "integer")
                    .endObject()

                    // unitbase
                    .startObject("unitbase")
                    .field("type", "integer")
                    .endObject()

                    // monetaryMass
                    .startObject("monetaryMass")
                    .field("type", "long")
                    .endObject()

                    // dividend
                    .startObject("dividend")
                    .field("type", "integer")
                    .endObject()

                    // identities:
                    //.startObject("identities")
                    //.endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for block index: " + ioe.getMessage(), ioe);
        }
    }


    /* -- Internal methods -- */

    protected List<BlockchainBlock> toBlocks(SearchResponse response, boolean withHighlight) {
        // Read query result
        List<BlockchainBlock> result = Lists.newArrayList();

        response.getHits().forEach(searchHit -> {
            BlockchainBlock block;
            if (searchHit.source() != null) {
                String jsonString = new String(searchHit.source());
                try {
                    block = getObjectMapper().readValue(jsonString, BlockchainBlock.class);
                } catch(Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Error while parsing block from JSON:\n" + jsonString);
                    }
                    throw new JsonSyntaxException("Error while read block from JSON: " + e.getMessage(), e);
                }
            }
            else {
                block = new BlockchainBlock();
                SearchHitField field = searchHit.getFields().get("hash");
                block.setHash(field.getValue());
            }
            result.add(block);

            // If possible, use highlights
            if (withHighlight) {
                Map<String, HighlightField> fields = searchHit.getHighlightFields();
                for (HighlightField field : fields.values()) {
                    String blockNameHighLight = field.getFragments()[0].string();
                    block.setHash(blockNameHighLight);
                }
            }
        });

        return result;
    }
}
