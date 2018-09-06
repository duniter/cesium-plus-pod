package org.duniter.elasticsearch.user.dao;

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
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.elasticsearch.Records;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.ObjectUtils;
import org.duniter.elasticsearch.dao.AbstractIndexTypeDao;
import org.duniter.elasticsearch.user.PluginSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by Benoit on 30/03/2015.
 */
public class AbstractRecordDaoImpl<T extends AbstractRecordDaoImpl> extends AbstractIndexTypeDao<T> implements RecordDao<T> {

    protected PluginSettings pluginSettings;

    private boolean isPubkeyFieldEnable = false;
    private boolean isNestedPicturesEnable = false;
    private boolean isNestedCategoryEnable = false;

    public AbstractRecordDaoImpl(String index, PluginSettings pluginSettings) {
        this(index, RecordDao.TYPE, pluginSettings);
    }

    public AbstractRecordDaoImpl(String index, String type, PluginSettings pluginSettings) {
        super(index, type);
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

    public XContentBuilder createTypeMapping() {
        String stringAnalyzer = pluginSettings.getDefaultStringAnalyzer();

        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(getType())
                    .startObject("properties")

                    // version
                    .startObject(Record.PROPERTY_VERSION)
                    .field("type", "integer")
                    .endObject()

                    // title
                    .startObject(Records.PROPERTY_TITLE)
                    .field("type", "string")
                    .field("analyzer", stringAnalyzer)
                    .endObject()

                    // description
                    .startObject(Records.PROPERTY_DESCRIPTION)
                    .field("type", "string")
                    .field("analyzer", stringAnalyzer)
                    .endObject()

                    // creationTime
                    .startObject(Records.PROPERTY_CREATION_TIME)
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

                    // hash
                    .startObject(Record.PROPERTY_HASH)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // signature
                    .startObject(Record.PROPERTY_SIGNATURE)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // address
                    .startObject(Records.PROPERTY_ADDRESS)
                    .field("type", "string")
                    .field("analyzer", stringAnalyzer)
                    .endObject()

                    // city
                    .startObject(Records.PROPERTY_CITY)
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
                    .endObject();

            // pubkey
            if (isPubkeyFieldEnable) {
                mapping.startObject("pubkey")
                        .field("type", "string")
                        .field("index", "not_analyzed")
                        .endObject();
            }

            // pictures
            if (isNestedPicturesEnable) {
                mapping.startObject(Records.PROPERTY_PICTURES)
                        .field("type", "nested")
                        .field("dynamic", "false")
                        .startObject("properties")
                        .startObject("file") // file
                        .field("type", "attachment")
                        .startObject("fields")
                        .startObject("content") // content
                        .field("index", "no")
                        .endObject()
                        .startObject("title") // title
                        .field("type", "string")
                        .field("store", "yes")
                        .field("analyzer", stringAnalyzer)
                        .endObject()
                        .startObject("author") // author
                        .field("type", "string")
                        .field("store", "no")
                        .endObject()
                        .startObject("content_type") // content_type
                        .field("store", "yes")
                        .endObject()
                        .endObject()
                        .endObject()
                        .endObject()
                        .endObject()

                        // picturesCount
                        .startObject(Records.PROPERTY_PICTURES_COUNT)
                        .field("type", "integer")
                        .endObject();
            }

            // category
            if (isNestedCategoryEnable) {
                mapping.startObject(Records.PROPERTY_CATEGORY)
                        .field("type", "nested")
                        .field("dynamic", "false")
                        .startObject("properties")
                        .startObject("id") // id
                        .field("type", "string")
                        .field("index", "not_analyzed")
                        .endObject()
                        .startObject("parent") // parent
                        .field("type", "string")
                        .field("index", "not_analyzed")
                        .endObject()
                        .startObject("name") // name
                        .field("type", "string")
                        .field("analyzer", stringAnalyzer)
                        .endObject()
                        .endObject()
                        .endObject();
            }

            mapping.endObject()
                .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", getIndex(), getType(), ioe.getMessage()), ioe);
        }
    }

    /* -- protected methods -- */

    protected void setNestedPicturesEnable(boolean isPicturesEnable) {
        this.isNestedPicturesEnable = isPicturesEnable;
    }

    protected void setNestedCategoryEnable(boolean isNestedCategoryEnable) {
        this.isNestedCategoryEnable = isNestedCategoryEnable;
    }

    protected void setPubkeyFieldEnable(boolean isPubkeyFieldEnable) {
        this.isPubkeyFieldEnable = isPubkeyFieldEnable;
    }
}
