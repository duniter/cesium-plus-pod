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
import org.duniter.elasticsearch.model.SynchroResult;
import org.duniter.elasticsearch.synchro.SynchroActionResult;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.Message;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nuiton.i18n.I18n;

import java.io.IOException;

/**
 * Created by Benoit on 30/03/2015.
 */
public class UserInvitationService extends AbstractService {

    public static final String INDEX = "invitation";
    public static final String CERTIFICATION_TYPE = "certification";

    private final UserEventService userEventService;

    @Inject
    public UserInvitationService(Duniter4jClient client, PluginSettings settings,
                                 CryptoService cryptoService,
                                 UserEventService userEventService) {
        super("duniter." + INDEX, client, settings, cryptoService);
        this.userEventService = userEventService;
    }

    /**
     * Delete blockchain index, and all data
     * @throws JsonProcessingException
     */
    public UserInvitationService deleteIndex() {
        client.deleteIndexIfExists(INDEX);
        return this;
    }

    /**
     * Create index need for blockchain mail, if need
     */
    public UserInvitationService createIndexIfNotExists() {
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
    public UserInvitationService createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s/%s]", INDEX, CERTIFICATION_TYPE));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(INDEX);
        Settings indexSettings = Settings.settingsBuilder()
                .put("number_of_shards", 2)
                .put("number_of_replicas", 1)
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(CERTIFICATION_TYPE, createCertificationType());
        createIndexRequestBuilder.execute().actionGet();

        return this;
    }

    public String indexCertificationInvitationFromJson(String recordJson) {

        JsonNode source = readAndVerifyIssuerSignature(recordJson);

        // Check time is valid - fix #27
        verifyTimeForInsert(source);

        if (logger.isDebugEnabled()) {
            String issuer = getMandatoryField(source, Message.PROPERTY_ISSUER).asText();
            logger.debug(String.format("Indexing a invitation to certify from issuer [%s]", issuer.substring(0, 8)));
        }

        IndexResponse response = client.prepareIndex(INDEX, CERTIFICATION_TYPE)
                .setSource(recordJson)
                .setRefresh(false)
                .execute().actionGet();

        String invitationId = response.getId();

        // Notify user
        notifyUser(invitationId, source);

        return invitationId;
    }

    public void notifyUser(String id, JsonNode source) {
        String issuer = getMandatoryField(source, Message.PROPERTY_ISSUER).asText();
        String recipient = getMandatoryField(source, Message.PROPERTY_RECIPIENT).asText();
        Long time = getMandatoryField(source, Message.PROPERTY_TIME).asLong();

        // Notify recipient
        userEventService.notifyUser(UserEvent.newBuilder(UserEvent.EventType.INFO, UserEventCodes.INVITATION_TO_CERTIFY.name())
                .setRecipient(recipient)
                .setMessage(I18n.n("duniter.user.event.INVITATION_TO_CERTIFY"), issuer, ModelUtils.minifyPubkey(issuer))
                .setTime(time)
                .setReference(INDEX, CERTIFICATION_TYPE, id)
                .build());
    }

    /* -- Internal methods -- */

    public XContentBuilder createCertificationType() {
        return createMapping(CERTIFICATION_TYPE);
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

                    // content (encrypted)
                    .startObject("content")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    // comment (encrypted)
                    .startObject("comment")
                    .field("type", "string")
                    .field("index", "not_analyzed")
                    .endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while getting mapping for index [%s/%s]: %s", INDEX, CERTIFICATION_TYPE, ioe.getMessage()), ioe);
        }
    }
}
