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
import com.google.common.collect.ImmutableSet;
import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.service.AbstractBlockchainListenerService;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.DocumentReference;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.TimeValue;
import org.nuiton.i18n.I18n;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benoit on 30/03/2015.
 */
public class BlockchainUserEventService extends AbstractBlockchainListenerService  {

    public static final UserProfile EMPTY_PROFILE = new UserProfile();
    public static final String DEFAULT_PUBKEYS_SEPARATOR = ", ";

    private final UserService userService;
    private final UserEventService userEventService;

    @Inject
    public BlockchainUserEventService(Duniter4jClient client, PluginSettings pluginSettings, CryptoService cryptoService,
                                      ThreadPool threadPool,
                                      UserService userService,
                                      UserEventService userEventService) {
        super("duniter.user.event.blockchain", client, pluginSettings.getDelegate(), cryptoService, threadPool,
                new TimeValue(500, TimeUnit.MILLISECONDS),
                pluginSettings.enableBlockchainUserEventIndexation());
        this.userService = userService;
        this.userEventService = userEventService;
    }


    @Override
    protected void processBlockIndex(ChangeEvent change) {

        BlockchainBlock block = readBlock(change);

        // First: Delete old events on same block
        {
            DocumentReference reference = new DocumentReference(change.getIndex(), BlockchainService.BLOCK_TYPE, change.getId());
            this.bulkRequest = userEventService.addDeleteEventsByReferenceToBulk(reference, this.bulkRequest, this.bulkSize, false);
            flushBulk();
        }

        // Joiners
        if (CollectionUtils.isNotEmpty(block.getJoiners())) {
            for (BlockchainBlock.Joiner joiner: block.getJoiners()) {
                notifyUserEvent(block, joiner.getPublicKey(), UserEventCodes.MEMBER_JOIN, I18n.n("duniter.user.event.MEMBER_JOIN"), block.getCurrency());
            }
        }

        // Actives
        if (CollectionUtils.isNotEmpty(block.getActives())) {
            for (BlockchainBlock.Joiner active: block.getActives()) {
                notifyUserEvent(block, active.getPublicKey(), UserEventCodes.MEMBER_ACTIVE, I18n.n("duniter.user.event.MEMBER_ACTIVE"), block.getCurrency());
            }
        }

        // Leavers
        if (CollectionUtils.isNotEmpty(block.getLeavers())) {
            for (BlockchainBlock.Joiner leaver: block.getJoiners()) {
                notifyUserEvent(block, leaver.getPublicKey(), UserEventCodes.MEMBER_LEAVE, I18n.n("duniter.user.event.MEMBER_LEAVE"), block.getCurrency());
            }
        }

        // Revoked
        if (CollectionUtils.isNotEmpty(block.getRevoked())) {
            for (BlockchainBlock.Revoked revoked: block.getRevoked()) {
                notifyUserEvent(block, revoked.getPubkey(), UserEventCodes.MEMBER_REVOKE, I18n.n("duniter.user.event.MEMBER_REVOKE"), block.getCurrency());
            }
        }

        // Excluded
        if (CollectionUtils.isNotEmpty(block.getExcluded())) {
            for (String excluded: block.getExcluded()) {
                notifyUserEvent(block, excluded, UserEventCodes.MEMBER_EXCLUDE, I18n.n("duniter.user.event.MEMBER_EXCLUDE"), block.getCurrency());
            }
        }

        // Tx
        if (CollectionUtils.isNotEmpty(block.getTransactions())) {
            for (BlockchainBlock.Transaction tx: block.getTransactions()) {
                processTx(block, tx);
            }
        }

        // Certifications
        if (CollectionUtils.isNotEmpty(block.getCertifications())) {
            for (BlockchainBlock.Certification cert: block.getCertifications()) {
                processCertification(block, cert);
            }
        }

        flushBulkRequestOrSchedule();
    }

    @Override
    protected void processBlockDelete(ChangeEvent change) {

        DocumentReference reference = new DocumentReference(change.getIndex(), BlockchainService.BLOCK_TYPE, change.getId());

        if (change.getSource() != null) {
            BlockchainBlock block = readBlock(change);
            reference.setHash(block.getHash());
        }

        this.bulkRequest = userEventService.addDeleteEventsByReferenceToBulk(reference, this.bulkRequest, this.bulkSize, false);
        flushBulkRequestOrSchedule();
    }

    /* -- internal method -- */

