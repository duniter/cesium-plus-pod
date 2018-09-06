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
import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.InvalidSignatureException;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.Message;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Benoit on 30/03/2015.
 */
public class MessageService extends AbstractService {

    public static final String INDEX = "message";
    public static final String INBOX_TYPE = "inbox";
    public static final String OUTBOX_TYPE = "outbox";

    @Deprecated
    public static final String RECORD_TYPE = "record";


    private final UserEventService userEventService;

    @Inject
    public MessageService(Duniter4jClient client, PluginSettings settings,
                          CryptoService cryptoService, UserEventService userEventService) {
        super("duniter." + INDEX, client, settings, cryptoService);
        this.userEventService = userEventService;
    }

    /**
     * Delete blockchain index, and all data
     * @throws JsonProcessingException
     */
    public MessageService deleteIndex() {
        client.deleteIndexIfExists(INDEX);
        return this;
    }

    public boolean existsIndex() {
        return client.existsIndex(INDEX);
    }

    /**
     * Create index need for blockchain mail, if need
     */
    public MessageService createIndexIfNotExists() {
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
    public MessageService createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s/%s]", INDEX, INBOX_TYPE));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
        Settings indexSettings = Settings.settingsBuilder()
                .put("number_of_shards", 2)
                .put("number_of_replicas", 1)
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(INBOX_TYPE, createInboxType());
        createIndexRequestBuilder.addMapping(OUTBOX_TYPE, createOutboxType());
        createIndexRequestBuilder.execute().actionGet();

        return this;
    }

    public String indexInboxFromJson(String recordJson) {

        JsonNode actualObj = readAndVerifyIssuerSignature(recordJson);
        String issuer = getIssuer(actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a message from issuer [%s]", issuer.substring(0, 8)));
        }

        IndexResponse response = client.prepareIndex(INDEX, INBOX_TYPE)
                .setSource(recordJson)
                .setRefresh(false)
                .execute().actionGet();

        String messageId = response.getId();

        // Notify new message
        notifyUser(messageId, actualObj);

        return messageId;
    }

    public String indexOuboxFromJson(String recordJson) {

        JsonNode source = readAndVerifyIssuerSignature(recordJson);

        if (logger.isDebugEnabled()) {
            String issuer = getMandatoryField(source, Message.PROPERTY_ISSUER).asText();
            logger.debug(String.format("Indexing a message from issuer [%s]", issuer.substring(0, 8)));
        }

        IndexResponse response = client.prepareIndex(INDEX, OUTBOX_TYPE)
                .setSource(recordJson)
                .setRefresh(false)
                .execute().actionGet();

        return response.getId();
    }

    public void notifyUser(String messageId, JsonNode actualObj) {
        String issuer = getMandatoryField(actualObj, Message.PROPERTY_ISSUER).asText();
        String recipient = getMandatoryField(actualObj, Message.PROPERTY_RECIPIENT).asText();
        Long time = getMandatoryField(actualObj, Message.PROPERTY_TIME).asLong();

        // Notify recipient
        userEventService.notifyUser(UserEvent.newBuilder(UserEvent.EventType.INFO, UserEventCodes.MESSAGE_RECEIVED.name())
                .setRecipient(recipient)
                .setMessage(I18n.n("duniter.user.event.MESSAGE_RECEIVED"), issuer, ModelUtils.minifyPubkey(issuer))
                .setTime(time)
                .setReference(INDEX, INBOX_TYPE, messageId)
                .build());
    }

    public void markMessageAsRead(String id, String signature) {
        Map<String, Object> fields = client.getMandatoryFieldsById(INDEX, INBOX_TYPE, id, Message.PROPERTY_HASH, Message.PROPERTY_RECIPIENT);
        String recipient = fields.get(UserEvent.PROPERTY_RECIPIENT).toString();
        String hash = fields.get(UserEvent.PROPERTY_HASH).toString();

        // Check signature
        boolean valid = cryptoService.verify(hash, signature, recipient);
        if (!valid) {
            throw new InvalidSignatureException("Invalid signature: only the recipient can mark an message as read.");
        }

        UpdateRequestBuilder request = client.prepareUpdate(INDEX, INBOX_TYPE, id)
                .setDoc("read_signature", signature);
        request.execute();
    }

    /* -- Internal methods -- */

    public XContentBuilder createInboxType() {
        return createMapping(INBOX_TYPE);
    }

    public XContentBuilder createOutboxType() {
        return createMapping(OUTBOX_TYPE);
    }

    public XContentBuilder createMapping(String typeName) {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(typeName)
                    .startObject("properties")

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

                    // nonce
                    .startObject("nonce")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // title (encrypted)
                    .startObject("title")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // content (encrypted)
                    .startObject("content")
                    .field("type", "string")
                    .field("index", "not_analyzed")
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
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", INDEX, INBOX_TYPE, ioe.getMessage()), ioe);
        }
    }
}
