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
import org.duniter.core.util.websocket.WebsocketClientEndpoint;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.service.AbstractBlockchainListenerService;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.TimeValue;
import org.nuiton.i18n.I18n;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benoit on 30/03/2015.
 */
public class BlockchainUserEventService extends AbstractBlockchainListenerService  {

    public static final String DEFAULT_PUBKEYS_SEPARATOR = ", ";

    private final UserService userService;
    private final UserEventService userEventService;
    private final AdminService adminService;

    @Inject
    public BlockchainUserEventService(Duniter4jClient client, PluginSettings settings, CryptoService cryptoService,
                                      ThreadPool threadPool,
                                      BlockchainService blockchainService,
                                      UserService userService,
                                      AdminService adminService,
                                      UserEventService userEventService) {
        super("duniter.user.event.blockchain", client, settings.getDelegate(), cryptoService, threadPool,
                new TimeValue(500, TimeUnit.MILLISECONDS));
        this.userService = userService;
        this.adminService = adminService;
        this.userEventService = userEventService;

        if (this.enable) {
            blockchainService.registerConnectionListener(createConnectionListeners());
        }
    }


    @Override
    protected void processBlockIndex(ChangeEvent change) {

        BlockchainBlock block = readBlock(change);

        // First: Delete old events on same block
        {
            UserEvent.Reference reference = new UserEvent.Reference(change.getIndex(), BlockchainService.BLOCK_TYPE, change.getId());
            this.bulkRequest = userEventService.addDeleteEventsByReferenceToBulk(reference, this.bulkRequest, this.bulkSize, false);
            flushBulkRequestOrSchedule();
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
    }

    @Override
    protected void processBlockDelete(ChangeEvent change) {

        UserEvent.Reference reference = new UserEvent.Reference(change.getIndex(), BlockchainService.BLOCK_TYPE, change.getId());

        if (change.getSource() != null) {
            BlockchainBlock block = readBlock(change);
            reference.setHash(block.getHash());
        }

        this.bulkRequest = userEventService.addDeleteEventsByReferenceToBulk(reference, this.bulkRequest, this.bulkSize, false);
        flushBulkRequestOrSchedule();
    }

    /* -- internal method -- */

    /**
     * Create a listener that notify admin when the Duniter node connection is lost or retrieve
     */
    private WebsocketClientEndpoint.ConnectionListener createConnectionListeners() {
        return new WebsocketClientEndpoint.ConnectionListener() {
            private boolean errorNotified = false;

            @Override
            public void onSuccess() {
                // Send notify on reconnection
                if (errorNotified) {
                    errorNotified = false;
                    adminService.notifyAdmin(UserEvent.newBuilder(UserEvent.EventType.INFO, UserEventCodes.NODE_BMA_UP.name())
                            .setMessage(I18n.n("duniter.user.event.NODE_BMA_UP"),
                                    pluginSettings.getNodeBmaHost(),
                                    String.valueOf(pluginSettings.getNodeBmaPort()),
                                    pluginSettings.getClusterName())
                            .build());
                }
            }

            @Override
            public void onError(Exception e, long lastTimeUp) {
                if (errorNotified) return; // already notify

                // Wait 1 min, then notify admin (once)
                long now = System.currentTimeMillis() / 1000;
                boolean wait = now - lastTimeUp < 60;
                if (!wait) {
                    errorNotified = true;
                    adminService.notifyAdmin(UserEvent.newBuilder(UserEvent.EventType.ERROR, UserEventCodes.NODE_BMA_DOWN.name())
                            .setMessage(I18n.n("duniter.user.event.NODE_BMA_DOWN"),
                                    pluginSettings.getNodeBmaHost(),
                                    String.valueOf(pluginSettings.getNodeBmaPort()),
                                    pluginSettings.getClusterName(),
                                    String.valueOf(lastTimeUp))
                            .build());
                }
            }
        };
    }




    private void processTx(BlockchainBlock block, BlockchainBlock.Transaction tx) {
        Set<String> senders = ImmutableSet.copyOf(tx.getIssuers());

        // Received
        String senderNames = userService.joinNamesFromPubkeys(senders, DEFAULT_PUBKEYS_SEPARATOR, true);
        String sendersPubkeys = ModelUtils.joinPubkeys(senders, DEFAULT_PUBKEYS_SEPARATOR, false);
        Set<String> receivers = new HashSet<>();
        for (String output : tx.getOutputs()) {
            String[] parts = output.split(":");
            if (parts.length >= 3 && parts[2].startsWith("SIG(")) {
                String receiver = parts[2].substring(4, parts[2].length() - 1);
                if (!senders.contains(receiver) && !receivers.contains(receiver)) {
                    notifyUserEvent(block, receiver, UserEventCodes.TX_RECEIVED, I18n.n("duniter.user.event.TX_RECEIVED"), sendersPubkeys, senderNames);
                    receivers.add(receiver);
                }
            }
        }

        // Sent
        if (CollectionUtils.isNotEmpty(receivers)) {
            String receiverNames = userService.joinNamesFromPubkeys(receivers, DEFAULT_PUBKEYS_SEPARATOR, true);
            String receiverPubkeys = ModelUtils.joinPubkeys(receivers, DEFAULT_PUBKEYS_SEPARATOR, false);
            for (String sender : senders) {
                notifyUserEvent(block, sender, UserEventCodes.TX_SENT, I18n.n("duniter.user.event.TX_SENT"), receiverPubkeys, receiverNames);
            }
        }

    }

    private void processCertification(BlockchainBlock block, BlockchainBlock.Certification certification) {
        String sender = certification.getFromPubkey();
        String receiver = certification.getToPubkey();

        // Received
        String senderName = userService.getProfileTitle(sender);
        if (senderName == null) {
            senderName = ModelUtils.minifyPubkey(sender);
        }
        notifyUserEvent(block, receiver, UserEventCodes.CERT_RECEIVED, I18n.n("duniter.user.event.CERT_RECEIVED"), sender, senderName);

        // Sent
        String receiverName = userService.getProfileTitle(receiver);
        if (receiverName == null) {
            receiverName = ModelUtils.minifyPubkey(receiver);
        }
        notifyUserEvent(block, sender, UserEventCodes.CERT_SENT, I18n.n("duniter.user.event.CERT_SENT"), receiver, receiverName);
    }

    private void notifyUserEvent(BlockchainBlock block, String pubkey, UserEventCodes code, String message, String... params) {
        UserEvent event = UserEvent.newBuilder(UserEvent.EventType.INFO, code.name())
                .setRecipient(pubkey)
                .setMessage(message, params)
                .setTime(block.getMedianTime())
                .setReference(block.getCurrency(), BlockchainService.BLOCK_TYPE, String.valueOf(block.getNumber()))
                .setReferenceHash(block.getHash())
                .build();

        event = userEventService.fillUserEvent(event);

        try {
            bulkRequest.add(client.prepareIndex(UserEventService.INDEX, UserEventService.EVENT_TYPE)
                    .setSource(getObjectMapper().writeValueAsBytes(event))
                    .setRefresh(false));
            flushBulkRequestOrSchedule();
        }
        catch(JsonProcessingException e) {
            logger.error("Could not serialize UserEvent into JSON: " + e.getMessage(), e);
        }
    }


}
