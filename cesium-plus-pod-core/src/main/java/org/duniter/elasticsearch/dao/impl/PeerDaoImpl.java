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
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.AbstractDao;
import org.duniter.elasticsearch.dao.PeerDao;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;
import org.elasticsearch.search.aggregations.metrics.max.Max;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by blavenie on 29/12/15.
 */
public class PeerDaoImpl extends AbstractDao implements PeerDao {

    public PeerDaoImpl(){
        super("duniter.dao.peer");
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Peer create(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkArgument(StringUtils.isNotBlank(peer.getId()));
        Preconditions.checkArgument(StringUtils.isNotBlank(peer.getCurrency()));
        //Preconditions.checkNotNull(peer.getHash());
        Preconditions.checkNotNull(peer.getHost());
        Preconditions.checkNotNull(peer.getApi());

        // Serialize into JSON
        // WARN: must use GSON, to have same JSON result (e.g identities and joiners field must be converted into String)
        try {
            String json = getObjectMapper().writeValueAsString(peer);

            // Preparing indexBlocksFromNode
            IndexRequestBuilder indexRequest = client.prepareIndex(peer.getCurrency(), TYPE)
                    .setId(peer.getId())
                    .setSource(json);

            // Execute indexBlocksFromNode
            indexRequest
                    .setRefresh(true)
                    .execute();
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
        return peer;
    }

    @Override
    public Peer update(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkArgument(StringUtils.isNotBlank(peer.getId()));
        Preconditions.checkArgument(StringUtils.isNotBlank(peer.getCurrency()));
        //Preconditions.checkNotNull(peer.getHash());
        Preconditions.checkNotNull(peer.getHost());
        Preconditions.checkNotNull(peer.getApi());

        // Serialize into JSON
        try {
            String json = getObjectMapper().writeValueAsString(peer);

            // Preparing indexBlocksFromNode
            UpdateRequestBuilder updateRequest = client.prepareUpdate(peer.getCurrency(), TYPE, peer.getId())
                    .setDoc(json);

            // Execute indexBlocksFromNode
            updateRequest
                    .setRefresh(true)
                    .execute();
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
        return peer;
    }

    @Override
    public Peer getById(String id) {
        throw new TechnicalException("not implemented");
    }

    @Override
    public void remove(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkArgument(StringUtils.isNotBlank(peer.getId()));
        Preconditions.checkArgument(StringUtils.isNotBlank(peer.getCurrency()));

        // Delete the document
        client.prepareDelete(peer.getCurrency(), TYPE, peer.getId()).execute().actionGet();
    }

    @Override
    public List<Peer> getPeersByCurrencyId(String currencyId) {
        throw new TechnicalException("no implemented: loading all peers may be unsafe for memory...");
    }

    @Override
    public List<Peer> getPeersByCurrencyIdAndApi(String currencyId, String endpointApi) {
        return getPeersByCurrencyIdAndApiAndPubkeys(currencyId, endpointApi, null);
    }

    @Override
    public List<Peer> getPeersByCurrencyIdAndApiAndPubkeys(String currencyId, String endpointApi, String[] pubkeys) {
        Preconditions.checkNotNull(currencyId);
        Preconditions.checkNotNull(endpointApi);

        SearchRequestBuilder request = client.prepareSearch(currencyId)
                .setTypes(TYPE)
                .setSize(1000);

        // Query = filter on lastUpTime
        NestedQueryBuilder statusQuery = QueryBuilders.nestedQuery(Peer.PROPERTY_STATS,
                QueryBuilders.boolQuery()
                        .filter(QueryBuilders.termQuery(Peer.PROPERTY_STATS + "." + Peer.Stats.PROPERTY_STATUS, Peer.PeerStatus.UP.name())));

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(Peer.PROPERTY_API, endpointApi));
        if (CollectionUtils.isNotEmpty(pubkeys)) {
            boolQuery.filter(QueryBuilders.termsQuery(Peer.PROPERTY_PUBKEY, pubkeys));
        }

        request.setQuery(QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery().must(boolQuery).must(statusQuery)));

        SearchResponse response = request.execute().actionGet();
        return toList(response, Peer.class);
    }

    @Override
    public boolean isExists(String currencyId, String peerId) {
        return client.isDocumentExists(currencyId, TYPE, peerId);
    }

    @Override
    public Long getMaxLastUpTime(String currencyName) {

        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // Get max(number)
        searchRequest.addAggregation(AggregationBuilders.nested(Peer.PROPERTY_STATS)
                .path(Peer.PROPERTY_STATS)
                .subAggregation(
                        AggregationBuilders.max(Peer.Stats.PROPERTY_LAST_UP_TIME)
                            .field(Peer.PROPERTY_STATS + "." + Peer.Stats.PROPERTY_LAST_UP_TIME)
                            .missing(0)
                ));

        // Execute query
        SearchResponse searchResponse = searchRequest.execute().actionGet();

        // Read query result
        SingleBucketAggregation stats = searchResponse.getAggregations().get(Peer.PROPERTY_STATS);
        if (stats == null) return null;

        Max result = stats.getAggregations().get(Peer.Stats.PROPERTY_LAST_UP_TIME);
        if (result == null) {
            return null;
        }

        return (result.getValue() == Double.NEGATIVE_INFINITY)
                ? null
                : (long)result.getValue();
    }

    @Override
    public void updatePeersAsDown(String currencyName, long upTimeLimit) {

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[%s] Setting peers as DOWN, if older than [%s]...", currencyName, new Date(upTimeLimit*1000)));
        }

        SearchRequestBuilder searchRequest = client.prepareSearch(currencyName)
                .setFetchSource(false)
                .setTypes(TYPE);

        // Query = filter on lastUpTime
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                // where lastUpTime < upTimeLimit
                .filter(QueryBuilders.rangeQuery(Peer.PROPERTY_STATS + "." + Peer.Stats.PROPERTY_LAST_UP_TIME).lte(upTimeLimit))
                // AND status = UP
                .filter(QueryBuilders.termQuery(Peer.PROPERTY_STATS + "." + Peer.Stats.PROPERTY_STATUS, Peer.PeerStatus.UP.name()));
        searchRequest.setQuery(QueryBuilders.nestedQuery(Peer.PROPERTY_STATS, QueryBuilders.constantScoreQuery(boolQuery)));

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        // Execute query, while there is some data
        try {

            int counter = 0;
            boolean loop = true;
            int bulkSize = pluginSettings.getIndexBulkSize();
            searchRequest.setSize(bulkSize);
            SearchResponse response = searchRequest.execute().actionGet();

            // Execute query, while there is some data
            do {

                // Read response
                SearchHit[] searchHits = response.getHits().getHits();
                for (SearchHit searchHit : searchHits) {

                    // Add deletion to bulk
                    bulkRequest.add(
                            client.prepareUpdate(currencyName, TYPE, searchHit.getId())
                            .setDoc(String.format("{\"%s\": {\"%s\": \"%s\"}}", Peer.PROPERTY_STATS, Peer.Stats.PROPERTY_STATUS, Peer.PeerStatus.DOWN.name()).getBytes())
                    );
                    counter++;

                    // Flush the bulk if not empty
                    if ((bulkRequest.numberOfActions() % bulkSize) == 0) {
                        client.flushBulk(bulkRequest);
                        bulkRequest = client.prepareBulk();
                    }
                }

                // Prepare next iteration
                if (counter == 0 || counter >= response.getHits().getTotalHits()) {
                    loop = false;
                }
                // Prepare next iteration
                else {
                    searchRequest.setFrom(counter);
                    response = searchRequest.execute().actionGet();
                }
            } while(loop);

            // last flush
            if ((bulkRequest.numberOfActions() % bulkSize) != 0) {
                client.flushBulk(bulkRequest);
            }

            if (counter > 0) {
                logger.info(String.format("Mark %s peers as DOWN", counter));
            }

        } catch (SearchPhaseExecutionException e) {
            // Failed or no item on index
            logger.error(String.format("Error while update peer status to DOWN: %s.", e.getMessage()), e);
        }


    }

