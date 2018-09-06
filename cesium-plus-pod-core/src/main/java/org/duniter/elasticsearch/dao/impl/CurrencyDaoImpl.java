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
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.AbstractIndexTypeDao;
import org.duniter.elasticsearch.dao.CurrencyExtendDao;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by blavenie on 29/12/15.
 */
public class CurrencyDaoImpl extends AbstractIndexTypeDao<CurrencyExtendDao> implements CurrencyExtendDao {

    protected static final String REGEX_WORD_SEPARATOR = "[-\\t@# _]+";

    public CurrencyDaoImpl(){
        super(INDEX, RECORD_TYPE);
    }

    @Override
    public org.duniter.core.client.model.local.Currency create(final org.duniter.core.client.model.local.Currency currency) {

        try {

            if (currency instanceof org.duniter.core.client.model.elasticsearch.Currency) {
                fillTags((org.duniter.core.client.model.elasticsearch.Currency)currency);
            }

            // Serialize into JSON
            byte[] json = getObjectMapper().writeValueAsBytes(currency);

            // Preparing indexBlocksFromNode
            IndexRequestBuilder indexRequest = client.prepareIndex(INDEX, RECORD_TYPE)
                    .setId(currency.getId())
                    .setSource(json);

            // Execute indexBlocksFromNode
            indexRequest
                    .setRefresh(true)
                    .execute().actionGet();

        } catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }

        return currency;
    }

    @Override
    public org.duniter.core.client.model.local.Currency update(final org.duniter.core.client.model.local.Currency currency) {
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
    public void remove(final org.duniter.core.client.model.local.Currency currency) {
        Preconditions.checkNotNull(currency);
        Preconditions.checkArgument(StringUtils.isNotBlank(currency.getId()));

        // Delete the document
        client.prepareDelete(INDEX, RECORD_TYPE, currency.getId()).execute().actionGet();
    }

    @Override
    public org.duniter.core.client.model.local.Currency getById(String currencyId) {
        return client.getSourceByIdOrNull(INDEX, RECORD_TYPE, currencyId, org.duniter.core.client.model.elasticsearch.Currency.class);
    }

    @Override
    public List<Currency> getCurrencies(long accountId) {
        throw new TechnicalException("Not implemented yet");
    }

    @Override
    public List<String> getCurrencyIds() {
        SearchRequestBuilder request = client.prepareSearch(INDEX)
                .setTypes(RECORD_TYPE)
                .setSize(pluginSettings.getIndexBulkSize())
                .setFetchSource(false);

        return toListIds(request.execute().actionGet());
    }

    @Override
    public long getLastUD(String currencyId) {
        org.duniter.core.client.model.local.Currency currency = getById(currencyId);
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

                    // currency
                    .startObject("currency")
                    .field("type", "string")
                    .endObject()

                    // firstBlockSignature
                    .startObject("firstBlockSignature")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // member count
                    .startObject("membersCount")
                    .field("type", "long")
                    .endObject()

                    // lastUD
                    .startObject("lastUD")
                    .field("type", "long")
                    .endObject()

                    // unitbase
                    .startObject("unitbase")
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

    /* -- internal methods -- */

    @Override
    protected void createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s]", INDEX));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
        org.elasticsearch.common.settings.Settings indexSettings = org.elasticsearch.common.settings.Settings.settingsBuilder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1)
                //.put("analyzer", createDefaultAnalyzer())
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(RECORD_TYPE, createTypeMapping());
        createIndexRequestBuilder.execute().actionGet();
    }

    protected void fillTags(org.duniter.core.client.model.elasticsearch.Currency currency) {
        String currencyName = currency.getCurrencyName();
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
