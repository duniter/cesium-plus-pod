package org.duniter.elasticsearch.client;

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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.local.LocalEntity;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.handler.StringReaderHandler;
import org.duniter.elasticsearch.exception.AccessDeniedException;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.*;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.count.CountRequest;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.exists.ExistsRequest;
import org.elasticsearch.action.exists.ExistsRequestBuilder;
import org.elasticsearch.action.exists.ExistsResponse;
import org.elasticsearch.action.explain.ExplainRequest;
import org.elasticsearch.action.explain.ExplainRequestBuilder;
import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.action.fieldstats.FieldStatsRequest;
import org.elasticsearch.action.fieldstats.FieldStatsRequestBuilder;
import org.elasticsearch.action.fieldstats.FieldStatsResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.indexedscripts.delete.DeleteIndexedScriptRequest;
import org.elasticsearch.action.indexedscripts.delete.DeleteIndexedScriptRequestBuilder;
import org.elasticsearch.action.indexedscripts.delete.DeleteIndexedScriptResponse;
import org.elasticsearch.action.indexedscripts.get.GetIndexedScriptRequest;
import org.elasticsearch.action.indexedscripts.get.GetIndexedScriptRequestBuilder;
import org.elasticsearch.action.indexedscripts.get.GetIndexedScriptResponse;
import org.elasticsearch.action.indexedscripts.put.PutIndexedScriptRequest;
import org.elasticsearch.action.indexedscripts.put.PutIndexedScriptRequestBuilder;
import org.elasticsearch.action.indexedscripts.put.PutIndexedScriptResponse;
import org.elasticsearch.action.percolate.*;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.suggest.SuggestRequest;
import org.elasticsearch.action.suggest.SuggestRequestBuilder;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.action.termvectors.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.support.Headers;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.threadpool.ThreadPool;

import java.io.*;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Benoit on 08/04/2015.
 */
public class Duniter4jClientImpl implements Duniter4jClient {

    private final ESLogger logger;

    private final Client client;
    private final org.duniter.elasticsearch.threadpool.ThreadPool threadPool;

    @Inject
    public Duniter4jClientImpl(Client client, Settings settings, org.duniter.elasticsearch.threadpool.ThreadPool threadPool) {
        super();
        this.logger = Loggers.getLogger("duniter.client", settings, new String[0]);
        this.client = client;
        this.threadPool = threadPool;
    }

    @Override
    public boolean existsIndex(String indexes) {
        IndicesExistsRequestBuilder requestBuilder = client.admin().indices().prepareExists(indexes);
        IndicesExistsResponse response = requestBuilder.execute().actionGet();
        return response.isExists();
    }

