package org.duniter.elasticsearch.client;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Core plugin
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

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.local.LocalEntity;
import org.duniter.elasticsearch.dao.handler.StringReaderHandler;
import org.duniter.elasticsearch.threadpool.CompletableActionFuture;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by blavenie on 03/04/17.
 */
public interface Duniter4jClient extends Bean, Client {

    boolean existsIndex(String index);

    void deleteIndexIfExists(String indexName);

    Object getFieldById(String index, String type, String docId, String fieldName);

    Map<String, Object> getFieldByIds(String index, String type, Set<String> ids, String fieldName);

    Map<String, Object> getFieldsById(String index, String type, String docId, String... fieldNames);

    <T> T getTypedFieldById(String index, String type, String docId, String fieldName);

    Map<String, Object> getMandatoryFieldsById(String index, String type, String docId, String... fieldNames);

    <T> T getMandatoryTypedFieldById(String index, String type, String docId, String fieldName);

    String indexDocumentFromJson(String index, String type, String json);

    void updateDocumentFromJson(String index, String type, String id, String json);

    void checkSameDocumentField(String index, String type, String id, String fieldName, String expectedvalue) throws ElasticsearchException;

    void checkSameDocumentIssuer(String index, String type, String id, String expectedIssuer);

    boolean isDocumentExists(String index, String type, String id) throws ElasticsearchException;

    void checkDocumentExists(String index, String type, String id) throws ElasticsearchException;

    /**
     * Retrieve a document by id (safe mode)
     * @param docId
     * @return
     */
    <T extends Object> T getSourceByIdOrNull(String index, String type, String docId, Class<T> classOfT, String... fieldNames);

    /**
     * Retrieve a document by id
     * @param docId
     * @return
     */
    <T extends Object> T getSourceById(String index, String type, String docId, Class<T> classOfT, String... fieldNames);

    <C extends LocalEntity<String>> C readSourceOrNull(SearchHit searchHit, Class<? extends C> clazz);

    void bulkFromClasspathFile(String classpathFile, String indexName, String indexType);

    void bulkFromClasspathFile(String classpathFile, String indexName, String indexType, StringReaderHandler handler);

    void bulkFromFile(File file, String indexName, String indexType);

    void bulkFromFile(File file, String indexName, String indexType, StringReaderHandler handler);

    void bulkFromStream(InputStream is, String indexName, String indexType);

    void bulkFromStream(InputStream is, String indexName, String indexType, StringReaderHandler handler);

    void flushDeleteBulk(final String index, final String type, BulkRequestBuilder bulkRequest);

    void flushBulk(BulkRequestBuilder bulkRequest);

    BulkRequestBuilder bulkDeleteFromSearch(String index,
                                            String type,
                                            SearchRequestBuilder searchRequest,
                                            BulkRequestBuilder bulkRequest,
                                            int bulkSize,
                                            boolean flushAll);

    void safeExecuteRequest(ActionRequestBuilder<?, ?, ?> request, boolean wait);

    ScheduledThreadPoolExecutor scheduler();
}
