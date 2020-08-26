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
import com.google.common.collect.Lists;
import org.duniter.core.client.model.local.Currency;
import org.duniter.core.client.util.KnownCurrencies;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.AbstractIndexTypeDao;
import org.duniter.elasticsearch.dao.CurrencyExtendDao;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by blavenie on 29/12/15.
 */
public class CurrencyDaoImpl extends AbstractIndexTypeDao<CurrencyExtendDao> implements CurrencyExtendDao {

    protected static final String REGEX_WORD_SEPARATOR = "[-\\t@# _]+";

    private static String defaultId;

    public CurrencyDaoImpl(){
        super(INDEX, RECORD_TYPE);
    }

    @Override
    public Currency create(final Currency currency) {

        try {

            if (currency instanceof org.duniter.core.client.model.elasticsearch.Currency) {
                fillTags((org.duniter.core.client.model.elasticsearch.Currency)currency);
            }

            // Preparing indexBlocksFromNode
            IndexRequestBuilder request = client.prepareIndex(INDEX, RECORD_TYPE)
                    .setId(currency.getId())
                    .setRefresh(true)
                    .setSource(getObjectMapper().writeValueAsBytes(currency));

            // Execute indexBlocksFromNode
            client.safeExecuteRequest(request, true);

        } catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }

        return currency;
    }

    @Override
    public Currency update(final Currency currency) {
        try {

            if (currency instanceof org.duniter.core.client.model.elasticsearch.Currency) {
                fillTags((org.duniter.core.client.model.elasticsearch.Currency)currency);
            }

            // Serialize into JSON
            byte[] json = getObjectMapper().writeValueAsBytes(currency);

            UpdateRequestBuilder updateRequest = client.prepareUpdate(INDEX, RECORD_TYPE, currency.getId())
                    .setDoc(json);

            // Execute indexBlocksFromNode
            updateRequest
                    .setRefresh(true)
                    .execute();

        } catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }


        return currency;
    }

    @Override
    public void updateMemberCount(final String currency, int memberCount) {
        client.prepareUpdate(INDEX, RECORD_TYPE, currency)
                .setDoc(String.format("{\"%s\": %s}", Currency.PROPERTY_MEMBER_COUNT,
                        memberCount
                ).getBytes()).execute().actionGet();
    }

    @Override
    public void updateLastUD(final String currency, long lastUD) {
        client.prepareUpdate(INDEX, RECORD_TYPE, currency)
                .setDoc(String.format("{\"%s\": %s}", Currency.PROPERTY_LAST_UD,
                        lastUD
                ).getBytes()).execute().actionGet();
    }

    @Override
    public void remove(final Currency currency) {
        Preconditions.checkNotNull(currency);
        Preconditions.checkArgument(StringUtils.isNotBlank(currency.getId()));

        // Delete the document
        client.prepareDelete(INDEX, RECORD_TYPE, currency.getId()).execute().actionGet();
    }

    @Override
    public Currency getById(String currencyId) {
        org.duniter.core.client.model.elasticsearch.Currency result = client.getSourceById(INDEX, RECORD_TYPE, currencyId, org.duniter.core.client.model.elasticsearch.Currency.class);
        result.setId(currencyId);
        return result;
    }

    @Override
    public List<Currency> getAllByAccount(long accountId) {
        throw new TechnicalException("Not implemented yet");
    }

    @Override
    public List<Currency> getAll() {
        SearchRequestBuilder request = client.prepareSearch(INDEX)
                .setTypes(RECORD_TYPE)
                .setSize(pluginSettings.getIndexBulkSize())
                .setFetchSource(true);
        return toList(request.execute().actionGet(), Currency.class);
    }

    @Override
    public Set<String> getAllIds() {
        SearchRequestBuilder request = client.prepareSearch(INDEX)
                .setTypes(RECORD_TYPE)
                .setSize(pluginSettings.getIndexBulkSize())
                .setFetchSource(false);

        return executeAndGetIds(request.execute().actionGet());
    }

    @Override
    public long getLastUD(String currencyId) {
        Currency currency = getById(currencyId);
        if (currency == null) {
            return -1;
        }
        return currency.getLastUD();
    }

    @Override
    public Map<Integer, Long> getAllUD(String currencyId) {

        throw new TechnicalException("Not implemented yet");
    }

    @Override
    public void insertUDs(String currencyId,  Map<Integer, Long> newUDs) {
        throw new TechnicalException("Not implemented yet");
    }

    public boolean existsIndex() {
        return client.existsIndex(INDEX);
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                    .startObject(RECORD_TYPE)
                    .startObject("properties")

                    // firstBlockSignature
                    .startObject(Currency.PROPERTY_FIRST_BLOCK_SIGNATURE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // member count
                    .startObject(Currency.PROPERTY_MEMBER_COUNT)
                    .field("type", "integer")
                    .endObject()

                    // lastUD
                    .startObject(Currency.PROPERTY_LAST_UD)
                    .field("type", "long")
                    .endObject()

                    // unitbase
                    .startObject(Currency.PROPERTY_UNITBASE)
                    .field("type", "integer")
                    .endObject()

                    // tags
                    .startObject("tags")
                    .field("type", "completion")
                    .field("search_analyzer", "simple")
                    .field("analyzer", "simple")
                    .field("preserve_separators", "false")

                    .endObject()
                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", INDEX, RECORD_TYPE, ioe.getMessage()), ioe);
        }
    }

    /**
     * Return the default currency
     * @return
     */
    public String getDefaultId() {

        if (defaultId != null) return defaultId;

        boolean enableBlockchainIndexation = pluginSettings.enableBlockchainIndexation() && existsIndex();
        try {
            Set<String> ids = enableBlockchainIndexation ? getAllIds() : null;
            if (CollectionUtils.isNotEmpty(ids)) {
                defaultId = ids.iterator().next();
                return defaultId;
            }
        } catch(Throwable t) {
            // Continue (index not read yet?)
        }
        return KnownCurrencies.G1;
    }

    /* -- internal methods -- */

    @Override
    protected void createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s]", INDEX));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
        org.elasticsearch.common.settings.Settings indexSettings = org.elasticsearch.common.settings.Settings.settingsBuilder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1)
                .put("analyzer", pluginSettings.getDefaultStringAnalyzer())
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(RECORD_TYPE, createTypeMapping());
        createIndexRequestBuilder.execute().actionGet();
    }

    protected void fillTags(org.duniter.core.client.model.elasticsearch.Currency currency) {
        String currencyName = currency.getId();
        String[] tags = currencyName.split(REGEX_WORD_SEPARATOR);
        List<String> tagsList = Lists.newArrayList(tags);

        // Convert as a sentence (replace separator with a space)
        String sentence = currencyName.replaceAll(REGEX_WORD_SEPARATOR, " ");
        if (!tagsList.contains(sentence)) {
            tagsList.add(sentence);
        }

        currency.setTags(tagsList.toArray(new String[tagsList.size()]));
    }

}