    @Override
    public void deleteIndexIfExists(String indexName){
        if (!existsIndex(indexName)) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("Deleting index [%s]", indexName));
        }

        DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin().indices().prepareDelete(indexName);
        deleteIndexRequestBuilder.execute().actionGet();
    }

    @Override
    public String indexDocumentFromJson(String index, String type, String json) {
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(json)
                .setRefresh(true)
                .execute().actionGet();
        return response.getId();
    }

    @Override
    public void updateDocumentFromJson(String index, String type, String id, String json) {
        // Execute indexBlocksFromNode
        safeExecuteRequest(client.prepareUpdate(index, type, id)
                .setRefresh(true)
                .setDoc(json), true);
    }

    @Override
    public void checkSameDocumentField(String index, String type, String id, String fieldName, String expectedvalue) throws ElasticsearchException {

        GetResponse response = client.prepareGet(index, type, id)
                .setFields(fieldName)
                .execute().actionGet();
        boolean failed = !response.isExists();
        if (failed) {
            throw new NotFoundException(String.format("Document [%s/%s/%s] not exists.", index, type, id));
        } else {
            String docValue = (String)response.getFields().get(fieldName).getValue();
            if (!Objects.equals(expectedvalue, docValue)) {
                throw new AccessDeniedException(String.format("Could not delete this document: not same [%s].", fieldName));
            }
        }
    }

    @Override
    public boolean isDocumentExists(String index, String type, String id) throws ElasticsearchException {
        GetResponse response = client.prepareGet(index, type, id)
                .setFetchSource(false)
                .execute().actionGet();
        return response.isExists();
    }

    @Override
    public void checkDocumentExists(String index, String type, String id) throws ElasticsearchException {
        if (!isDocumentExists(index, type, id)) {
            throw new NotFoundException(String.format("Document [%s/%s/%s] not exists.", index, type, id));
        }
    }


    @Override
    public void checkSameDocumentIssuer(String index, String type, String id, String expectedIssuer) {
        String issuer = getMandatoryFieldsById(index, type, id, Record.PROPERTY_ISSUER).get(Record.PROPERTY_ISSUER).toString();
        if (!ObjectUtils.equals(expectedIssuer, issuer)) {
            throw new AccessDeniedException("Not same issuer");
        }
    }

    /**
     * Retrieve some field from a document id, and check if all field not null
     * @param index
     * @param type
     * @param docId
     * @param fieldNames
     * @return
     */
    @Override
    public Map<String, Object> getMandatoryFieldsById(String index, String type, String docId, String... fieldNames) {
        Map<String, Object> fields = getFieldsById(index, type, docId, fieldNames);
        if (MapUtils.isEmpty(fields)) throw new NotFoundException(String.format("Document [%s/%s/%s] not exists.", index, type, docId));
        Arrays.stream(fieldNames).forEach((fieldName) -> {
            if (!fields.containsKey(fieldName)) throw new NotFoundException(String.format("Document [%s/%s/%s] should have the mandatory field [%s].", index, type, docId, fieldName));
        });
        return fields;
    }

    /**
     * Retrieve some field from a document id
     * @param docId
     * @return
     */
    @Override
    public Map<String, Object> getFieldsById(String index, String type, String docId, String... fieldNames) {
        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        searchRequest.setQuery(QueryBuilders.idsQuery().ids(docId));
        searchRequest.addFields(fieldNames);

        // Execute query
        try {
            SearchResponse response = searchRequest.execute().actionGet();

            if (response.getHits().getTotalHits() == 0) return null;

            Map<String, Object> result = new HashMap<>();
            // Read query result
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                Map<String, SearchHitField> hitFields = searchHit.getFields();
                for(String fieldName: hitFields.keySet()) {
                    result.put(fieldName, hitFields.get(fieldName).getValue());
                }
                break;
            }
            return result;
        }
        catch(SearchPhaseExecutionException e) {
            // Failed or no item on index
            throw new TechnicalException(String.format("[%s/%s] Unable to retrieve fields [%s] for id [%s]",
                    index, type,
                    Joiner.on(',').join(fieldNames).toString(),
                    docId), e);
        }
    }

    /**
     * Retrieve some field from a document id
     * @param index
     * @param type
     * @param ids
     * @param fieldName
     * @return
     */
    @Override
    public Map<String, Object> getFieldByIds(String index, String type, Set<String> ids, String fieldName) {
        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        searchRequest.setQuery(QueryBuilders.idsQuery().ids(ids));
        searchRequest.addFields(fieldName);

        // Execute query
        try {
            SearchResponse response = searchRequest.execute().actionGet();

            Map<String, Object> result = new HashMap<>();
            // Read query result
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                Map<String, SearchHitField> hitFields = searchHit.getFields();
                if (hitFields.get(fieldName) != null) {
                    result.put(searchHit.getId(), hitFields.get(fieldName).getValue());
                }
            }
            return result;
        }
        catch(SearchPhaseExecutionException e) {
            // Failed or no item on index
            throw new TechnicalException(String.format("[%s/%s] Unable to retrieve field [%s] for ids [%s]",
                    index, type, fieldName,
                    Joiner.on(',').join(ids).toString()), e);
        }
    }

    /**
     * Retrieve a field from a document id
     * @param docId
     * @return
     */
    @Override
    public Object getFieldById(String index, String type, String docId, String fieldName) {

        Map<String, Object> result = getFieldsById(index, type, docId, fieldName);
        if (MapUtils.isEmpty(result)) {
            return null;
        }
        return result.get(fieldName);
    }

    @Override
    public <T> T getTypedFieldById(String index, String type, String docId, String fieldName) {
        return (T)getFieldById(index, type, docId, fieldName);
    }

    @Override
    public <T> T getMandatoryTypedFieldById(String index, String type, String docId, String fieldName) {
        Object result = getFieldById(index, type, docId, fieldName);
        if (result == null) {
            throw new NotFoundException(String.format("Document [%s/%s/%s] missing value for mandatory field [%s].", index, type, docId, fieldName));
        }
        return (T)result;
    }

    /**
     * Retrieve a document by id (safe mode)
     * @param docId
     * @return
     */
    @Override
    public <T extends Object> T getSourceByIdOrNull(String index, String type, String docId, Class<T> classOfT, String... fieldNames) {
        try {
            return getSourceById(index, type, docId, classOfT, fieldNames);
        }
        catch(TechnicalException e) {
            return null; // not found
        }
    }

    /**
     * Retrieve a document by id
     * @param docId
     * @return
     */
    @Override
    public <T extends Object> T getSourceById(String index, String type, String docId, Class<T> classOfT, String... fieldNames) {

        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(index)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        searchRequest.setQuery(QueryBuilders.idsQuery(type).ids(docId));
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            searchRequest.setFetchSource(fieldNames, null);
        }
        else {
            searchRequest.setFetchSource(true); // full source
        }

        // Execute query
        try {
            SearchResponse response = searchRequest.execute().actionGet();

            if (response.getHits().getTotalHits() == 0) return null;

            // Read query result
            SearchHit[] searchHits = response.getHits().getHits();
            ObjectMapper objectMapper = JacksonUtils.getThreadObjectMapper();

            for (SearchHit searchHit : searchHits) {
                if (searchHit.source() != null) {
                    return objectMapper.readValue(searchHit.source(), classOfT);
                }
                break;
            }
            return null;
        }
        catch(SearchPhaseExecutionException | IOException e) {
            // Failed to get source
            throw new TechnicalException(String.format("[%s/%s] Error while getting [%s]",
                    index, type,
                    docId), e);
        }
    }

    @Override
    public <C extends LocalEntity<String>> C readSourceOrNull(SearchHit searchHit, Class<? extends C> clazz) {
        try {
            C value = JacksonUtils.getThreadObjectMapper().readValue(searchHit.getSourceRef().streamInput(), clazz);
            value.setId(searchHit.getId());
            return value;
        }
        catch(IOException e) {
            logger.warn(String.format("Unable to deserialize source [%s/%s/%s] into [%s]: %s", searchHit.getIndex(), searchHit.getType(), searchHit.getId(), clazz.getName(), e.getMessage()));
            return null;
        }
    }


    @Override
    public void bulkFromClasspathFile(String classpathFile, String indexName, String indexType) {
        bulkFromClasspathFile(classpathFile, indexName, indexType, null);
    }

    @Override
    public void bulkFromClasspathFile(String classpathFile, String indexName, String indexType, StringReaderHandler handler) {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(classpathFile);
            if (is == null) {
                throw new TechnicalException(String.format("Could not retrieve data file [%s] need to fill index [%s]: ", classpathFile, indexName));
            }

            bulkFromStream(is, indexName, indexType, handler);
        }
        finally {
            if (is != null) {
                try  {
                    is.close();
                }
                catch(IOException e) {
                    // Silent is gold
                }
            }
        }
    }

    @Override
    public void bulkFromFile(File file, String indexName, String indexType) {
        bulkFromFile(file, indexName, indexType, null);
    }

    @Override
    public void bulkFromFile(File file, String indexName, String indexType, StringReaderHandler handler) {
        Preconditions.checkNotNull(file);
        Preconditions.checkArgument(file.exists());

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            bulkFromStream(is, indexName, indexType, handler);
        }
        catch(FileNotFoundException e) {
            throw new TechnicalException(String.format("[%s] Could not find file %s", indexName, file.getPath()), e);
        }
        finally {
            if (is != null) {
                try  {
                    is.close();
                }
                catch(IOException e) {
                    // Silent is gold
                }
            }
        }
    }

    @Override
    public void bulkFromStream(InputStream is, String indexName, String indexType) {
        bulkFromStream(is, indexName, indexType, null);
    }

    @Override
    public void bulkFromStream(InputStream is, String indexName, String indexType, StringReaderHandler handler) {
        Preconditions.checkNotNull(is);
        BulkRequest bulkRequest = Requests.bulkRequest();

        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            StringBuilder builder = new StringBuilder();
            while(line != null) {
                line = line.trim();
                if (StringUtils.isNotBlank(line)) {
                    if (logger.isTraceEnabled()) {
                        logger.trace(String.format("[%s] Add to bulk: %s", indexName, line));
                    }
                    if (handler != null) {
                        line = handler.onReadLine(line.trim());
                    }
                    builder.append(line).append('\n');
                }
                line = br.readLine();
            }

            byte[] data = builder.toString().getBytes();
            bulkRequest.add(new BytesArray(data), indexName, indexType, false);

        } catch(Exception e) {
            throw new TechnicalException(String.format("[%s] Error while inserting rows into %s", indexName, indexType), e);
        }
        finally {
            if (br != null) {
                try  {
                    br.close();
                }
                catch(IOException e) {
                    // Silent is gold
                }
            }
        }

        try {
            client.bulk(bulkRequest).actionGet();
        } catch(Exception e) {
            throw new TechnicalException(String.format("[%s] Error while inserting rows into %s", indexName, indexType), e);
        }
    }

    @Override
    public void flushDeleteBulk(final String index, final String type, final BulkRequestBuilder bulkRequest) {
        if (bulkRequest.numberOfActions() > 0) {

            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            // If failures, continue but save missing blocks
            if (bulkResponse.hasFailures()) {
                // process failures by iterating through each bulk response item
                for (BulkItemResponse itemResponse : bulkResponse) {
                    boolean skip = !itemResponse.isFailed();
                    if (!skip) {
                        logger.debug(String.format("[%s/%s] Error while deleting doc [%s]: %s. Skipping this deletion.", index, type, itemResponse.getId(), itemResponse.getFailureMessage()));
                    }
                }
            }
        }
    }

    @Override
    public void flushBulk(final BulkRequestBuilder bulkRequest) {
        if (bulkRequest.numberOfActions() > 0) {

            // Flush the bulk if not empty
            BulkResponse bulkResponse = bulkRequest.get();

            Set<String> missingDocIds = new LinkedHashSet<>();

            // If failures, continue but save missing blocks
            if (bulkResponse.hasFailures()) {
                // process failures by iterating through each bulk response item
                for (BulkItemResponse itemResponse : bulkResponse) {
                    boolean skip = !itemResponse.isFailed()
                            || missingDocIds.contains(itemResponse.getId());
                    if (!skip) {
                        logger.error(String.format("[%s/%s] could not process _id=%s: %s. Skipping.",
                                itemResponse.getIndex(), itemResponse.getType(), itemResponse.getId(), itemResponse.getFailureMessage()));
                        missingDocIds.add(itemResponse.getId());
                    }
                }
            }
        }
    }

    @Override
    public BulkRequestBuilder bulkDeleteFromSearch(final String index,
                                                   final String type,
                                                   final SearchRequestBuilder searchRequest,
                                                   BulkRequestBuilder bulkRequest,
                                                   final int bulkSize,
                                                   final boolean flushAll) {

        // Execute query, while there is some data
        try {

            int counter = 0;
            boolean loop = true;
            searchRequest.setSize(bulkSize);
            SearchResponse response = searchRequest.execute().actionGet();

            // Execute query, while there is some data
            do {

                // Read response
                SearchHit[] searchHits = response.getHits().getHits();
                for (SearchHit searchHit : searchHits) {

                    // Add deletion to bulk
                    bulkRequest.add(
                            client.prepareDelete(index, type, searchHit.getId())
                    );
                    counter++;

                    // Flush the bulk if not empty
                    if ((bulkRequest.numberOfActions() % bulkSize) == 0) {
                        flushDeleteBulk(index, type, bulkRequest);
                        bulkRequest = client.prepareBulk();
                    }
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

            // last flush
            if (flushAll && (bulkRequest.numberOfActions() % bulkSize) != 0) {
                flushDeleteBulk(index, type, bulkRequest);
            }

        } catch (SearchPhaseExecutionException e) {
            // Failed or no item on index
            logger.error(String.format("Error while deleting by reference: %s. Skipping deletions.", e.getMessage()), e);
        }

        return bulkRequest;
    }

    /* delegate methods */

    @Override
    public AdminClient admin() {
        return client.admin();
    }

    @Override
    public ActionFuture<IndexResponse> index(IndexRequest request) {
        return client.index(request);
    }

    @Override
    public void index(IndexRequest request, ActionListener<IndexResponse> listener) {
        client.index(request, listener);
    }

    @Override
    public IndexRequestBuilder prepareIndex() {
        return client.prepareIndex();
    }

    @Override
    public ActionFuture<UpdateResponse> update(UpdateRequest request) {
        return client.update(request);
    }

    @Override
    public void update(UpdateRequest request, ActionListener<UpdateResponse> listener) {
        client.update(request, listener);
    }

    @Override
    public UpdateRequestBuilder prepareUpdate() {
        return client.prepareUpdate();
    }

    @Override
    public UpdateRequestBuilder prepareUpdate(String index, String type, String id) {
        return client.prepareUpdate(index, type, id);
    }

    @Override
    public IndexRequestBuilder prepareIndex(String index, String type) {
        return client.prepareIndex(index, type);
    }

    @Override
    public IndexRequestBuilder prepareIndex(String index, String type, @Nullable String id) {
        return client.prepareIndex(index, type, id);
    }

    @Override
    public ActionFuture<DeleteResponse> delete(DeleteRequest request) {
        return client.delete(request);
    }

    @Override
    public void delete(DeleteRequest request, ActionListener<DeleteResponse> listener) {
        client.delete(request, listener);
    }

    @Override
    public DeleteRequestBuilder prepareDelete() {
        return client.prepareDelete();
    }

    @Override
    public DeleteRequestBuilder prepareDelete(String index, String type, String id) {
        return client.prepareDelete(index, type, id);
    }

    @Override
    public ActionFuture<BulkResponse> bulk(BulkRequest request) {
        return client.bulk(request);
    }

    @Override
    public void bulk(BulkRequest request, ActionListener<BulkResponse> listener) {
        client.bulk(request, listener);
    }

    @Override
    public BulkRequestBuilder prepareBulk() {
        return client.prepareBulk();
    }

    @Override
    public ActionFuture<GetResponse> get(GetRequest request) {
        return client.get(request);
    }

    @Override
    public void get(GetRequest request, ActionListener<GetResponse> listener) {
        client.get(request, listener);
    }

    @Override
    public GetRequestBuilder prepareGet() {
        return client.prepareGet();
    }

    @Override
    public GetRequestBuilder prepareGet(String index, @Nullable String type, String id) {
        return client.prepareGet(index, type, id);
    }

    @Override
    public PutIndexedScriptRequestBuilder preparePutIndexedScript() {
        return client.preparePutIndexedScript();
    }

    @Override
    public PutIndexedScriptRequestBuilder preparePutIndexedScript(@Nullable String scriptLang, String id, String source) {
        return client.preparePutIndexedScript(scriptLang, id, source);
    }

    @Override
    public void deleteIndexedScript(DeleteIndexedScriptRequest request, ActionListener<DeleteIndexedScriptResponse> listener) {
        client.deleteIndexedScript(request, listener);
    }

    @Override
    public ActionFuture<DeleteIndexedScriptResponse> deleteIndexedScript(DeleteIndexedScriptRequest request) {
        return client.deleteIndexedScript(request);
    }

    @Override
    public DeleteIndexedScriptRequestBuilder prepareDeleteIndexedScript() {
        return client.prepareDeleteIndexedScript();
    }

    @Override
    public DeleteIndexedScriptRequestBuilder prepareDeleteIndexedScript(@Nullable String scriptLang, String id) {
        return client.prepareDeleteIndexedScript(scriptLang, id);
    }

    @Override
    public void putIndexedScript(PutIndexedScriptRequest request, ActionListener<PutIndexedScriptResponse> listener) {
        client.putIndexedScript(request, listener);
    }

    @Override
    public ActionFuture<PutIndexedScriptResponse> putIndexedScript(PutIndexedScriptRequest request) {
        return client.putIndexedScript(request);
    }

    @Override
    public GetIndexedScriptRequestBuilder prepareGetIndexedScript() {
        return client.prepareGetIndexedScript();
    }

    @Override
    public GetIndexedScriptRequestBuilder prepareGetIndexedScript(@Nullable String scriptLang, String id) {
        return client.prepareGetIndexedScript(scriptLang, id);
    }

    @Override
    public void getIndexedScript(GetIndexedScriptRequest request, ActionListener<GetIndexedScriptResponse> listener) {
        client.getIndexedScript(request, listener);
    }

    @Override
    public ActionFuture<GetIndexedScriptResponse> getIndexedScript(GetIndexedScriptRequest request) {
        return client.getIndexedScript(request);
    }

    @Override
    public ActionFuture<MultiGetResponse> multiGet(MultiGetRequest request) {
        return client.multiGet(request);
    }

    @Override
    public void multiGet(MultiGetRequest request, ActionListener<MultiGetResponse> listener) {
        client.multiGet(request, listener);
    }

    @Override
    public MultiGetRequestBuilder prepareMultiGet() {
        return client.prepareMultiGet();
    }

    @Override
    @Deprecated
    public ActionFuture<CountResponse> count(CountRequest request) {
        return client.count(request);
    }

    @Override
    @Deprecated
    public void count(CountRequest request, ActionListener<CountResponse> listener) {
        client.count(request, listener);
    }

    @Override
    @Deprecated
    public CountRequestBuilder prepareCount(String... indices) {
        return client.prepareCount(indices);
    }

    @Override
    @Deprecated
    public ActionFuture<ExistsResponse> exists(ExistsRequest request) {
        return client.exists(request);
    }

    @Override
    @Deprecated
    public void exists(ExistsRequest request, ActionListener<ExistsResponse> listener) {
        client.exists(request, listener);
    }

    @Override
    @Deprecated
    public ExistsRequestBuilder prepareExists(String... indices) {
        return client.prepareExists(indices);
    }

    @Override
    public ActionFuture<SuggestResponse> suggest(SuggestRequest request) {
        return client.suggest(request);
    }

    @Override
    public void suggest(SuggestRequest request, ActionListener<SuggestResponse> listener) {
        client.suggest(request, listener);
    }

    @Override
    public SuggestRequestBuilder prepareSuggest(String... indices) {
        return client.prepareSuggest(indices);
    }

    @Override
    public ActionFuture<SearchResponse> search(SearchRequest request) {
        return client.search(request);
    }

    @Override
    public void search(SearchRequest request, ActionListener<SearchResponse> listener) {
        client.search(request, listener);
    }

    @Override
    public SearchRequestBuilder prepareSearch(String... indices) {
        return client.prepareSearch(indices);
    }

    @Override
    public ActionFuture<SearchResponse> searchScroll(SearchScrollRequest request) {
        return client.searchScroll(request);
    }

    @Override
    public void searchScroll(SearchScrollRequest request, ActionListener<SearchResponse> listener) {
        client.searchScroll(request, listener);
    }

    @Override
    public SearchScrollRequestBuilder prepareSearchScroll(String scrollId) {
        return client.prepareSearchScroll(scrollId);
    }

    @Override
    public ActionFuture<MultiSearchResponse> multiSearch(MultiSearchRequest request) {
        return client.multiSearch(request);
    }

    @Override
    public void multiSearch(MultiSearchRequest request, ActionListener<MultiSearchResponse> listener) {
        client.multiSearch(request, listener);
    }

    @Override
    public MultiSearchRequestBuilder prepareMultiSearch() {
        return client.prepareMultiSearch();
    }

    @Override
    public ActionFuture<TermVectorsResponse> termVectors(TermVectorsRequest request) {
        return client.termVectors(request);
    }

    @Override
    public void termVectors(TermVectorsRequest request, ActionListener<TermVectorsResponse> listener) {
        client.termVectors(request, listener);
    }

    @Override
    public TermVectorsRequestBuilder prepareTermVectors() {
        return client.prepareTermVectors();
    }

    @Override
    public TermVectorsRequestBuilder prepareTermVectors(String index, String type, String id) {
        return client.prepareTermVectors(index, type, id);
    }

    @Override
    @Deprecated
    public ActionFuture<TermVectorsResponse> termVector(TermVectorsRequest request) {
        return client.termVector(request);
    }

    @Override
    @Deprecated
    public void termVector(TermVectorsRequest request, ActionListener<TermVectorsResponse> listener) {
        client.termVector(request, listener);
    }

    @Override
    @Deprecated
    public TermVectorsRequestBuilder prepareTermVector() {
        return client.prepareTermVector();
    }

    @Override
    @Deprecated
    public TermVectorsRequestBuilder prepareTermVector(String index, String type, String id) {
        return client.prepareTermVector(index, type, id);
    }

    @Override
    public ActionFuture<MultiTermVectorsResponse> multiTermVectors(MultiTermVectorsRequest request) {
        return client.multiTermVectors(request);
    }

    @Override
    public void multiTermVectors(MultiTermVectorsRequest request, ActionListener<MultiTermVectorsResponse> listener) {
        client.multiTermVectors(request, listener);
    }

    @Override
    public MultiTermVectorsRequestBuilder prepareMultiTermVectors() {
        return client.prepareMultiTermVectors();
    }

    @Override
    public ActionFuture<PercolateResponse> percolate(PercolateRequest request) {
        return client.percolate(request);
    }

    @Override
    public void percolate(PercolateRequest request, ActionListener<PercolateResponse> listener) {
        client.percolate(request, listener);
    }

    @Override
    public PercolateRequestBuilder preparePercolate() {
        return client.preparePercolate();
    }

    @Override
    public ActionFuture<MultiPercolateResponse> multiPercolate(MultiPercolateRequest request) {
        return client.multiPercolate(request);
    }

    @Override
    public void multiPercolate(MultiPercolateRequest request, ActionListener<MultiPercolateResponse> listener) {
        client.multiPercolate(request, listener);
    }

    @Override
    public MultiPercolateRequestBuilder prepareMultiPercolate() {
        return client.prepareMultiPercolate();
    }

    @Override
    public ExplainRequestBuilder prepareExplain(String index, String type, String id) {
        return client.prepareExplain(index, type, id);
    }

    @Override
    public ActionFuture<ExplainResponse> explain(ExplainRequest request) {
        return client.explain(request);
    }

    @Override
    public void explain(ExplainRequest request, ActionListener<ExplainResponse> listener) {
        client.explain(request, listener);
    }

    @Override
    public ClearScrollRequestBuilder prepareClearScroll() {
        return client.prepareClearScroll();
    }

    @Override
    public ActionFuture<ClearScrollResponse> clearScroll(ClearScrollRequest request) {
        return client.clearScroll(request);
    }

    @Override
    public void clearScroll(ClearScrollRequest request, ActionListener<ClearScrollResponse> listener) {
        client.clearScroll(request, listener);
    }

    @Override
    public FieldStatsRequestBuilder prepareFieldStats() {
        return client.prepareFieldStats();
    }

    @Override
    public ActionFuture<FieldStatsResponse> fieldStats(FieldStatsRequest request) {
        return client.fieldStats(request);
    }

    @Override
    public void fieldStats(FieldStatsRequest request, ActionListener<FieldStatsResponse> listener) {
        client.fieldStats(request, listener);
    }

    @Override
    public Settings settings() {
        return client.settings();
    }

    @Override
    public Headers headers() {
        return client.headers();
    }

    public <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> ActionFuture<Response> execute(Action<Request, Response, RequestBuilder> action, Request request) {
        return client.execute(action, request);
    }

    public <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> void execute(Action<Request, Response, RequestBuilder> action, Request request, ActionListener<Response> listener) {
        client.execute(action, request, listener);
    }

    public <Request extends ActionRequest, Response extends ActionResponse, RequestBuilder extends ActionRequestBuilder<Request, Response, RequestBuilder>> RequestBuilder prepareExecute(Action<Request, Response, RequestBuilder> action) {
        return client.prepareExecute(action);
    }

    public ThreadPool threadPool() {
        return client.threadPool();
    }

    public ScheduledThreadPoolExecutor scheduler() {
        return (ScheduledThreadPoolExecutor)client.threadPool().scheduler();
    }

    public void close() {
        client.close();
    }

    public void safeExecuteRequest(ActionRequestBuilder<?, ?, ?> request, boolean wait) {
        // Execute in a pool
        if (!wait) {
            boolean acceptedInPool = false;
            while(!acceptedInPool)
                try {
                    request.execute();
                    acceptedInPool = true;
                }
                catch(EsRejectedExecutionException e) {
                    // not accepted, so wait
                    try {
                        Thread.sleep(1000); // 1s
                    }
                    catch(InterruptedException e2) {
                        // silent
                    }
                }

        } else {
            request.execute().actionGet();
        }
    }
}
