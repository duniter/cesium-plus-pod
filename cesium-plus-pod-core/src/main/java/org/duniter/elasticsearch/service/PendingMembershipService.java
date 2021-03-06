package org.duniter.elasticsearch.service;

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
import org.apache.commons.lang3.mutable.MutableInt;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.bma.WotPendingMembership;
import org.duniter.core.client.model.bma.WotRequirements;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.WotRemoteService;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.CurrencyExtendDao;
import org.duniter.elasticsearch.dao.PendingMembershipDao;
import org.duniter.elasticsearch.dao.SaveResult;
import org.duniter.elasticsearch.threadpool.ScheduledActionFuture;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.util.bytes.JsonNodeBytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Benoit on 10/01/2019.
 */
public class PendingMembershipService extends AbstractService  {


    private ThreadPool threadPool;
    private boolean indexing = false;
    private PeerService peerService;
    private PendingMembershipDao pendingMembershipDao;
    private final CurrencyExtendDao currencyDao;
    private WotRemoteService wotRemoteService;

    @Inject
    public PendingMembershipService(Duniter4jClient client, PluginSettings pluginSettings, CryptoService cryptoService,
                                    ThreadPool threadPool,
                                    CurrencyDao currencyDao,
                                    PendingMembershipDao pendingMembershipDao,
                                    PeerService peerService,
                                    final ServiceLocator serviceLocator) {
        super("duniter.core", client, pluginSettings, cryptoService);

        this.threadPool = threadPool;
        this.pendingMembershipDao = pendingMembershipDao;
        this.currencyDao = (CurrencyExtendDao)currencyDao;
        this.peerService = peerService;

        threadPool.scheduleOnStarted(() -> {
            this.wotRemoteService = serviceLocator.getWotRemoteService();
            setIsReady(true);
        });
    }


    public ScheduledActionFuture<?> startScheduling() {
        return threadPool.scheduleAtFixedRate(this::safeIndexAllCurrencies,
                // start in 5 min
                5,
                // Then every 30 minutes
                30, TimeUnit.MINUTES);
    }


    public List<WotPendingMembership> getPendingMemberships(String currency, int from, int size) {
        final String currencyId = safeGetCurrency(currency);

        int startIndex = Math.max(0, from);
        int fixSize = Math.max(0, size);

        // Index is enable: use dao
        if (pluginSettings.enablePendingMembershipIndexation()) {

            List<WotPendingMembership> memberships = pendingMembershipDao.getPendingMemberships(currencyId, from, fixSize);
            return memberships;
        }
        else {
            List<WotPendingMembership> res = wotRemoteService.getPendingMemberships(currencyId);

            if (startIndex > 0 || res.size() > fixSize) {
                return startIndex < res.size() ?
                        res.subList(startIndex, Math.min(startIndex + fixSize, res.size() -1)) :
                        null;
            }
            return res;
        }
    }


    public PendingMembershipService indexFromPeer(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());
        Preconditions.checkArgument(isReady(), "Cannot index pending memberships (service not started yet).");

        String currencyId = peer.getCurrency();
        long startTimeMs = System.currentTimeMillis();
        logger.info(String.format("[%s] [%s] Indexing pending memberships...", currencyId, peer));

        // Get pending memberships from peer
        List<WotPendingMembership> memberships = wotRemoteService.getPendingMemberships(peer);
        if (CollectionUtils.isEmpty(memberships)) {
            logger.info(String.format("[%s] [%s] Indexing pending memberships [OK] 0 pending membership", currencyId, peer));
            return this;
        }

        // Do save in index
        SaveResult result = pendingMembershipDao.save(currencyId, memberships);

