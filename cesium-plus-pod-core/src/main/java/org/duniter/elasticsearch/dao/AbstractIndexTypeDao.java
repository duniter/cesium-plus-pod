package org.duniter.elasticsearch.dao;

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
import org.duniter.elasticsearch.dao.handler.StringReaderHandler;
import org.elasticsearch.action.bulk.BulkRequestBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Benoit on 08/04/2015.
 */
public abstract class AbstractIndexTypeDao<T extends IndexTypeDao> extends AbstractDao implements IndexTypeDao<T> {

    private final String index;
    private final String type;

    public AbstractIndexTypeDao(String index, String type) {
        super("duniter.dao."+index);
        this.index = index;
        this.type = type;
    }

    /**
     * Create index
     * @throws JsonProcessingException
     */
    protected abstract void createIndex() throws JsonProcessingException;

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public T createIndexIfNotExists() {
        try {
            if (!client.existsIndex(index)) {
                createIndex();
            }
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(String.format("Error while creating index [%s]", index));
        }
        return (T)this;
    }

    @Override
    public T deleteIndex() {
        client.deleteIndexIfExists(index);
        return (T)this;
    }

    @Override
    public boolean isExists(String docId) {
        return client.isDocumentExists(index, type, docId);
    }

    public String create(final String json) {
        return client.indexDocumentFromJson(index, type, json);
    }

    public void update(final String id, final String json) {
        client.updateDocumentFromJson(index, type, id, json);
    }

    public String indexDocumentFromJson(String json) {
        return client.indexDocumentFromJson(index, type, json);
    }

    public void updateDocumentFromJson(String id, String json) {
        client.updateDocumentFromJson(index, type, id, json);
    }

    /**
     * Retrieve a field from a document id
     * @param docId
     * @return
     */
    public Object getFieldById(String docId, String fieldName) {
        return client.getFieldById(index, type, docId, fieldName);
    }

    public <T> T getTypedFieldById(String docId, String fieldName) {
        return client.getTypedFieldById(index, type, docId, fieldName);
    }

    @Override
    public Map<String, Object> getMandatoryFieldsById(String docId, String... fieldNames) {
        return client.getMandatoryFieldsById(index, type, docId, fieldNames);
    }

    @Override
    public Map<String, Object> getFieldsById(String docId, String... fieldNames) {
        return client.getFieldsById(index, type, docId, fieldNames);
    }

    /**
     * Retrieve a document by id (safe mode)
     * @param docId
     * @return
     */
    public <T extends Object> T getSourceByIdOrNull(String docId, Class<T> classOfT, String... fieldNames) {
        return client.getSourceByIdOrNull(index, type, docId, classOfT, fieldNames);
    }

    /**
     * Retrieve a document by id
     * @param docId
     * @return
     */
    public <T extends Object> T getSourceById(String docId, Class<T> classOfT, String... fieldNames) {
        return client.getSourceById(index, type, docId, classOfT, fieldNames);
    }

    public void bulkFromClasspathFile(String classpathFile) {
        client.bulkFromClasspathFile(classpathFile, index, type, null);
    }

    public void bulkFromClasspathFile(String classpathFile, StringReaderHandler handler) {
        client.bulkFromClasspathFile(classpathFile, index, type, handler);
    }

    public void bulkFromFile(File file) {
        client.bulkFromFile(file, index, type, null);
    }

    public void bulkFromFile(File file, StringReaderHandler handler) {
        client.bulkFromFile(file, index, type, handler);
    }

    public void bulkFromStream(InputStream is) {
        client.bulkFromStream(is, index, type, null);
    }

    public void bulkFromStream(InputStream is, StringReaderHandler handler) {
        client.bulkFromStream(is, index, type, handler);
    }

    public void flushDeleteBulk(BulkRequestBuilder bulkRequest) {
        client.flushDeleteBulk(index, type, bulkRequest);
    }

    @Override
    public boolean existsIndex() {
        return client.existsIndex(index);
    }

    public void create(String json, boolean wait) {
        Preconditions.checkNotNull(json);

        // Execute
        client.safeExecuteRequest(client.prepareIndex(getIndex(), getType())
                .setRefresh(false) // let's see if this works
                .setSource(json), wait);
    }

    public void update(String id, String json, boolean wait) {
        Preconditions.checkNotNull(json);

        // Execute
        client.safeExecuteRequest(client.prepareUpdate(getIndex(), getType(), id)
                .setRefresh(false) // let's see if this works
                .setDoc(json), wait);
    }
}
