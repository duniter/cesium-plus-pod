package org.duniter.elasticsearch.dao.impl;

/*
 * #%L
 * UCoin Java :: Core Client API
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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.AbstractDao;
import org.duniter.elasticsearch.dao.SynchroExecutionDao;
import org.duniter.elasticsearch.model.SynchroExecution;
import org.duniter.elasticsearch.model.SynchroResult;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * Created by blavenie on 29/12/15.
 */
public class SynchroExecutionDaoImpl extends AbstractDao implements SynchroExecutionDao {

    public SynchroExecutionDaoImpl(){
        super("duniter.dao.peer");
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void save(SynchroExecution execution) {
        Preconditions.checkNotNull(execution);
        Preconditions.checkArgument(StringUtils.isNotBlank(execution.getCurrency()));
        Preconditions.checkArgument(StringUtils.isNotBlank(execution.getPeer()));
        Preconditions.checkNotNull(execution.getTime());
        Preconditions.checkArgument(execution.getTime() > 0);

        try {
            // Serialize into JSON
            String json = getObjectMapper().writeValueAsString(execution);

            // Preparing indexBlocksFromNode
            IndexRequestBuilder indexRequest = client.prepareIndex(execution.getCurrency(), TYPE)
                    .setSource(json);

            // Execute indexBlocksFromNode
            indexRequest
                    .setRefresh(true)
                    .execute().actionGet();
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    @Override
    public SynchroExecution getLastExecution(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());
        Preconditions.checkNotNull(peer.getId());
        Preconditions.checkNotNull(peer.getApi());

        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(SynchroExecution.PROPERTY_PEER, peer.getId()))
                .filter(QueryBuilders.termQuery(SynchroExecution.PROPERTY_API, peer.getApi()));

        SearchResponse response = client.prepareSearch(peer.getCurrency())
                .setTypes(TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .setFetchSource(true)
                .setSize(1)
                .addSort(SynchroExecution.PROPERTY_TIME, SortOrder.DESC)
                .get();

        if (response.getHits().getTotalHits() == 0) return null;

        SearchHit hit = response.getHits().getHits()[0];
        return client.readSourceOrNull(hit, SynchroExecution.class);
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // currency
                    .startObject(SynchroExecution.PROPERTY_CURRENCY)
                    .field("type", "string")
                    .endObject()

                    // peer
                    .startObject(SynchroExecution.PROPERTY_PEER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // peer
                    .startObject(SynchroExecution.PROPERTY_API)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // issuer
                    .startObject(SynchroExecution.PROPERTY_ISSUER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // time
                    .startObject(SynchroExecution.PROPERTY_TIME)
                    .field("type", "long")
                    .endObject()

                    // hash
                    .startObject(SynchroExecution.PROPERTY_HASH)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // signature
                    .startObject(SynchroExecution.PROPERTY_SIGNATURE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // execution time
                    .startObject(SynchroExecution.PROPERTY_EXECUTION_TIME)
                    .field("type", "long")
                    .endObject()

                    // result
                    .startObject(SynchroExecution.PROPERTY_RESULT)
                    .field("type", "nested")
                    .field("dynamic", "false")
                    .startObject("properties")

                        // inserts
                        .startObject(SynchroResult.PROPERTY_INSERTS)
                        .field("type", "long")
                        .endObject()

                        // updates
                        .startObject(SynchroResult.PROPERTY_UPDATES)
                        .field("type", "long")
                        .endObject()

                        // deletes
                        .startObject(SynchroResult.PROPERTY_DELETES)
                        .field("type", "long")
                        .endObject()

                        // deletes
                        .startObject(SynchroResult.PROPERTY_INVALID_SIGNATURES)
                        .field("type", "long")
                        .endObject()

                    .endObject()
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for synchro index: " + ioe.getMessage(), ioe);
        }
    }
}
