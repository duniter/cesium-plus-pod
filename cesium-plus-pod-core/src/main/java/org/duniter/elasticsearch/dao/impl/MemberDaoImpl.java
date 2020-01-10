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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.duniter.core.client.model.local.Identity;
import org.duniter.core.client.model.local.Member;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.elasticsearch.dao.AbstractDao;
import org.duniter.elasticsearch.dao.MemberDao;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by blavenie on 29/12/15.
 */
public class MemberDaoImpl extends AbstractDao implements MemberDao {

    public MemberDaoImpl(){
        super("duniter.dao.member");
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public List<Member> getMembers(String currencyId) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[%s] Getting WoT members...", currencyId));
        }

        int bulkSize = pluginSettings.getIndexBulkSize();
        List<Member> result = Lists.newArrayList();

        // Query = filter on isMember
        BoolQueryBuilder query = QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery(Identity.PROPERTY_IS_MEMBER, true));

        SearchRequestBuilder searchRequest = client.prepareSearch(currencyId)
                .setFetchSource(true)
                .setTypes(TYPE)
                .setSize(bulkSize)
                .setQuery(QueryBuilders.constantScoreQuery(query));

        // Execute query, while there is some data
        try {

            int counter = 0;
            long now = System.currentTimeMillis();
            boolean loop = true;
            SearchResponse response = searchRequest.execute().actionGet();

            // Execute query, while there is some data
            do {

                // Read response
                List<Member> hits = toList(response, Member.class);

                // Add to result
                if (CollectionUtils.size(hits) > 0) {
                    counter += hits.size();
                    result.addAll(hits);
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

            if (counter > 0 && logger.isDebugEnabled()) {
                logger.debug(String.format("[%s] Get %s WoT members in %s ms", currencyId, counter, (System.currentTimeMillis() - now)));
            }

        } catch (SearchPhaseExecutionException e) {
            // Failed or no item on index
            logger.error(String.format("Error while getting WoT members: %s.", e.getMessage()), e);
        }
        return result;
    }

    @Override
    public boolean isExists(String currencyId, String pubkey) {
        return client.isDocumentExists(currencyId, TYPE, pubkey);
    }

    @Override
    public Identity create(Identity identity) {
        Preconditions.checkNotNull(identity);
        Preconditions.checkNotNull(identity.getPubkey());
        Preconditions.checkNotNull(identity.getCurrency());

        // Serialize into JSON
        try {
            // Preparing indexBlocksFromNode
            IndexRequestBuilder request = client.prepareIndex(identity.getCurrency(), TYPE)
                    .setId(identity.getPubkey())
                    .setRefresh(true)
                    .setSource(getObjectMapper().writeValueAsBytes(identity));

            // Execute indexBlocksFromNode
            client.safeExecuteRequest(request, false);
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
        return identity;
    }

    @Override
    public Identity update(Identity identity) {
        Preconditions.checkNotNull(identity);
        Preconditions.checkNotNull(identity.getPubkey());
        Preconditions.checkNotNull(identity.getCurrency());

        // Serialize into JSON
        try {

            // Preparing indexBlocksFromNode
            UpdateRequestBuilder request = client.prepareUpdate(identity.getCurrency(), TYPE, identity.getPubkey())
                    .setRefresh(true)
                    .setDoc(getObjectMapper().writeValueAsBytes(identity));

            // Execute
            client.safeExecuteRequest(request, false);
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
        return identity;
    }

    @Override
    public Set<String> getMemberPubkeys(String currency) {

        QueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(Member.PROPERTY_IS_MEMBER, true));

        return executeAndGetIds(client.prepareSearch(currency)
                .setTypes(TYPE)
                .setQuery(QueryBuilders.constantScoreQuery(query))
                .setFetchSource(false));
    }

    @Override
    public void save(String currency, List<Member> members) {
        int bulkSize = pluginSettings.getIndexBulkSize();
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        ObjectMapper objectMapper = getObjectMapper();

        int counter = 0;
        for (Member m: members) {
            try {
                boolean exists = isExists(currency, m.getPubkey());

                // Add insert to bulk
                if (!exists) {
                    bulkRequest.add(
                            client.prepareIndex(currency, TYPE)
                                    .setId(m.getPubkey())
                                    .setSource(objectMapper.writeValueAsBytes(m))
                    );
                }
                // Add update to bulk
                else {
                    bulkRequest.add(
                            client.prepareUpdate(currency, TYPE, m.getPubkey())
                                    .setDoc(objectMapper.writeValueAsBytes(m))
                    );
                }
                counter++;
            } catch (Exception e) {
                throw new TechnicalException(e);
            }

            // Flush the bulk if not empty
            if ((counter % bulkSize) == 0) {
                client.flushBulk(bulkRequest);
                bulkRequest = client.prepareBulk();
            }
        }

        // Flush the bulk if not empty
        if (counter > 0 && (counter % bulkSize) != 0) {
            client.flushBulk(bulkRequest);
        }
    }

    @Override
    public void updateAsWasMember(String currency, Collection<String> wasMemberPubkeys) {
        int bulkSize = pluginSettings.getIndexBulkSize();
        BulkRequestBuilder bulkRequest = client.prepareBulk().setRefresh(true);

        int counter = 0;

        // Update old members (set wasMember to true)
        for (String pubkey: wasMemberPubkeys) {
            bulkRequest.add(
                    client.prepareUpdate(currency, TYPE, pubkey)
                            .setDoc(String.format("{\"%s\": false, \"%s\": true}", Member.PROPERTY_IS_MEMBER, Member.PROPERTY_WAS_MEMBER))
            );
            counter++;
            // Flush the bulk if not empty
            if ((counter % bulkSize) == 0) {
                client.flushBulk(bulkRequest);
                bulkRequest = client.prepareBulk();
            }
        }

        // Flush the bulk if not empty
        if (counter > 0 && (counter % bulkSize) != 0) {
            client.flushBulk(bulkRequest);
        }
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // uid
                    .startObject(Member.PROPERTY_UID)
                    .field("type", "string")
                    .endObject()

                    // pubkey
                    .startObject(Member.PROPERTY_PUBKEY)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // isMember
                    .startObject(Member.PROPERTY_IS_MEMBER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // wasMember
                    .startObject(Member.PROPERTY_WAS_MEMBER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    .endObject()
                    .endObject()
                    .endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for peer index: " + ioe.getMessage(), ioe);
        }
    }
}
