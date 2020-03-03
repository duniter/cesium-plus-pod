package org.duniter.elasticsearch.user.service;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.AccessDeniedException;
import org.duniter.elasticsearch.exception.DuplicatedDocumentException;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.service.WotService;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.dao.profile.UserProfileDao;
import org.duniter.elasticsearch.user.model.*;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Benoit on 30/03/2015.
 */
public class LikeService extends AbstractService {

    public static final String INDEX = "like";
    public static final String RECORD_TYPE = "record";

    static {
        // Reserve i18n
        I18n.n("duniter.user.event.MODERATION_RECEIVED");
        I18n.n("duniter.user.event.LIKE_RECEIVED");
        I18n.n("duniter.user.event.ABUSE_RECEIVED");
        I18n.n("duniter.user.event.STAR_RECEIVED");
        I18n.n("duniter.user.event.FOLLOW_RECEIVED");
    }
    private final AdminService adminService;
    private final UserEventService userEventService;
    private final WotService wotService;

    private final Set<String> reportAbuseIssuerRequirements;
    private final Set<String> likeIssuerRequirements;

    @Inject
    public LikeService(Duniter4jClient client, PluginSettings settings, CryptoService cryptoService,
                       AdminService adminService,
                       UserEventService userEventService,
                       WotService wotService) {
        super("duniter." + INDEX, client, settings, cryptoService);
        this.adminService = adminService;
        this.userEventService = userEventService;
        this.wotService = wotService;
        this.reportAbuseIssuerRequirements = ImmutableSet.copyOf(pluginSettings.getReportAbuseIssuerRequirements()).stream()
                .filter(StringUtils::isNotBlank)
                .map(String::toLowerCase).collect(Collectors.toSet());
        this.likeIssuerRequirements =  ImmutableSet.copyOf(pluginSettings.getLikeIssuerRequirements()).stream()
                .filter(StringUtils::isNotBlank)
                .map(String::toLowerCase).collect(Collectors.toSet());;
    }

    /**
     * Delete blockchain index, and all data
     */
    public LikeService deleteIndex() {
        client.deleteIndexIfExists(INDEX);
        return this;
    }


    public boolean existsIndex() {
        return client.existsIndex(INDEX);
    }

    /**
     * Create index need for blockchain mail, if need
     */
    public LikeService createIndexIfNotExists() {
        try {
            if (!client.existsIndex(INDEX)) {
                createIndex();
            }
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(String.format("Error while creating index [%s]", INDEX));
        }

        return this;
    }

