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
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.ObjectUtils;
import org.duniter.elasticsearch.dao.AbstractIndexTypeDao;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.user.PluginSettings;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by blavenie on 03/04/17.
 */
public class UserSettingsDaoImpl extends AbstractIndexTypeDao<UserSettingsDaoImpl>
        implements UserSettingsDao<UserSettingsDaoImpl> {

    @Inject
    public UserSettingsDaoImpl() {
        super(UserIndexDao.INDEX, UserSettingsDao.TYPE);
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

        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(getType())
                    .startObject("properties")

                    // version
                    .startObject(Record.PROPERTY_VERSION)
                    .field("type", "integer")
                    .endObject()

                    // time
                    .startObject(Record.PROPERTY_TIME)
                    .field("type", "integer")
                    .endObject()

                    // issuer
                    .startObject(Record.PROPERTY_ISSUER)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // nonce
                    .startObject("nonce")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // content
                    .startObject("content")
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
}
