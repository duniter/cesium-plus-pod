package org.duniter.elasticsearch.user.service;

/*
 * #%L
 * UCoin Java Client :: Core API
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
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.service.MailService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.InvalidSignatureException;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Benoit on 30/03/2015.
 */
public class UserEventService extends AbstractService implements ChangeService.ChangeListener {


    public interface UserEventListener {
        String getId();
        String getPubkey();
        void onEvent(UserEvent event);
    }

    public static final String INDEX = "user";
    public static final String EVENT_TYPE = "event";

    private static final Map<String, UserEventListener> LISTENERS = new HashMap<>();

    private static final List<ChangeSource> CHANGE_LISTEN_SOURCES = ImmutableList.of(new ChangeSource(INDEX, EVENT_TYPE));

    public static void registerListener(UserEventListener listener) {
        synchronized (LISTENERS) {
            LISTENERS.put(listener.getId(), listener);
        }
    }

    public static synchronized void unregisterListener(UserEventListener listener) {
        synchronized (LISTENERS) {
            LISTENERS.remove(listener.getId());
        }
    }

    private final MailService mailService;
    private final ThreadPool threadPool;
    public final boolean trace;

    @Inject
    public UserEventService(final Duniter4jClient client,
                            final PluginSettings pluginSettings,
                            final CryptoService cryptoService,
                            final MailService mailService,
                            final ThreadPool threadPool) {
        super("duniter.user.event", client, pluginSettings, cryptoService);
        this.mailService = mailService;
        this.threadPool = threadPool;
        this.trace = logger.isTraceEnabled();

        ChangeService.registerListener(this);

    }

    /**
     * Notify a user
     */
    public ListenableActionFuture<IndexResponse> notifyUser(UserEvent event) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(event.getRecipient());

        // Get user profile locale
        UserProfile userProfile = getUserProfile(event.getRecipient(),
                UserProfile.PROPERTY_EMAIL, UserProfile.PROPERTY_TITLE, UserProfile.PROPERTY_LOCALE);

        Locale locale = userProfile.getLocale() != null ? new Locale(userProfile.getLocale()) : null;