    @Override
    public boolean hasPeersUpWithApi(String currencyId, Set<EndpointApi> api) {
        SearchRequestBuilder searchRequest = client.prepareSearch(currencyId)
                .setFetchSource(false)
                .setTypes(TYPE)
                .setSize(0);

        // Query = filter on lastUpTime
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        if (CollectionUtils.isNotEmpty(api)) {
            query.minimumNumberShouldMatch(api.size());
            api.forEach(a -> query.should(QueryBuilders.termQuery(Peer.PROPERTY_API, a.name())));
        }

        query.must(QueryBuilders.nestedQuery(Peer.PROPERTY_STATS, QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(Peer.PROPERTY_STATS + "." + Peer.Stats.PROPERTY_STATUS, Peer.PeerStatus.UP.name())))));

        searchRequest.setQuery(query);
        SearchResponse response = searchRequest.execute().actionGet();
        return response.getHits() != null && response.getHits().getTotalHits() > 0;
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // currency
                    .startObject(Peer.PROPERTY_CURRENCY)
                    .field("type", "string")
                    .endObject()

                    // pubkey
                    .startObject(Peer.PROPERTY_PUBKEY)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // api
                    .startObject(Peer.PROPERTY_API)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // dns
                    .startObject(Peer.PROPERTY_DNS)
                    .field("type", "string")
                    .endObject()

                    // ipv4
                    .startObject(Peer.PROPERTY_IPV4)
                    .field("type", "string")
                    .endObject()

                    // ipv6
                    .startObject(Peer.PROPERTY_IPV6)
                    .field("type", "string")
                    .endObject()

                    // epId
                    .startObject(Peer.PROPERTY_EP_ID)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // stats
                    .startObject(Peer.PROPERTY_STATS)
                    .field("type", "nested")
                    //.field("dynamic", "false")
                    .startObject("properties")

                        // stats.version
                        .startObject(Peer.Stats.PROPERTY_VERSION)
                        .field("type", "string")
                        .endObject()

                        // stats.status
                        .startObject(Peer.Stats.PROPERTY_STATUS)
                        .field("type", "string")
                        .field("index", "not_analyzed")
                        .endObject()

                        // stats.blockNumber
                        .startObject("blockNumber")
                        .field("type", "integer")
                        .endObject()

                        // stats.blockHash
                        .startObject("blockHash")
                        .field("type", "string")
                        .field("index", "not_analyzed")
                        .endObject()

                        // stats.error
                        .startObject("error")
                        .field("type", "string")
                        .endObject()

                        // stats.medianTime
                        .startObject("medianTime")
                        .field("type", "integer")
                        .endObject()

                        // stats.hardshipLevel
                        .startObject("hardshipLevel")
                        .field("type", "integer")
                        .endObject()

                        // stats.consensusPct
                        .startObject("consensusPct")
                        .field("type", "integer")
                        .endObject()

                        // stats.uid
                        .startObject(Peer.Stats.PROPERTY_UID)
                        .field("type", "string")
                        .endObject()

                        // stats.mainConsensus
                        .startObject("mainConsensus")
                        .field("type", "boolean")
                        .field("index", "not_analyzed")
                        .endObject()

                        // stats.forkConsensus
                        .startObject("forkConsensus")
                        .field("type", "boolean")
                        .field("index", "not_analyzed")
                        .endObject()

                        // stats.lastUP
                        .startObject(Peer.Stats.PROPERTY_LAST_UP_TIME)
                        .field("type", "integer")
                        .endObject()

                    .endObject()
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for peer index: " + ioe.getMessage(), ioe);
        }
    }
}
