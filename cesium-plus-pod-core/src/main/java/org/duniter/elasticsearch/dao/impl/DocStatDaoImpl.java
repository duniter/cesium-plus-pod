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
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.dao.*;
import org.duniter.elasticsearch.model.DocStat;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by Benoit on 30/03/2015.
 */
public class DocStatDaoImpl extends AbstractIndexTypeDao<DocStatDao> implements DocStatDao {

    private PluginSettings pluginSettings;

    @Inject
    public DocStatDaoImpl(PluginSettings pluginSettings) {
        super(DocStatDao.INDEX, DocStatDao.TYPE);
        this.pluginSettings = pluginSettings;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public long countDoc(String index, String type) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));

        SearchRequestBuilder searchRequest = client.prepareSearch(index)
                .setFetchSource(false)
                .setSize(0);

        // Set type if present
        if (StringUtils.isNotBlank(type)) {
            searchRequest.setTypes(type);
        }

        SearchResponse response = searchRequest.execute().actionGet();
        return response.getHits().getTotalHits();
    }

    @Override
    public IndexRequestBuilder prepareIndex(DocStat stat) {
        Preconditions.checkNotNull(stat);
        Preconditions.checkArgument(StringUtils.isNotBlank(stat.getIndex()));

        // Make sure time has been set
        if (stat.getTime() == 0) {
            stat.setTime(System.currentTimeMillis()/1000);
        }

        try {
            return client.prepareIndex(INDEX, TYPE)
                    .setRefresh(false)
                    .setSource(getObjectMapper().writeValueAsBytes(stat));
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(e);
        }
    }

    @Override
    protected void createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s]", INDEX));

        client.admin().indices().prepareCreate(INDEX)
            .setSettings(Settings.settingsBuilder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1)
                .build())
            .addMapping(TYPE, createTypeMapping())
            .execute().actionGet();
    }

    @Override
    public XContentBuilder createTypeMapping() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(TYPE)
                    .startObject("properties")

                    // index
                    .startObject(DocStat.PROPERTY_INDEX)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // indexType
                    .startObject(DocStat.PROPERTY_INDEX_TYPE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // type
                    .startObject(DocStat.PROPERTY_COUNT)
                    .field("type", "long")
                    .endObject()

                    // time
                    .startObject(DocStat.PROPERTY_TIME)
                    .field("type", "integer")
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for doc stat index: " + ioe.getMessage(), ioe);
        }
    }

    /* -- protected method -- */

    protected long countData(String index, String type) {

        SearchRequestBuilder searchRequest = client.prepareSearch(index)
                .setTypes(type)
                .setFetchSource(false)
                .setSize(0);

        SearchResponse response = searchRequest.execute().actionGet();
        return response.getHits().getTotalHits();
    }
}
