package org.duniter.elasticsearch.synchro;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.HttpService;
import org.duniter.core.client.service.exception.HttpNotFoundException;
import org.duniter.core.client.service.exception.HttpUnauthorizeException;
import org.duniter.core.exception.BusinessException;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.exception.InvalidSignatureException;
import org.duniter.elasticsearch.model.SearchHit;
import org.duniter.elasticsearch.model.SearchScrollResponse;
import org.duniter.elasticsearch.model.SynchroResult;
import org.duniter.elasticsearch.service.AbstractService;
import org.duniter.elasticsearch.service.ServiceLocator;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.synchro.impl.NullSynchroActionResult;
import org.duniter.elasticsearch.synchro.impl.SynchroActionResultImpl;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.util.bytes.JsonNodeBytesReference;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

public abstract class AbstractSynchroAction extends AbstractService implements SynchroAction {

    private static final String SCROLL_TIME_TO_LIVE_SHORT = "1m";

    private static final String SCROLL_TIME_TO_LIVE_LONG = "10m";

    private static final int SCROLL_MAX_RETRY = 5;

    private static SynchroActionResult NULL_ACTION_RESULT = new NullSynchroActionResult();

    private String fromIndex;
    private String fromType;
    private String toIndex;
    private String toType;
    private String issuerFieldName = Record.PROPERTY_ISSUER;
    private String versionFieldName = Record.PROPERTY_TIME;
    private String timeFieldName = versionFieldName;
    private ChangeSource changeSource;

    private HttpService httpService;

    private boolean enableUpdate = false;
    private boolean enableSignatureValidation = true;
    private boolean enableTimeValidation = true;
    private List<SourceConsumer> insertionListeners;
    private List<SourceConsumer> updateListeners;
    private List<SourceConsumer> validationListeners;

    private boolean trace = false;

    public AbstractSynchroAction(String index, String type,
                                 Duniter4jClient client,
                                 PluginSettings pluginSettings,
                                 CryptoService cryptoService,
                                 ThreadPool threadPool) {
        this(index, type, index, type, client, pluginSettings, cryptoService, threadPool);
    }

    public AbstractSynchroAction(String fromIndex, String fromType,
                                 String toIndex, String toType,
                                 Duniter4jClient client,
                                 PluginSettings pluginSettings,
                                 CryptoService cryptoService,
                                 ThreadPool threadPool) {
        super("duniter.p2p." + toIndex, client, pluginSettings, cryptoService);
        this.fromIndex = fromIndex;
        this.fromType = fromType;
        this.toIndex = toIndex;
        this.toType = toType;
        this.changeSource = new ChangeSource()
                .addIndex(fromIndex)
                .addType(fromType);
        this.trace = logger.isTraceEnabled();
        threadPool.scheduleOnStarted(() -> httpService = ServiceLocator.instance().getHttpService());
    }


    @Override
    public abstract EndpointApi getEndPointApi();

    @Override
    public ChangeSource getChangeSource() {
        return changeSource;
    }

    @Override
    public void handleSynchronize(Peer peer,
                                  long fromTime,
                                  SynchroResult result) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkArgument(fromTime >= 0);
        Preconditions.checkNotNull(result);

        String logPrefix = String.format("[%s] [%s] [%s/%s]", peer.getCurrency(), peer, toIndex, toType);

        // Log start
        if (logger.isDebugEnabled()) {
            if (Record.PROPERTY_TIME.equals(versionFieldName)) {
                // Since a date
                if (fromTime > 0) {
                    logger.debug(String.format("%s Synchronization {since %s}...", logPrefix,
                            DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
                                    .format(new Date(fromTime * 1000))));
                }
                // Force full synchro
                else {
                    logger.debug(String.format("%s Synchronization {All}...", logPrefix));
                }
            }
            else {
                // From a value
                logger.debug(String.format("%s Synchronization {where %s > %s}...", logPrefix, versionFieldName, fromTime));
            }
        }

