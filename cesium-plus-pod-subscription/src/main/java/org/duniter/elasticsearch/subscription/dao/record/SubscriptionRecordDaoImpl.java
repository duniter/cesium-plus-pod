package org.duniter.elasticsearch.subscription.dao.record;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Subscription plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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

import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.CollectionUtils;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.duniter.elasticsearch.subscription.dao.AbstractSubscriptionIndexTypeDao;
import org.duniter.elasticsearch.subscription.dao.SubscriptionIndexDao;
import org.duniter.elasticsearch.subscription.model.SubscriptionRecord;
import org.duniter.elasticsearch.subscription.model.email.EmailSubscription;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by blavenie on 03/04/17.
 */
public class SubscriptionRecordDaoImpl extends AbstractSubscriptionIndexTypeDao<SubscriptionRecordDaoImpl> implements SubscriptionRecordDao<SubscriptionRecordDaoImpl> {

    @Inject
    public SubscriptionRecordDaoImpl(PluginSettings pluginSettings, SubscriptionIndexDao indexDao) {
        super(SubscriptionIndexDao.INDEX, TYPE, pluginSettings);

        indexDao.register(this);
    }

    @Override
    public List<SubscriptionRecord> getSubscriptions(int from, int size, String recipient, String... types) {

        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(SubscriptionRecord.PROPERTY_RECIPIENT, recipient));
        if (CollectionUtils.isNotEmpty(types)) {
            query.must(QueryBuilders.termsQuery(SubscriptionRecord.PROPERTY_TYPE, types));
        }

        SearchResponse response = client.prepareSearch(SubscriptionIndexDao.INDEX)
                .setTypes(SubscriptionRecordDao.TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .setFetchSource(true)
                .setFrom(from).setSize(size)
                .get();

        return Arrays.asList(response.getHits().getHits()).stream()
                .map(this::toSubscription)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                    .startObject(getType())
                    .startObject("properties")

                    // version
                    .startObject(SubscriptionRecord.PROPERTY_VERSION)
                    .field("type", "integer")
                    .endObject()

                    // type
                    .startObject(SubscriptionRecord.PROPERTY_TYPE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // issuer
                    .startObject(SubscriptionRecord.PROPERTY_ISSUER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // recipient
                    .startObject(SubscriptionRecord.PROPERTY_RECIPIENT)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // time
                    .startObject(SubscriptionRecord.PROPERTY_TIME)
                    .field("type", "integer")
                    .endObject()

                    // nonce
                    .startObject(SubscriptionRecord.PROPERTY_NONCE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // issuerContent
                    .startObject(SubscriptionRecord.PROPERTY_ISSUER_CONTENT)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // receiver content
                    .startObject(SubscriptionRecord.PROPERTY_RECIPIENT_CONTENT)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // hash
                    .startObject(SubscriptionRecord.PROPERTY_HASH)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // signature
                    .startObject(SubscriptionRecord.PROPERTY_SIGNATURE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", getIndex(), getType(), ioe.getMessage()), ioe);
        }
    }

    protected SubscriptionRecord toSubscription(SearchHit searchHit) {

        SubscriptionRecord record = null;

        if (SubscriptionRecordDao.TYPE.equals(searchHit.getType())) {
            record = client.readSourceOrNull(searchHit, EmailSubscription.class);
        }

        if (record != null) {
            record.setId(searchHit.getId());
        }

        return record;
    }

}