    /**
     * Create index need for category mail
     * @throws JsonProcessingException
     */
    public LikeService createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s/%s]", INDEX, RECORD_TYPE));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
        Settings indexSettings = Settings.settingsBuilder()
                .put("number_of_shards", 2)
                .put("number_of_replicas", 1)
                //.put("analyzer", createDefaultAnalyzer())
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(RECORD_TYPE, createRecordType());
        createIndexRequestBuilder.execute().actionGet();

        return this;
    }


    public String indexLikeFromJson(String recordJson, boolean allowOldDocument) throws IOException{
        JsonNode source;
        try {
            source = getObjectMapper().readTree(recordJson);
        }
        catch(IOException e) {
            throw new InvalidFormatException("Invalid record JSON: " + e.getMessage(), e);
        }

        // Check issuer signature
        String issuer = source.get(LikeRecord.PROPERTY_ISSUER).asText();
        if (StringUtils.isNotBlank(issuer) && !LikeRecord.ANONYMOUS_ISSUER.equalsIgnoreCase(issuer)) {
            readAndVerifyIssuerSignature(recordJson, source, LikeRecord.PROPERTY_ISSUER);
        }

        // Check if valid deletion
        checkIsValidLike(source, allowOldDocument);

        if (logger.isDebugEnabled()) {
            String kind = source.get(LikeRecord.PROPERTY_KIND).asText();
            String index = source.get(LikeRecord.PROPERTY_INDEX).asText();
            String type = source.get(LikeRecord.PROPERTY_TYPE).asText();
            String id = source.get(LikeRecord.PROPERTY_ID).asText();
            String anchor = getOptionalField(source, LikeRecord.PROPERTY_ANCHOR).map(JsonNode::asText).orElse(null);
            if (StringUtils.isBlank(anchor)) {
                logger.debug(String.format("Adding %s on {%s/%s/%s} - issuer {%.8s}", kind, index, type, id, issuer));
            }
            else {
                logger.debug(String.format("Adding %s on {%s/%s/%s} #%s - issuer {%.8s}", kind, index, type, id, anchor, issuer));
            }
        }

        // Add deletion to history
        IndexResponse response = client.prepareIndex(INDEX, RECORD_TYPE)
                .setSource(recordJson)
                .setRefresh(false)
                .execute().actionGet();

        // Notify user and admin
        notifyOnInsert(source);

        return response.getId();
    }

    public void checkIsValidLike(JsonNode actualObj, boolean allowOldDocuments) {
        String index = getMandatoryField(actualObj, LikeRecord.PROPERTY_INDEX).asText();
        String type = getMandatoryField(actualObj,LikeRecord.PROPERTY_TYPE).asText();
        String id = getMandatoryField(actualObj,LikeRecord.PROPERTY_ID).asText();
        String kind = getMandatoryField(actualObj, LikeRecord.PROPERTY_KIND).asText();
        String issuer = actualObj.get(LikeRecord.PROPERTY_ISSUER).asText();
        String anchor = getOptionalField(actualObj, LikeRecord.PROPERTY_ANCHOR).map(JsonNode::asText).orElse(null);
        boolean isAnonymous = StringUtils.isBlank(issuer) || LikeRecord.ANONYMOUS_ISSUER.equals(issuer);

        // Check kind exist in enum
        LikeRecord.Kind kindEnum;
        try {
            kindEnum = LikeRecord.Kind.valueOf(kind);
        } catch(IllegalArgumentException e) {
            throw new InvalidFormatException(String.format("Like kind {%s} not exists. Expected: %s", kind, LikeRecord.Kind.values()));
        }

        // Check star's level
        if (kindEnum == LikeRecord.Kind.STAR) {
            String levelStr = getMandatoryField(actualObj, LikeRecord.PROPERTY_LEVEL).asText();
            Integer level = null;
            try {
                level = Integer.parseInt(levelStr);
            }
            catch(Exception e) {
                // Continue
            }
            if (level == null || level < 0 || level > 5) {
                throw new InvalidFormatException(String.format("Field 'level' must be an int between [0-5]"));
            }
        }

        if (kindEnum == LikeRecord.Kind.ABUSE) {
            // Check abuse is not anonymous
            if (isAnonymous) {
                throw new AccessDeniedException("Anonymous abuse report are not allowed");
            }

            // Check abuse's comment
            String comment = getMandatoryField(actualObj, LikeRecord.PROPERTY_COMMENT).asText();
            if (StringUtils.isBlank(comment.trim())) {
                throw new InvalidFormatException(String.format("Missing required and not blank 'comment', when reporting an abuse"));
            }

        }

        // Check time is valid - fix #27
        verifyTime(actualObj, allowOldDocuments, LikeRecord.PROPERTY_TIME);

        // Check index
        if (MessageService.INDEX.equals(index) || UserInvitationService.INDEX.equals(index)) {
            throw new NotFoundException(String.format("Index {%s/%s} not allow for like feature.", index, type));
        }
        if (!client.existsIndex(index)) {
            throw new NotFoundException(String.format("Index {%s/%s} not exists.", index, type));
        }

        if (!isAnonymous) {

            // Check issuer requirements
            if (!allowOldDocuments) {
                Set<String> requirements = kindEnum == LikeRecord.Kind.ABUSE ? reportAbuseIssuerRequirements : likeIssuerRequirements;
                // Check if issuer was a member
                if (requirements.contains("wasMember") && !wotService.isOrWasMember(issuer)) {
                    throw new AccessDeniedException(String.format("Issuer %s not authorized to send %s data.", issuer, kindEnum.name().toLowerCase()));
                }
                // Check if issuer is a member
                else if (requirements.contains("member") && !wotService.isMember(issuer)) {
                    throw new AccessDeniedException(String.format("Issuer %s not authorized to send %s data.", issuer, kindEnum.name().toLowerCase()));
                }
                // Check if issuer has a profile
                if (requirements.contains("profile")) {
                    try {
                        client.checkDocumentExists(UserService.INDEX, UserProfileDao.TYPE, issuer);
                    }
                    catch(NotFoundException e) {
                        throw new AccessDeniedException(String.format("Issuer %s not authorized to send %s (missing a profile).", issuer,  kindEnum.name().toLowerCase()));
                    }
                }
            }

            // Check user NOT already like this document
            if (existsLikeWithSameIssuer(index, type, id, kindEnum, issuer, anchor)) {
                throw new DuplicatedDocumentException(String.format("Issuer {%.8s} already sent %s for this document", issuer, kind.toLowerCase()));
            }
        }

        // Check anonymous document is valid
        else {
            // Need explicit
            if (StringUtils.isBlank(issuer)) {
                throw new InvalidFormatException("Missing value for 'issuer' field.");
            }

            // Check no signature
            JsonNode signatureNode = actualObj.get(LikeRecord.PROPERTY_SIGNATURE);
            if (signatureNode != null && !signatureNode.isMissingNode()) {
                throw new InvalidFormatException(String.format("Field 'signature' must not be set, on a anonymous %s document.", kind.toLowerCase()));
            }

            // Check NOT same document exists (same hash)
            String hash = getMandatoryField(actualObj, LikeRecord.PROPERTY_HASH).asText();
            if (existsLikeWithSameHash(index, type, id, kindEnum, hash, anchor)) {
                throw new DuplicatedDocumentException(String.format("Duplicated anonymous %s document. Skipping insertion.", kind.toLowerCase()));
            }
        }

        // Check referenced document exists
        if (!allowOldDocuments) {
            client.checkDocumentExists(index, type, id);
        }
    }

    public boolean existsLikeWithSameIssuer(final String index, final String type, final String id,
                                            final LikeRecord.Kind kindEnum,
                                            final String issuer,
                                            final String anchor) {
        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(INDEX)
                .setTypes(RECORD_TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        // Query = filter on index/type/id
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_INDEX, index))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_TYPE, type))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ID, id))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ISSUER, issuer))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_KIND, kindEnum.toString()));
        if (StringUtils.isNotBlank(anchor)) {
            boolQuery.filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ANCHOR, anchor));
        }

        searchRequest.setQuery(QueryBuilders.constantScoreQuery(boolQuery));

        // Execute query
        SearchResponse response = searchRequest.execute().actionGet();
        return response.getHits().getTotalHits() > 0;
    }

    public boolean existsLikeWithSameHash(final String index, final String type, final String id,
                                          final LikeRecord.Kind kindEnum,
                                          final String hash, final String anchor) {
        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(INDEX)
                .setTypes(RECORD_TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        // Query = filter on index/type/id
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_INDEX, index))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_TYPE, type))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ID, id))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ISSUER, LikeRecord.ANONYMOUS_ISSUER))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_KIND, kindEnum.toString()))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_HASH, hash));
        if (StringUtils.isNotBlank(anchor)) {
            boolQuery.filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ANCHOR, anchor));
        }

        searchRequest.setQuery(QueryBuilders.constantScoreQuery(boolQuery));

        // Execute query
        SearchResponse response = searchRequest.execute().actionGet();
        return response.getHits().getTotalHits() > 0;
    }

    public void notifyOnInsert(JsonNode actualObj) {
        String kind = getMandatoryField(actualObj,LikeRecord.PROPERTY_KIND).asText();
        LikeRecord.Kind kindEnum = LikeRecord.Kind.valueOf(kind);

        // If not need notification: skip
        if (kindEnum == LikeRecord.Kind.DISLIKE || kindEnum == LikeRecord.Kind.VIEW) return;

        UserEvent.EventType eventType = kindEnum == LikeRecord.Kind.ABUSE ? UserEvent.EventType.WARN : UserEvent.EventType.INFO;

        String index = getMandatoryField(actualObj, LikeRecord.PROPERTY_INDEX).asText();
        String type = getMandatoryField(actualObj,LikeRecord.PROPERTY_TYPE).asText();
        String id = getMandatoryField(actualObj,LikeRecord.PROPERTY_ID).asText();
        Long time = getMandatoryField(actualObj,LikeRecord.PROPERTY_TIME).asLong();
        String anchor = getOptionalField(actualObj, LikeRecord.PROPERTY_ANCHOR).map(JsonNode::asText).orElse(null);
        String issuer = getMandatoryField(actualObj,LikeRecord.PROPERTY_ISSUER).asText();
        String level = getOptionalField(actualObj,LikeRecord.PROPERTY_LEVEL).map(JsonNode::asText).orElse(null);

        // Load some fields from the original document
        Map<String, Object> docFields = client.getFieldsById(index, type, id, Record.PROPERTY_ISSUER, UserProfile.PROPERTY_TITLE);
        String docIssuer = String.valueOf(docFields.get(Record.PROPERTY_ISSUER));
        String docTitle = Optional.ofNullable(docFields.get(UserProfile.PROPERTY_TITLE)).orElse("?").toString();

        // Notify admin if abuse
        if (kindEnum == LikeRecord.Kind.ABUSE) {
            String comment = getMandatoryField(actualObj,LikeRecord.PROPERTY_COMMENT).asText();
            UserEvent adminEvent = UserEvent.newBuilder(eventType, UserEventCodes.MODERATION_RECEIVED.toString())
                    .setMessage(I18n.n("duniter.user.event.MODERATION_RECEIVED"),
                            // Message params
                            issuer, ModelUtils.minifyPubkey(issuer), docTitle, comment, level
                    )
                    .setTime(time)
                    .setReference(index, type, id)
                    .setReferenceAnchor(anchor)
                    .build();

            adminService.notifyAdmin(adminEvent);
        }

        // Notify the issuer of the document
        String eventCode = kindEnum.toString() + "_RECEIVED";
        UserEvent userEvent = UserEvent.newBuilder(eventType,eventCode )
                .setRecipient(docIssuer)
                .setMessage(I18n.n("duniter.user.event." + eventCode, ModelUtils.minifyPubkey(issuer)),
                        // Message params
                        issuer, ModelUtils.minifyPubkey(issuer), docTitle, level)
                .setTime(time)
                .setReference(index, type, id)
                .setReferenceAnchor(anchor)
                .build();
        userEventService.notifyUser(userEvent);
    }

    public long countByDocumentAndKind(String index, String type, String docId, LikeRecord.Kind kind) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        Preconditions.checkNotNull(StringUtils.isNotBlank(type));
        Preconditions.checkNotNull(StringUtils.isNotBlank(docId));
        Preconditions.checkNotNull(kind);

        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(LikeService.INDEX)
                .setTypes(LikeService.RECORD_TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        // Query = filter on index/type/id
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_INDEX, index))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_TYPE, type))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ID, docId))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_KIND, kind.toString()));

        searchRequest.setQuery(QueryBuilders.constantScoreQuery(boolQuery));

        SearchResponse response = searchRequest
                .setSize(0)
                .get();

        return response.getHits().getTotalHits();
    }

    public Set<String> getIssuersByDocumentAndKind(String index, String type, String docId, LikeRecord.Kind kind) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        Preconditions.checkArgument(StringUtils.isNotBlank(type));
        Preconditions.checkArgument(StringUtils.isNotBlank(docId));
        Preconditions.checkNotNull(kind);

        int size = pluginSettings.getIndexBulkSize();

        // Prepare search request
        SearchRequestBuilder request = client
                .prepareSearch(LikeService.INDEX)
                .setTypes(LikeService.RECORD_TYPE)
                .setFetchSource(false)
                .addFields(LikeRecord.PROPERTY_ISSUER)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setSize(size);

        // Query = filter on index/type/id
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_INDEX, index))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_TYPE, type))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ID, docId))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_KIND, kind.toString()));

        request.setQuery(QueryBuilders.constantScoreQuery(boolQuery));


        // Execute query
        long total = -1;
        int from = 0;
        Set<String> result = Sets.newHashSet();
        do {
            SearchResponse response = client.safeExecuteRequest(request).actionGet();

            // Read query result
            SearchHit[] searchHits = response.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                Map<String, SearchHitField> hitFields = searchHit.getFields();
                SearchHitField issuerField = hitFields.get(LikeRecord.PROPERTY_ISSUER);
                if (issuerField != null) {
                    result.add(issuerField.getValue());
                }
            }
            from += size;
            request.setFrom(from);
            if (total == -1) total = (response.getHits() != null) ? response.getHits().getTotalHits() : 0;
        } while (from < total);

        return result;
    }

    public void deleteAllByReference(final DocumentReference reference) {
        Preconditions.checkNotNull(reference);
        addDeletesByReferenceToBulk(reference, client.prepareBulk(), pluginSettings.getIndexBulkSize(), true);
    }

    /* -- Internal methods -- */


    protected XContentBuilder createRecordType() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(RECORD_TYPE)
                    .startObject("properties")

                    // version
                    .startObject("version")
                    .field("type", "integer")
                    .endObject()

                    // index
                    .startObject("index")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // type
                    .startObject("type")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // id
                    .startObject(LikeRecord.PROPERTY_ID)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // anchor
                    .startObject(LikeRecord.PROPERTY_ANCHOR)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // kind
                    .startObject(LikeRecord.PROPERTY_KIND)
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // issuer
                    .startObject("issuer")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // time
                    .startObject("time")
                    .field("type", "integer")
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

                    // level (e.g. kind is 'star')
                    .startObject(LikeRecord.PROPERTY_LEVEL)
                    .field("type", "integer")
                    .endObject()

                    // comment
                    .startObject(LikeRecord.PROPERTY_COMMENT)
                    .field("type", "string")
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", INDEX, RECORD_TYPE, ioe.getMessage()), ioe);
        }
    }

    public BulkRequestBuilder addDeletesByReferenceToBulk(final DocumentReference reference,
                                                          BulkRequestBuilder bulkRequest,
                                                          final int bulkSize,
                                                          final boolean flushAll) {

        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(INDEX)
                .setTypes(RECORD_TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                // Query = filter on reference
                .setQuery(createQueryForReference(reference));

        // Execute query, while there is some data
        return client.bulkDeleteFromSearch(INDEX, RECORD_TYPE, searchRequest, bulkRequest, bulkSize, flushAll);
    }

    protected ConstantScoreQueryBuilder createQueryForReference(final DocumentReference reference) {
        // Query = filter on reference
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(reference.getIndex())) {
            boolQuery.filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_INDEX, reference.getIndex()));
        }
        if (StringUtils.isNotBlank(reference.getType())) {
            boolQuery.filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_TYPE, reference.getType()));
        }
        if (StringUtils.isNotBlank(reference.getId())) {
            boolQuery.filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ID, reference.getId()));
        }
        if (StringUtils.isNotBlank(reference.getAnchor())) {
            boolQuery.filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ANCHOR, reference.getAnchor()));
        }

        return QueryBuilders.constantScoreQuery(boolQuery);
    }
}