        // Add new event to index
        return indexEvent(locale, event);
    }

    public ListenableActionFuture<IndexResponse> indexEvent(Locale locale, UserEvent event) {
        Preconditions.checkNotNull(event.getRecipient());
        Preconditions.checkNotNull(event.getType());
        Preconditions.checkNotNull(event.getCode());

        String nodePubkey = pluginSettings.getNodePubkey();

        // Generate json
        String eventJson;
        if (StringUtils.isNotBlank(nodePubkey)) {
            UserEvent signedEvent = new UserEvent(event);
            signedEvent.setMessage(event.getLocalizedMessage(locale));
            // set issuer, hash, signature
            signedEvent.setIssuer(nodePubkey);

            // Add hash
            String hash = cryptoService.hash(toJson(signedEvent, true));
            signedEvent.setHash(hash);

            // Add signature
            String signature = cryptoService.sign(toJson(signedEvent, true), pluginSettings.getNodeKeypair().getSecKey());
            signedEvent.setSignature(signature);

            eventJson = toJson(signedEvent);
        } else {
            logger.warn("Could not generate hash for new user event (no keyring)");
            // Node has not keyring: do NOT sign it
            eventJson = event.toJson(locale);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a event to recipient [%s]", event.getRecipient().substring(0, 8)));
        }

        // do indexation
        return indexEvent(eventJson, false /*checkSignature*/);

    }

    public ListenableActionFuture<IndexResponse> indexEvent(String eventJson) {
        return indexEvent(eventJson, true);
    }

    public ListenableActionFuture<IndexResponse> indexEvent(String eventJson, boolean checkSignature) {

        if (checkSignature) {
            JsonNode jsonNode = readAndVerifyIssuerSignature(eventJson);
            String recipient = getMandatoryField(jsonNode, UserEvent.PROPERTY_ISSUER).asText();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Indexing a event to recipient [%s]", recipient.substring(0, 8)));
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace(eventJson);
        }

        return client.prepareIndex(INDEX, EVENT_TYPE)
                .setSource(eventJson)
                .setRefresh(false)
                .execute();
    }

    public ActionFuture<?> deleteEventsByReference(final UserEvent.Reference reference) {
        Preconditions.checkNotNull(reference);
        Preconditions.checkNotNull(reference.getIndex());
        Preconditions.checkNotNull(reference.getType());

        return threadPool.schedule(() -> doDeleteEventsByReference(reference));
    }

    public ListenableActionFuture<UpdateResponse> markEventAsRead(String id, String signature) {

        Map<String, Object> fields = client.getMandatoryFieldsById(INDEX, EVENT_TYPE, id, UserEvent.PROPERTY_HASH, UserEvent.PROPERTY_RECIPIENT);
        String recipient = fields.get(UserEvent.PROPERTY_RECIPIENT).toString();
        String hash = fields.get(UserEvent.PROPERTY_HASH).toString();

        // Check signature
        boolean valid = cryptoService.verify(hash, signature, recipient);
        if (!valid) {
            throw new InvalidSignatureException("Invalid signature: only the recipient can mark an event as read.");
        }

        return client.prepareUpdate(INDEX, EVENT_TYPE, id)
                .setDoc("read_signature", signature)
                .execute();
    }



    public List<UserEvent> getUserEvents(String pubkey, Long lastTime, String[] includesCodes, String[] excludesCodes) {

        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery(UserEvent.PROPERTY_RECIPIENT, pubkey));
        if (lastTime != null) {
            query.must(QueryBuilders.rangeQuery(UserEvent.PROPERTY_TIME).gt(lastTime));
        }

        if (CollectionUtils.isNotEmpty(includesCodes)) {
            query.must(QueryBuilders.termsQuery(UserEvent.PROPERTY_CODE, includesCodes));
        }
        if (CollectionUtils.isNotEmpty(excludesCodes)) {
            query.mustNot(QueryBuilders.termsQuery(UserEvent.PROPERTY_CODE, excludesCodes));
        }

        SearchResponse response = client.prepareSearch(INDEX)
                .setTypes(EVENT_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setFetchSource(true)
                .setQuery(query)
                .addSort(UserEvent.PROPERTY_TIME, SortOrder.DESC)
                .get();


        return Arrays.asList(response.getHits().getHits()).stream()
                .map(searchHit -> client.readSourceOrNull(searchHit, UserEvent.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /* -- Internal methods -- */


    public static XContentBuilder createEventType() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(EVENT_TYPE)
                    .startObject("properties")

                    // type
                    .startObject("type")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // issuer
                    .startObject("issuer")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // recipient
                    .startObject("recipient")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // time
                    .startObject("time")
                    .field("type", "integer")
                    .endObject()

                    // code
                    .startObject("code")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // reference
                    .startObject("reference")
                        .field("type", "nested")
                        .field("dynamic", "false")
                        .startObject("properties")
                            .startObject("index")
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject("type")
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject("id")
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject("hash")
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                            .startObject("anchor")
                                .field("type", "string")
                                .field("index", "not_analyzed")
                            .endObject()
                        .endObject()
                    .endObject()

                    // message
                    .startObject("message")
                    .field("type", "string")
                    .endObject()

                    // params
                    .startObject("params")
                    .field("type", "string")
                    .endObject()

                    // hash
                    .startObject("hash")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // signature
                    .startObject("signature")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // read_signature
                    .startObject("read_signature")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", INDEX, EVENT_TYPE, ioe.getMessage()), ioe);
        }
    }

    private UserProfile getUserProfile(String pubkey, String... fieldnames) {
        UserProfile result = client.getSourceByIdOrNull(UserService.INDEX, UserService.PROFILE_TYPE, pubkey, UserProfile.class, fieldnames);
        if (result == null) result = new UserProfile();
        return result;
    }

    private void doDeleteEventsByReference(final UserEvent.Reference reference) {

        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(INDEX)
                .setTypes(EVENT_TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        // Query = filter on reference
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(UserEvent.PROPERTY_REFERENCE + "." + UserEvent.Reference.PROPERTY_INDEX, reference.getIndex()))
                .filter(QueryBuilders.termQuery(UserEvent.PROPERTY_REFERENCE + "." + UserEvent.Reference.PROPERTY_TYPE, reference.getType()));
        if (StringUtils.isNotBlank(reference.getId())) {
            boolQuery.filter(QueryBuilders.termQuery(UserEvent.PROPERTY_REFERENCE + "." + UserEvent.Reference.PROPERTY_ID, reference.getId()));
        }
        if (StringUtils.isNotBlank(reference.getHash())) {
            boolQuery.filter(QueryBuilders.termQuery(UserEvent.PROPERTY_REFERENCE + "." + UserEvent.Reference.PROPERTY_HASH, reference.getHash()));
        }
        if (StringUtils.isNotBlank(reference.getAnchor())) {
            boolQuery.filter(QueryBuilders.termQuery(UserEvent.PROPERTY_REFERENCE + "." + UserEvent.Reference.PROPERTY_ANCHOR, reference.getAnchor()));
        }

        searchRequest.setQuery(QueryBuilders.nestedQuery(UserEvent.PROPERTY_REFERENCE, QueryBuilders.constantScoreQuery(boolQuery)));

        // Execute query
        try {
            SearchResponse response = searchRequest.execute().actionGet();

            int bulkSize = pluginSettings.getIndexBulkSize();
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            // Read query result
            long counter = 0;
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                bulkRequest.add(
                        client.prepareDelete(INDEX, EVENT_TYPE, searchHit.getId())
                );
                counter++;

                // Flush the bulk if not empty
                if ((counter % bulkSize) == 0) {
                    client.flushDeleteBulk(INDEX, EVENT_TYPE, bulkRequest);
                    bulkRequest = client.prepareBulk();
                }
            }

            // last flush
            client.flushDeleteBulk(INDEX, EVENT_TYPE, bulkRequest);
        }
        catch(SearchPhaseExecutionException e) {
            // Failed or no item on index
            logger.error(String.format("Error while deleting by reference: %s. Skipping deletions.", e.getMessage()), e);
        }
    }


    private UserProfile getUserProfileOrNull(String pubkey, String... fieldnames) {
        return client.getSourceByIdOrNull(UserService.INDEX, UserService.PROFILE_TYPE, pubkey, UserProfile.class, fieldnames);
    }

    private String toJson(UserEvent userEvent) {
        return toJson(userEvent, false);
    }

    private String toJson(UserEvent userEvent, boolean cleanHashAndSignature) {
        try {
            String json = objectMapper.writeValueAsString(userEvent);
            if (cleanHashAndSignature) {
                json = JacksonUtils.removeAttribute(json, Record.PROPERTY_SIGNATURE);
                json = JacksonUtils.removeAttribute(json, Record.PROPERTY_HASH);
            }
            return json;
        } catch(JsonProcessingException e) {
            throw new TechnicalException("Unable to serialize UserEvent object", e);
        }
    }

    @Override
    public String getId() {
        return "duniter.user.event";
    }

    @Override
    public Collection<ChangeSource> getChangeSources() {
        return CHANGE_LISTEN_SOURCES;
    }

    @Override
    public void onChange(ChangeEvent change) {

        try {

            switch (change.getOperation()) {
                // on create
                case CREATE:
                    if (change.getSource() != null) {
                        UserEvent event = objectMapper.readValue(change.getSource().streamInput(), UserEvent.class);
                        processEventCreate(change.getId(), event);
                    }
                    break;

                // on update
                case INDEX:
                    if (change.getSource() != null) {
                        UserEvent event = objectMapper.readValue(change.getSource().streamInput(), UserEvent.class);
                        processEventUpdate(change.getId(), event);
                    }
                    break;

                // on delete
                case DELETE:
                    // Do not propagate deletion

                    break;
            }

        }
        catch(IOException e) {
            throw new TechnicalException(String.format("Unable to parse received block %s", change.getId()), e);
        }

    }

    private void processEventCreate(final String eventId, final UserEvent event) {

        event.setId(eventId);

        // Notify listeners
        threadPool.schedule(() -> {
            synchronized (LISTENERS) {
                LISTENERS.values().forEach(listener -> {
                    if (event.getRecipient().equals(listener.getPubkey())) {
                        listener.onEvent(event);
                    }
                });
            }
        });

    }

    private void processEventUpdate(final String eventId, final UserEvent event) {

        // Skip if user has already read the event
        if (StringUtils.isNotBlank(event.getReadSignature())) {
            if (this.trace) logger.trace("Updated event already read: Skip propagation to listeners");
            return;
        }

        processEventCreate(eventId, event);
    }

}