        try {
            QueryBuilder query = createQuery(fromTime);

            // Apply synchro
            synchronize(peer, query, result);

            // Log end
            if (logger.isDebugEnabled()) logger.debug(String.format("%s Synchronization [OK]", logPrefix));
        }
        catch(IndexNotFoundException e1) {
            logger.debug(String.format("%s Index not exists locally. Skipping", logPrefix));
        }
//        catch(DuniterElasticsearchException e) {
//            // Log the first error
//            logger.error(e.getMessage());
//        }
    }

    @Override
    public void handleChange(Peer peer, ChangeEvent changeEvent) {

        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(changeEvent);
        Preconditions.checkNotNull(changeEvent.getOperation());

        String id = changeEvent.getId();
        String logPrefix = String.format("[%s] [%s] [%s/%s/%s] [WS]", peer.getCurrency(), peer, toIndex, toType, id);

        boolean skip = changeEvent.getOperation() == ChangeEvent.Operation.DELETE ||
                !enableUpdate && changeEvent.getOperation() == ChangeEvent.Operation.INDEX ||
                !changeEvent.hasSource();
        if (skip) {
            if (trace) {
                logger.trace(String.format("%s Ignoring change event of type [%s]", logPrefix, changeEvent.getOperation().name()));
            }
            return;
        }
        try {
            if (trace) {
                logger.trace(String.format("%s Processing new change event...", logPrefix));
            }
            // Save doc
            save(changeEvent.getId(), changeEvent.getSource(), logPrefix);
        }
        catch(Exception e1) {
            // Log the first error
            if (logger.isDebugEnabled()) {
                logger.error(e1.getMessage(), e1);
            }
            else {
                logger.error(e1.getMessage());
            }
        }
    }

    public void addInsertionListener(SourceConsumer listener) {
        if (insertionListeners == null) {
            insertionListeners = Lists.newArrayList();
        }
        insertionListeners.add(listener);
    }

    public void addUpdateListener(SourceConsumer listener) {
        if (updateListeners == null) {
            updateListeners = Lists.newArrayList();
        }
        updateListeners.add(listener);
    }

    public void addValidationListener(SourceConsumer listener) {
        if (validationListeners == null) {
            validationListeners = Lists.newArrayList();
        }
        validationListeners.add(listener);
    }

    /* -- protected methods -- */

    protected void notifyInsertion(final String id, final JsonNode source, final SynchroActionResult actionResult) throws Exception {
        if (CollectionUtils.isNotEmpty(insertionListeners)) {
            for (SourceConsumer listener: insertionListeners) {
                listener.accept(id, source, actionResult);
            }
        }
    }

    protected void notifyUpdate(final String id, final JsonNode source, final SynchroActionResult actionResult) throws Exception {
        if (CollectionUtils.isNotEmpty(updateListeners)) {
            for (SourceConsumer listener: updateListeners) {
                listener.accept(id, source, actionResult);
            }
        }
    }

    protected void notifyValidation(final String id,
                                    final JsonNode source,
                                    final boolean allowOldDocuments,
                                    final SynchroActionResult actionResult,
                                    final String logPrefix) throws Exception {

        // Validate signature
        if (enableSignatureValidation) {
            try {
                readAndVerifyIssuerSignature(source, issuerFieldName);
            } catch (InvalidSignatureException e) {
                // FIXME: some user/profile document failed ! - see issue #11
                // Il semble que le format JSON ne soit pas le même que celui qui a été signé
                actionResult.addInvalidSignature();
                if (trace) {
                    logger.warn(String.format("%s %s.\n%s", logPrefix, e.getMessage(), source.toString()));
                }
            }
        }

        // Validate time
        if (enableTimeValidation) {
            try {
                verifyTime(source, allowOldDocuments, timeFieldName);
            } catch (InvalidSignatureException e) {
                actionResult.addInvalidTime();
                if (trace) {
                    logger.warn(String.format("%s %s.", logPrefix, e.getMessage()));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(validationListeners)) {
            for (SourceConsumer listener : validationListeners) {
                listener.accept(id, source, actionResult);
            }
        }
    }

    protected QueryBuilder createQuery(long fromTime) {
        return QueryBuilders.constantScoreQuery(
                QueryBuilders.boolQuery()
                        .filter(QueryBuilders.rangeQuery("time").gte(fromTime))
            );
    }

    private HttpPost createScrollRequest(Peer peer,
                                         String fromIndex,
                                         String fromType,
                                         QueryBuilder query,
                                         String scrollTime,
                                         long from,
                                         long size) {
        HttpPost httpPost = new HttpPost(httpService.getPath(peer, fromIndex, fromType, "_search?scroll=" + scrollTime));
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        //httpPost.setHeader("Accept-Encoding", "gzip");

        try {
            // Query to String
            String queryString;
            {
                BytesStreamOutput bos = new BytesStreamOutput();
                XContentBuilder builder = new XContentBuilder(JsonXContent.jsonXContent, bos);
                query.toXContent(builder, null);
                builder.flush();
                queryString = bos.bytes().toUtf8();
                String qt = query.buildAsBytes(XContentType.JSON).toUtf8();
                if (qt != null && qt.equalsIgnoreCase(queryString)) {
                    logger.warn("TODO: simplify query -> String");
                }
            }

            String content = null;
            if (from == 0) {
                // Sort on "_doc" - see https://www.elastic.co/guide/en/elasticsearch/reference/2.4/search-request-scroll.html
                content = String.format("{\"query\":%s, \"size\":%s, \"sort\": [\"_doc\"]}",
                        queryString, size);
            }
            else {
                // Sort on "_doc" - see https://www.elastic.co/guide/en/elasticsearch/reference/2.4/search-request-scroll.html
                content = String.format("{\"query\":%s, \"from\":%s, \"size\":%s, \"sort\": [\"_doc\"]}",
                        queryString, from, size);
            }

            httpPost.setEntity(new StringEntity(content, "UTF-8"));

            if (trace) {
                logger.trace(String.format("[%s] [%s] [%s/%s] Sending POST scroll request: %s", peer.getCurrency(), peer, fromIndex, fromType, content));
            }

        } catch (IOException e) {
            throw new TechnicalException("Error while preparing search query: " + e.getMessage(), e);
        }

        return httpPost;
    }

    private HttpPost createNextScrollRequest(Peer peer,
                                             String scrollId,
                                             String scrollKeepAliveTime) {

        HttpPost httpPost = new HttpPost(httpService.getPath(peer, "_search", "scroll"));
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        //httpPost.setHeader("Accept-Encoding", "gzip");
        httpPost.setEntity(new StringEntity(String.format("{\"scroll\": \"%s\", \"scroll_id\": \"%s\"}",
                scrollKeepAliveTime,
                scrollId), "UTF-8"));
        return httpPost;
    }

    private SearchScrollResponse executeAndParseRequest(Peer peer, HttpUriRequest request) {
        try {
            // Execute query & parse response
            return httpService.executeRequest(request, SearchScrollResponse.class);
        } catch (HttpUnauthorizeException e) {
            throw new TechnicalException(String.format("[%s] [%s] [%s/%s] Unable to access (%s).", peer.getCurrency(), peer, fromIndex, fromType, e.getMessage()), e);
        } catch (TechnicalException e) {
            throw new TechnicalException(String.format("[%s] [%s] [%s/%s] Unable to scroll request: %s", peer.getCurrency(), peer, fromIndex, fromType, e.getMessage()), e);
        } catch (BusinessException e) {
            throw e; // Keep HttpNotFoundException
        } catch (Exception e) {
            throw new TechnicalException(String.format("[%s] [%s] [%s/%s] Unable to parse response: ", peer.getCurrency(), peer, fromIndex, fromType, e.getMessage()), e);
        }
    }

    private void synchronize(Peer peer,
                             QueryBuilder query,
                             SynchroResult result) {


        // DEV ONLY: skip
        //if (!"user".equalsIgnoreCase(fromIndex) || !"profile".equalsIgnoreCase(fromType)) {
        //if (!"market".equalsIgnoreCase(fromIndex) || !"record".equalsIgnoreCase(fromType)) {
        //    return;
        //}

        // Check index exists locally
        if (!client.existsIndex(toIndex)) {
            throw new IndexNotFoundException(String.format("Index [%s] not exists locally. Skipping", toIndex));
        }

        String logPrefix = String.format("[%s] [%s] [%s/%s] ", peer.getCurrency(), peer, toIndex, toType);
        ObjectMapper objectMapper = getObjectMapper();

        int size = pluginSettings.getIndexBulkSizeForSynchro();
        long from = 0;
        long total = -1;

        String currentScrollId = null;
        String scrollKeepAliveTime = SCROLL_TIME_TO_LIVE_SHORT;
        int scrollRetryCounter = 0;

        do {
            SearchScrollResponse response = null;

            // If exists, reuse the previous scroll
            if (currentScrollId != null) {
                try {
                    HttpUriRequest request = createNextScrollRequest(peer, currentScrollId, scrollKeepAliveTime);
                    response = executeAndParseRequest(peer, request);
                    scrollRetryCounter = 0; // Reset the scroll retry count
                }
                catch(HttpNotFoundException e) {
                    scrollRetryCounter++;
                    if (scrollRetryCounter >= SCROLL_MAX_RETRY) throw e;
                    // Already 2 retry: retry but increase the scroll duration
                    if (scrollRetryCounter >= 2) {
                        scrollKeepAliveTime = SCROLL_TIME_TO_LIVE_LONG; // Increase the scroll time
                    }

                    // Reset the scroll id (will create a new scroll request)
                    currentScrollId = null;

                    logger.warn(String.format("%s Scroll request closed (by remote pod). Retrying {%s/%s}...",
                            logPrefix, scrollRetryCounter, SCROLL_MAX_RETRY));
                }
            }

            // Create a new scroll request
            if (currentScrollId == null){
                HttpUriRequest request = createScrollRequest(peer, fromIndex, fromType, query, scrollKeepAliveTime, from, size);
                response = executeAndParseRequest(peer, request);
                currentScrollId = (response != null) ? response.getScrollId() : null;
                if (currentScrollId == null) {
                    logger.warn(String.format("%s Missing scroll id in the response. Skipping", logPrefix));
                }
            }

            if (currentScrollId != null) {

                // Indexing
                fetchAndSave(peer, response, objectMapper, result);
                from += size;
                if (total == -1) total = response.getHits().getTotalHits();

                // Log progress
                if (logger.isInfoEnabled() && total > 0) {
                    long pct = Math.min(100, Math.round(from * 100 / total));
                    logger.info(String.format("%s Indexing documents... %s / %s (%s%%)", logPrefix, from, total, pct));
                }
            }
        }
        while(currentScrollId != null && from<total);


    }

    private long fetchAndSave(final Peer peer,
                              final SearchScrollResponse response,
                              final ObjectMapper objectMapper,
                              final SynchroResult result) {


        long counter = 0;

        SynchroActionResult actionResult = new SynchroActionResultImpl();

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        bulkRequest.setRefresh(true);


        for (SearchHit hit: response.getHits().getHits()){
            //org.elasticsearch.search.SearchHits hit = hits.next();
            String id = hit.getId();

            String logPrefix = String.format("[%s] [%s] [%s/%s/%s]", peer.getCurrency(), peer, toIndex, toType, id);

            counter++;

            if (hit.getSource() == null) {
                logger.error(String.format("%s No source found. Skipping.", logPrefix));
            }
            else {
                // Save (create or update)
                save(id, hit.getSource(),
                     objectMapper,
                     bulkRequest,
                     true, // allow old documents
                     actionResult,
                     logPrefix);
            }
        }

        if (bulkRequest.numberOfActions() > 0) {

            // Flush the bulk if not empty
            BulkResponse bulkResponse = bulkRequest.get();
            Set<String> missingDocIds = new LinkedHashSet<>();

            // If failures, continue but saveInBulk missing blocks
            if (bulkResponse.hasFailures()) {
                // process failures by iterating through each bulk response item
                for (BulkItemResponse itemResponse : bulkResponse) {
                    boolean skip = !itemResponse.isFailed()
                            || missingDocIds.contains(itemResponse.getId());
                    if (!skip) {
                        if (trace) {
                            logger.debug(String.format("[%s] [%s] [%s/%s] could not process _id=%s: %s. Skipping.", peer.getCurrency(), peer, toIndex, toType, itemResponse.getId(), itemResponse.getFailureMessage()));
                        }
                        missingDocIds.add(itemResponse.getId());
                    }
                }
            }
        }

        // update result
        result.addInserts(toIndex, toType, actionResult.getInserts());
        result.addUpdates(toIndex, toType, actionResult.getUpdates());
        result.addDeletes(toIndex, toType, actionResult.getDeletes());
        result.addInvalidSignatures(toIndex, toType, actionResult.getInvalidSignatures());
        result.addInvalidTimes(toIndex, toType, actionResult.getInvalidTimes());

        return counter;
    }

    protected void save(String id, BytesReference sourceRef, String logPrefix) throws IOException{
        ObjectMapper om = getObjectMapper();
        save(id, sourceRef, om, null, false, NULL_ACTION_RESULT, logPrefix);
    }

    protected void save(String id,
                        final JsonNode source,
                        final ObjectMapper objectMapper,
                        final BulkRequestBuilder bulkRequest,
                        final boolean allowOldDocuments,
                        final SynchroActionResult actionResult,
                        final String logPrefix) {
        save(id, new JsonNodeBytesReference(source, objectMapper), objectMapper, bulkRequest, allowOldDocuments, actionResult, logPrefix);
    }

    protected void save(final String id,
                        final BytesReference sourceRef,
                        final ObjectMapper objectMapper,
                        final BulkRequestBuilder bulkRequest,
                        final boolean allowOldDocuments,
                        final SynchroActionResult actionResult,
                        final String logPrefix) {

        try {
            // Parse byte reference
            // WARN: always use BytesJsonNode.readTree(), because it can reuse the existing JsonNode,
            // if source use BytesJsonNode as implementation class
            JsonNode source = JsonNodeBytesReference.readTree(sourceRef, objectMapper);

            String issuer = source.get(issuerFieldName).asText();
            if (StringUtils.isBlank(issuer)) {
                throw new InvalidFormatException(String.format("Invalid format: missing or null %s field.", issuerFieldName));
            }
            long version = source.get(versionFieldName).asLong(-1);
            if (version == -1) {
                throw new InvalidFormatException(String.format("Invalid format: missing or null %s field.", versionFieldName));
            }

            Map<String, Object> existingFields = client.getFieldsById(toIndex, toType, id, versionFieldName, issuerFieldName);
            boolean exists = existingFields != null;

            // Insert (new doc)
            if (!exists) {

                if (trace) {
                    logger.trace(String.format("%s insert found\n%s", logPrefix, source.toString()));
                }

                // Validate doc
                notifyValidation(id, source, allowOldDocuments, actionResult, logPrefix);

                // Execute insertion
                IndexRequestBuilder request = client.prepareIndex(toIndex, toType, id)
                        .setSource(sourceRef.toBytes());
                if (bulkRequest != null) {
                    bulkRequest.add(request);
                }
                else {
                    client.safeExecuteRequest(request, false);
                }

                // Notify insert listeners
                notifyInsertion(id, source, actionResult);

                actionResult.addInsert();
            }

            // Existing doc: do update (if enable)
            else if (enableUpdate){

                // Check same issuer
                String existingIssuer = (String) existingFields.get(issuerFieldName);
                if (!Objects.equals(issuer, existingIssuer)) {
                    throw new InvalidFormatException(String.format("Invalid document: not same [%s].", issuerFieldName));
                }

                // Check version
                Number existingVersion = null;
                Object versionObj = existingFields.get(versionFieldName);
                if (versionObj != null) {
                    if (versionObj instanceof String) {
                        existingVersion = Long.parseLong((String) versionObj);
                    } else if (versionObj instanceof Number) {
                        existingVersion = ((Number) versionObj);
                    } else {
                        throw new InvalidFormatException(String.format("Invalid document: '%s' should be a long, but found: %s", versionFieldName, versionObj));
                    }
                }

                boolean doUpdate = (existingVersion == null || version > existingVersion.longValue());

                if (doUpdate) {
                    if (trace) {
                        logger.trace(String.format("%s found update\n%s", logPrefix, source.toString()));
                    }

                    // Validate source
                    notifyValidation(id, source, allowOldDocuments, actionResult, logPrefix);

                    // Execute update
                    UpdateRequestBuilder request = client.prepareUpdate(toIndex, toType, id);
                    request.setDoc(source);
                    if (bulkRequest != null) {
                        bulkRequest.add(request);
                    }
                    else {
                        request.setRefresh(true);
                        client.safeExecuteRequest(request, false);
                    }

                    // Notify insert listeners
                    notifyUpdate(id, source, actionResult);

                    actionResult.addUpdate();
                }
            }

        } catch (DuniterElasticsearchException e) {
            // Skipping document: log, then continue
            logger.warn(String.format("%s %s. Skipping.", logPrefix, e.getMessage()));
        } catch (Throwable e) {
            // Skipping document: log, then continue
            logger.error(String.format("%s %s. Skipping.", logPrefix, e.getMessage()), e);
        }
    }

    protected void setIssuerFieldName(String issuerFieldName) {
        this.issuerFieldName = issuerFieldName;
    }

    protected void setVersionFieldName(String versionFieldName) {
        this.versionFieldName = versionFieldName;
    }

    protected void setTimeFieldName(String timeFieldName) {
        this.timeFieldName = timeFieldName;
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }

    protected void setEnableUpdate(boolean enableUpdate) {
        this.enableUpdate = enableUpdate;
    }

    protected void setEnableSignatureValidation(boolean enableSignatureValidation) {
        this.enableSignatureValidation = enableSignatureValidation;
    }

    protected void setEnableTimeValidation(boolean enableTimeValidation) {
        this.enableTimeValidation = enableTimeValidation;
    }

}
