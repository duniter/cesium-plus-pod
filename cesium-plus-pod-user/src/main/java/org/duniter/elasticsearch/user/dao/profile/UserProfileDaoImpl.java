package org.duniter.elasticsearch.user.dao.profile;

/*
 * #%L
 * Ğchange Pod :: ElasticSearch plugin
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.elasticsearch.Records;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.elasticsearch.dao.AbstractIndexTypeDao;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by blavenie on 03/04/17.
 */
public class UserProfileDaoImpl extends AbstractIndexTypeDao<UserProfileDaoImpl> implements UserProfileDao<UserProfileDaoImpl> {

    private PluginSettings pluginSettings;

    @Inject
    public UserProfileDaoImpl(PluginSettings pluginSettings) {
        super(UserIndexDao.INDEX, UserProfileDao.TYPE);
        this.pluginSettings = pluginSettings;
    }

    @Override
    protected void createIndex() throws JsonProcessingException {
        throw new TechnicalException("not implemented");
    }

    @Override
    public void checkSameDocumentIssuer(String id, String expectedIssuer) {
        String issuer = getMandatoryFieldsById(id, Record.PROPERTY_ISSUER).get(Record.PROPERTY_ISSUER).toString();
        if (!ObjectUtils.equals(expectedIssuer, issuer)) {
            throw new TechnicalException("Not same issuer");
        }
    }

    @Override
    public String create(final String json) {
        try {
            JsonNode actualObj = getObjectMapper().readTree(json);
            String issuer = actualObj.get(Record.PROPERTY_ISSUER).asText();

            return create(issuer, json);
        }
        catch(IOException e) {
            throw new InvalidFormatException("Invalid record JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public String create(final String issuer, final String json) {

        IndexResponse response = client.prepareIndex(getIndex(), getType())
                .setSource(json)
                .setId(issuer) // always use the issuer pubkey as id
                .setRefresh(false)
                .execute().actionGet();
        return response.getId();
    }

    @Override
    public XContentBuilder createTypeMapping() {
        String stringAnalyzer = pluginSettings.getDefaultStringAnalyzer();

        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(getType())
                    .startObject("properties")

                    // version
                    .startObject(UserProfile.PROPERTY_VERSION)
                    .field("type", "integer")
                    .endObject()

                    // title
                    .startObject(UserProfile.PROPERTY_TITLE)
                    .field("type", "string")
                    .field("analyzer", stringAnalyzer)
                    .endObject()

                    // description
                    .startObject(UserProfile.PROPERTY_DESCRIPTION)
                    .field("type", "string")
                    .field("analyzer", stringAnalyzer)
                    .endObject()

                    // time
                    .startObject(UserProfile.PROPERTY_TIME)
                    .field("type", "integer")
                    .endObject()

                    // issuer
                    .startObject(UserProfile.PROPERTY_ISSUER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // city
                    .startObject(UserProfile.PROPERTY_CITY)
                    .field("type", "string")
                    .endObject()

                    // address
                    .startObject(UserProfile.PROPERTY_ADDRESS)
                    .field("type", "string")
                    .endObject()

                    // geoPoint
                    .startObject(Records.PROPERTY_GEO_POINT)
                    .field("type", "geo_point")
                    .endObject()

                    // avatar
                    .startObject(Records.PROPERTY_AVATAR)
                    .field("type", "attachment")
                    .startObject("fields") // fields
                    .startObject("content") // content
                    .field("index", "no")
                    .endObject()
                    .startObject("title") // title
                    .field("type", "string")
                    .field("store", "no")
                    .endObject()
                    .startObject("author") // author
                    .field("store", "no")
                    .endObject()
                    .startObject("content_type") // content_type
                    .field("store", "yes")
                    .endObject()
                    .endObject()
                    .endObject()

                    // social networks
                    .startObject(Records.PROPERTY_SOCIALS)
                    .field("type", "nested")
                    .field("dynamic", "false")
                    .startObject("properties")
                    .startObject("type") // type
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("url") // url
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()
                    .endObject()
                    .endObject()

                    // tags
                    .startObject(Records.PROPERTY_TAGS)
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
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", getIndex(), getType(), ioe.getMessage()), ioe);
        }
    }
}
