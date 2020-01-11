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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.duniter.core.client.model.bma.WotPendingMembership;
import org.duniter.core.client.model.local.Identity;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.CollectionUtils;
import org.duniter.elasticsearch.dao.AbstractDao;
import org.duniter.elasticsearch.dao.PendingMembershipDao;
import org.duniter.elasticsearch.dao.SaveResult;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;

/**
 * Created by blavenie on 29/12/15.
 */
public class PendingMembershipDaoImpl extends AbstractDao implements PendingMembershipDao {

    public PendingMembershipDaoImpl(){
        super("duniter.dao.pending");
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public List<WotPendingMembership> getPendingMemberships(String currencyId, int minFrom, int maxSize) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[%s] Getting WoT pending memberships...", currencyId));
        }
        List<WotPendingMembership> result = Lists.newArrayList();

        int size = Math.min(pluginSettings.getIndexBulkSize(), maxSize);
        int from = minFrom;
        long now = System.currentTimeMillis();
        long total = -1;

        // Query = filter block ?
        BoolQueryBuilder query = QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery(WotPendingMembership.PROPERTY_CURRENCY,  currencyId));

        SearchRequestBuilder searchRequest = client.prepareSearch(currencyId)
                .setFetchSource(true)
                .setTypes(TYPE)
                .setSize(size)
                .setQuery(QueryBuilders.constantScoreQuery(query))
                .addSort(WotPendingMembership.PROPERTY_BLOCK_NUMBER, SortOrder.DESC);

        // Execute query, while there is some data
        try {

            // Execute query, while there is some data
            do {
                SearchResponse response = searchRequest
                        .setFrom(from)
                        .execute().actionGet();

                // Read response
                List<WotPendingMembership> hits = toList(response, WotPendingMembership.class);

                // Add to result
                if (CollectionUtils.isNotEmpty(hits)) result.addAll(hits);

                // Prepare next iteration
                from += size;
                if (total == -1) total = Math.min(maxSize, response.getHits().getTotalHits());
            } while(from < total);

            if (logger.isDebugEnabled() && total > 0) {
                logger.debug(String.format("[%s] Get %s WoT pending memberships in %s ms", currencyId, (total - from), (System.currentTimeMillis() - now)));
            }

        } catch (SearchPhaseExecutionException e) {
            // Failed or no item on index
            logger.error(String.format("Error while getting WoT pending memberships: %s.", e.getMessage()), e);
        }
        return result;
    }


    @Override
    public SaveResult save(String currencyId, List<WotPendingMembership> memberships) {

        long now = System.currentTimeMillis();
        if (logger.isDebugEnabled())
            logger.debug(String.format("[%s] Saving %s pending memberships...", currencyId, CollectionUtils.size(memberships)));

        int bulkSize = pluginSettings.getIndexBulkSize();
        int insertions = 0;
        int updates = 0;
        ObjectMapper objectMapper = getObjectMapper();

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (WotPendingMembership m: memberships) {
            try {
                String docId = computeDocId(m);
                boolean exists = isExists(currencyId, docId);

                // Add insert to bulk
                if (!exists) {
                    bulkRequest.add(
                            client.prepareIndex(currencyId, TYPE)
                                    .setId(docId)
                                    .setSource(objectMapper.writeValueAsBytes(m))
                    );
                    insertions++;
                }
                // Add update to bulk
                else {
                    bulkRequest.add(
                            client.prepareUpdate(currencyId, TYPE, docId)
                                    .setDoc(objectMapper.writeValueAsBytes(m))
                    );
                    updates++;
                }

                // Flush the bulk if not empty
                if (bulkRequest.numberOfActions() % bulkSize == 0) {
                    client.flushBulk(bulkRequest);
                    bulkRequest = client.prepareBulk();
                }
            } catch (Exception e) {
                throw new TechnicalException(e);
            }
        }

        // Final flush
        client.flushBulk(bulkRequest);

        SaveResult result = new SaveResult();
        result.addInserts(currencyId, TYPE, insertions);
        result.addUpdates(currencyId, TYPE, updates);

        if (logger.isDebugEnabled())
            logger.debug(String.format("[%s] Pending memberships saved in %s ms - %s", currencyId, CollectionUtils.size(memberships), System.currentTimeMillis() - now, result.toString()));

        return result;
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // pubkey
                    .startObject(WotPendingMembership.PROPERTY_PUBKEY)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // uid
                    .startObject(WotPendingMembership.PROPERTY_UID)
                    .field("type", "string")
                    .endObject()

                    // version
                    .startObject(WotPendingMembership.PROPERTY_VERSION)
                    .field("type", "integer")
                    .endObject()

                    // currency
                    .startObject(WotPendingMembership.PROPERTY_CURRENCY)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // membership
                    .startObject(WotPendingMembership.PROPERTY_MEMBERSHIP)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // blockNumber
                    .startObject(WotPendingMembership.PROPERTY_BLOCK_NUMBER)
                    .field("type", "integer")
                    .endObject()

                    // blockHash
                    .startObject(WotPendingMembership.PROPERTY_BLOCK_HASH)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // isMember
                    .startObject(WotPendingMembership.PROPERTY_WRITTEN)
                    .field("type", "boolean")
                    .endObject()

                    .endObject()
                    .endObject()
                    .endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for pending membership index: " + ioe.getMessage(), ioe);
        }
    }

    /* -- -- */

    protected String computeDocId(WotPendingMembership membership) {
        return cryptoService.hash(computeUniqueKey(membership));
    }

    protected String computeUniqueKey(WotPendingMembership membership) {
        return Joiner.on('-').skipNulls().join(membership.getPubkey(), membership.getUid(), membership.getBlockNumber(), membership.getBlockHash());
    }

    protected boolean isExists(String currencyId, String id) {
        return client.isDocumentExists(currencyId, TYPE, id);
    }

}