    private void processTx(BlockchainBlock block, BlockchainBlock.Transaction tx) {
        Set<String> issuers = ImmutableSet.copyOf(tx.getIssuers());


        // Collect receivers
        Set<String> receivers = new HashSet<>();
        for (String output : tx.getOutputs()) {
            String[] parts = output.split(":");
            if (parts.length >= 3 && parts[2].startsWith("SIG(")) {
                String receiver = parts[2].substring(4, parts[2].length() - 1);
                if (!issuers.contains(receiver) && !receivers.contains(receiver)) {
                    receivers.add(receiver);
                }
            }
        }

        Map<String, UserProfile> issuerProfiles = userService.getProfilesByPubkey(issuers, UserProfile.PROPERTY_TITLE, UserProfile.PROPERTY_LOCALE);
        Map<String, UserProfile> receiverProfiles = userService.getProfilesByPubkey(receivers, UserProfile.PROPERTY_TITLE, UserProfile.PROPERTY_LOCALE);

        // Emit TX_RECEIVED events
        if (CollectionUtils.isNotEmpty(issuers)) {
            String issuerNames = userService.joinNamesFromProfiles(issuers, issuerProfiles, DEFAULT_PUBKEYS_SEPARATOR, true);
            String issuersAsString = ModelUtils.joinPubkeys(issuers, DEFAULT_PUBKEYS_SEPARATOR, false);
            for (String receiver : receivers) {
                UserProfile receiverProfile = receiverProfiles.get(receiver);
                String receiverLocale = receiverProfile != null ? receiverProfile.getLocale() : null;
                notifyUserEvent(block, receiver, receiverLocale, UserEventCodes.TX_RECEIVED, I18n.n("duniter.user.event.TX_RECEIVED"), issuersAsString, issuerNames);
            }
        }


        // Emit TX_SENT events
        if (CollectionUtils.isNotEmpty(receivers)) {
            String receiverNames = userService.joinNamesFromProfiles(receivers, receiverProfiles, DEFAULT_PUBKEYS_SEPARATOR, true);
            String receiversAsString = ModelUtils.joinPubkeys(receivers, DEFAULT_PUBKEYS_SEPARATOR, false);
            for (String issuer : issuers) {
                UserProfile issuerProfile = issuerProfiles.get(issuer);
                String issuerLocale = issuerProfile != null ? issuerProfile.getLocale() : null;
                notifyUserEvent(block, issuer, issuerLocale, UserEventCodes.TX_SENT, I18n.n("duniter.user.event.TX_SENT"), receiversAsString, receiverNames);
            }
        }



    }

    private void processCertification(BlockchainBlock block, BlockchainBlock.Certification certification) {
        String issuer = certification.getFromPubkey();
        UserProfile issuerProfile = userService.getProfileByPubkey(issuer).orElse(EMPTY_PROFILE);

        String receiver = certification.getToPubkey();
        UserProfile receiverProfile = userService.getProfileByPubkey(receiver).orElse(EMPTY_PROFILE);

        // Received
        String issuerName = StringUtils.isNotBlank(issuerProfile.getTitle()) ? issuerProfile.getTitle() : ModelUtils.minifyPubkey(issuer);
        notifyUserEvent(block, receiver, receiverProfile.getLocale(), UserEventCodes.CERT_RECEIVED,
                I18n.n("duniter.user.event.CERT_RECEIVED"), issuer, issuerName);

        // Sent
        String receiverName = StringUtils.isNotBlank(receiverProfile.getTitle()) ? receiverProfile.getTitle() : ModelUtils.minifyPubkey(receiver);
        notifyUserEvent(block, issuer, issuerProfile.getLocale(), UserEventCodes.CERT_SENT,
                I18n.n("duniter.user.event.CERT_SENT"),
                receiver, receiverName);
    }
    private void notifyUserEvent(BlockchainBlock block, String pubkey,
                                 UserEventCodes code,
                                 String message,
                                 String... params) {
        notifyUserEvent(block, pubkey, null/*will read the locale from the profile, if any*/, code, message, params);
    }

    private void notifyUserEvent(BlockchainBlock block, String pubkey,
                                 @Nullable String locale,
                                 UserEventCodes code,
                                 String message,
                                 String... params) {
        Preconditions.checkNotNull(block);
        Preconditions.checkNotNull(pubkey);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(code);

        UserEvent event = UserEvent.newBuilder(UserEvent.EventType.INFO, code.name())
                .setRecipient(pubkey)
                .setMessage(message, params)
                .setTime(block.getMedianTime())
                .setReference(block.getCurrency(), BlockchainService.BLOCK_TYPE, String.valueOf(block.getNumber()))
                .setReferenceHash(block.getHash())
                .build();

        event = locale == null ? userEventService.fillUserEvent(event) : userEventService.fillUserEvent(new Locale(locale), event) ;

        try {
            bulkRequest.add(client.prepareIndex(UserEventService.INDEX, UserEventService.EVENT_TYPE)
                    .setSource(getObjectMapper().writeValueAsBytes(event))
                    .setRefresh(false));

            // Flush if need
            if (bulkRequest.numberOfActions() % bulkSize == 0) {
                flushBulk();
            }
        }
        catch(JsonProcessingException e) {
            logger.error("Could not serialize UserEvent into JSON: " + e.getMessage(), e);
        }
    }


}