        // TODO: load requirements for each membership
//        for (WotPendingMembership ms: memberships) {
//            try {
//                XContentBuilder sourceRef = getMembershipWithRequirements(peer, ms, om, requestCounter);
//
//                bulkRequest.add(client.prepareIndex(currencyId, "pending")
//                        .setSource(sourceRef.bytes()));
//                counter++;
//
//                // Flush if need
//                if (bulkRequest.numberOfActions() % bulkSize == 0) {
//                    client.flushBulk(bulkRequest);
//                    bulkRequest = client.prepareBulk();
//                }
//
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }
//        });
//
//        client.flushBulk(bulkRequest);

        logger.info(String.format("[%s] [%s] Indexing pending memberships [OK] %s in %s ms", peer.getCurrency(), peer,
                result.toString(),
                System.currentTimeMillis() - startTimeMs));

        return this;
    }

    /* -- internal method -- */


    protected void safeIndexAllCurrencies() {
        if (!isReady()) {
            logger.debug("Cannot schedule pending memberships (service not started yet). Skipping.");
            return;
        }
        if (indexing) {
            logger.debug("Already indexing pending memberships. Skipping.");
            return;
        }

        synchronized(this) {
            indexing = true;

            try {
                indexAllCurrencies();
            }
            catch(Throwable t) {
                // Log, then continue
                logger.error("Failed to index pending memberships: " + t.getMessage(), t);
            }
            finally {
                indexing = false;
            }
        }
    }

    protected void indexAllCurrencies() {
        Preconditions.checkArgument(isReady());

        Set<String> currencyIds;
        try {
            currencyIds = currencyDao.getAllIds();
        }
        catch (Exception e) {
            logger.error("Failed to load indexed currencies. Skipping pending memberships update.", e);
            return;
        }

        if (CollectionUtils.isEmpty(currencyIds)) {
            logger.debug("Unable to update pending memberships: no currency indexed.");
            return;
        }

        // For each currencies: update pendings
        currencyIds.forEach(this::indexByCurrency);

    }

    protected void indexByCurrency(String currency) {
        final String currencyId = safeGetCurrency(currency);

        // Choose a UP peer randomly
        Peer peer = getRandomUpPeerWithBma(currencyId);
        if (peer == null) {
            logger.info(String.format("[%s] Indexing pending memberships [Skipped] - No peer found", currencyId));
            return;
        }
        peer.setCurrency(currencyId);

        // Run the indexation
        indexFromPeer(peer);
    }


    public Peer getRandomUpPeerWithBma(String currencyId) {

        List<Peer> peers = peerService.getUpPeersByApis(currencyId, EndpointApi.BASIC_MERKLED_API, EndpointApi.BMAS)
                .stream()
                .filter(peer -> peer.getStats() != null && peer.getStats().isMainConsensus())
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(peers)) {
            return null;
        }

        int randomIndex = (int)Math.floor(Math.random() * CollectionUtils.size(peers));
        return peers.get(randomIndex);
    }

    protected XContentBuilder getMembershipWithRequirements(Peer peer,
                                                               WotPendingMembership membership,
                                                               ObjectMapper objectMapper,
                                                               MutableInt requestCounter) throws IOException {

        final XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                .field(WotPendingMembership.PROPERTY_PUBKEY, membership.getPubkey())
                .field(WotPendingMembership.PROPERTY_UID, membership.getUid())
                .field(WotPendingMembership.PROPERTY_VERSION, membership.getVersion())
                .field(WotPendingMembership.PROPERTY_CURRENCY, membership.getCurrency())
                .field(WotPendingMembership.PROPERTY_MEMBERSHIP, membership.getMembership())
                .field(WotPendingMembership.PROPERTY_BLOCK_NUMBER, membership.getBlockNumber())
                .field(WotPendingMembership.PROPERTY_BLOCK_HASH, membership.getBlockHash())
                .field(WotPendingMembership.PROPERTY_WRITTEN, membership.getWritten());

        int MAX_REQUEST_PER_URI = 10;

        // Load requirements identities
        List<WotRequirements> identities = null;
        int retryCounter = 0;
        do {

            try {
                identities = wotRemoteService.getRequirements(peer, membership.getPubkey());
                requestCounter.increment();
            }
            // Error can occur because of quota on BMA api
            catch (Exception e) {
                MAX_REQUEST_PER_URI = Math.min(MAX_REQUEST_PER_URI, requestCounter.getValue());
                requestCounter.setValue(0); // Make sure to wait
            }
            if (requestCounter.getValue() == 0 || requestCounter.getValue() % MAX_REQUEST_PER_URI == 0) {
                logger.debug(String.format("[%s] [%s] Waiting 10s and retry...", peer.getCurrency(), peer));
                try {
                    Thread.sleep(20 * 1000);
                }
                catch (InterruptedException e2) {
                    retryCounter = 100; // Stop
                }
            }
        } while(identities == null && retryCounter < 10);

        // Add requirements identities
        if (CollectionUtils.isNotEmpty(identities)) {

            content.startArray("requirements");
            for (WotRequirements identity : identities) {
                if (Objects.equals(membership.getPubkey(), identity.getPubkey()) && Objects.equals(membership.getUid(), identity.getUid())) {
                    content.rawValue(new JsonNodeBytesReference(identity, objectMapper));
                }
            }
            content.endArray();
        }

        content.endObject();

        return content;
    }

    /**
     * Return the given currency, or the default currency
     * @param currency
     * @return
     */
    protected String safeGetCurrency(String currency) {
        if (StringUtils.isNotBlank(currency)) return currency;
        return currencyDao.getDefaultId();
    }
}
