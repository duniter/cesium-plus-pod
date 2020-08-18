package org.duniter.elasticsearch.service;

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


import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.dao.PeerDao;
import org.duniter.core.client.model.bma.*;
import org.duniter.core.client.model.local.Currency;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.model.local.Peers;
import org.duniter.core.client.service.HttpService;
import org.duniter.core.client.service.bma.NetworkRemoteService;
import org.duniter.core.client.service.exception.HttpBadRequestException;
import org.duniter.core.client.util.KnownBlocks;
import org.duniter.core.client.util.KnownCurrencies;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.http.InetAddressUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.CurrencyExtendDao;
import org.duniter.elasticsearch.threadpool.ScheduledActionFuture;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Benoit on 30/03/2015.
 */
public class NetworkService extends AbstractService {

    private static final BlockchainBlock DEFAULT_BLOCK = KnownBlocks.getFirstBlock(KnownCurrencies.G1);

    private CurrencyExtendDao currencyDao;
    private BlockchainService blockchainService;
    private Map<String, NetworkPeering> peeringByCurrencyCache = Maps.newHashMap();

    // API where to sendBlock the peer document
    private final static Set<String> registeredPeeringTargetedApis = Sets.newHashSet();
    // API to include inside the peer document
    private final static Set<String> registeredPeeringPublishedApis = Sets.newHashSet();

    private final ThreadPool threadPool;
    private final PeerDao peerDao;
    private HttpService httpService;
    private NetworkRemoteService networkRemoteService;
    private PeerService peerService;
    private final boolean debug;
    private Cache<String, List<NetworkPeers.Peer>> peerAsBmaFormatCache;

    @Inject
    public NetworkService(Duniter4jClient client,
                          PluginSettings settings,
                          CryptoService cryptoService,
                          CurrencyDao currencyDao,
                          PeerDao peerDao,
                          BlockchainService blockchainService,
                          PeerService peerService,
                          ThreadPool threadPool,
                          final ServiceLocator serviceLocator
    ) {
        super("duniter.network", client, settings, cryptoService);
        this.peerDao = peerDao;
        this.currencyDao = (CurrencyExtendDao)currencyDao;
        this.blockchainService = blockchainService;
        this.peerService = peerService;
        this.threadPool = threadPool;
        this.peerAsBmaFormatCache = CacheBuilder.newBuilder()
                .expireAfterWrite(pluginSettings.getPeeringInterval() / 2, TimeUnit.SECONDS)
                .build();
        this.debug = logger.isDebugEnabled();
        threadPool.scheduleOnStarted(() -> {
            this.httpService = serviceLocator.getHttpService();
            this.networkRemoteService = serviceLocator.getNetworkRemoteService();
            setIsReady(true);
        });

        // Register ES_CORE_API as published API, inside the peering document
        registerPeeringPublishApi(pluginSettings.getCoreEnpointApi());

        // Register ES_CORE_API as target API, for peering document
        registerPeeringTargetApi(pluginSettings.getCoreEnpointApi());
    }

    public List<Peer> getConfigIncludesPeers(final String currencyId) {
        return getConfigIncludesPeers(currencyId, null);
    }

    public List<Peer> getConfigIncludesPeers(final String currencyId, final String api) {
        Preconditions.checkNotNull(currencyId);
        String[] endpoints = pluginSettings.getSynchroIncludesEndpoints();
        if (ArrayUtils.isEmpty(endpoints)) return null;

        List<Peer> peers = Lists.newArrayList();
        for (String endpoint: endpoints) {
            try {
                String[] endpointPart = endpoint.split(":");
                if (endpointPart.length > 2) {
                    logger.warn(String.format("Error in config: Unable to parse P2P endpoint [%s]: %s", endpoint));
                }
                String epCurrencyId = (endpointPart.length == 2) ? endpointPart[0] : null /*optional*/;

                NetworkPeering.Endpoint ep = (endpointPart.length == 2) ?
                        Endpoints.parse(endpointPart[1]).orElse(null) :
                        Endpoints.parse(endpoint).orElse(null);
                if (ep != null && (api == null || api.equals(ep.api)) && (epCurrencyId == null || epCurrencyId.equals(currencyId))) {
                    Peer peer = Peer.newBuilder()
                            .setEndpoint(ep)
                            .setCurrency(currencyId)
                            .build();

                    String hash = cryptoService.hash(peer.computeKey());
                    peer.setHash(hash);
                    peer.setId(hash);

                    peers.add(peer);
                }

            } catch (IOException e) {
                if (debug) {
                    logger.warn(String.format("Unable to parse P2P endpoint [%s]: %s", endpoint, e.getMessage()), e);
                }
                else {
                    logger.warn(String.format("Unable to parse P2P endpoint [%s]: %s", endpoint, e.getMessage()));
                }
            }
        }
        return peers;
    }

    public boolean hasSomePeers(Set<String> peerApiFilters) {

        Set<String> currencyIds = currencyDao.getAllIds();
        if (CollectionUtils.isEmpty(currencyIds)) return false;

        for (String currencyId: currencyIds) {
            boolean hasSome = peerDao.hasPeersUpWithApi(currencyId, peerApiFilters);
            if (hasSome) return true;
        }

        return false;
    }

    public boolean waitPeersReady(Set<String> peerApiFilters) throws InterruptedException{

        waitReady();

        final int sleepTime = 30 * 1000 /*30s*/;

        int maxWaitingDuration = 5 * 6 * sleepTime; // 5 min
        int waitingDuration = 0;
        while (!isReady() && !hasSomePeers(peerApiFilters)) {
            // Wait
            Thread.sleep(sleepTime);
            waitingDuration += sleepTime;
            if (waitingDuration >= maxWaitingDuration) {
                logger.warn(String.format("Could not start to publish peering. No Peer found (after waiting %s min).", waitingDuration/60/1000));
                return false; // stop here
            }
        }

        // Wait again, to make sure all peers have been saved by NetworkService
        Thread.sleep(sleepTime*2);

        return true;
    }

    public Collection<Peer> getPeersFromApis(final String currencyId, final Collection<String> apis) {

        return apis.stream().flatMap(api -> getPeersFromApi(currencyId, api).stream()).collect(Collectors.toList());
    }

    public Collection<Peer> getPeersFromApi(final String currencyId, final String api) {
        Preconditions.checkNotNull(api);
        Preconditions.checkArgument(StringUtils.isNotBlank(currencyId));

        try {

            // Use map by URL, to avoid duplicated peer
            Map<String, Peer> peersByUrls = Maps.newHashMap();

            // Get peers from config
            List<Peer> configPeers = getConfigIncludesPeers(currencyId, api);
            if (CollectionUtils.isNotEmpty(configPeers)) {
                configPeers.forEach(p -> peersByUrls.put(p.getUrl(), p));
            }

            // Get peers by pubkeys, from config
            String[] includePubkeys = pluginSettings.getSynchroIncludesPubkeys();
            if (ArrayUtils.isNotEmpty(includePubkeys)) {

                // Get from DAO, by API and pubkeys
                List<Peer> pubkeysPeers = peerDao.getPeersByCurrencyIdAndApiAndPubkeys(currencyId, api, includePubkeys);
                if (CollectionUtils.isNotEmpty(pubkeysPeers)) {
                    pubkeysPeers.stream()
                            .filter(Objects::nonNull)
                            .forEach(p -> peersByUrls.put(p.getUrl(), p));
                }
            }

            // Add discovered peers
            if (pluginSettings.enableSynchroDiscovery()) {
                List<Peer> discoveredPeers = peerDao.getPeersByCurrencyIdAndApi(currencyId, api);
                if (CollectionUtils.isNotEmpty(discoveredPeers)) {
                    discoveredPeers.stream()
                            .filter(Objects::nonNull)
                            .forEach(p -> peersByUrls.put(p.getUrl(), p));
                }
            }

            return peersByUrls.values();
        }
        catch (Exception e) {
            logger.error(String.format("Could not get peers for Api [%s]", api), e);
            return null;
        }
    }


    public List<NetworkPeers.Peer> getPeersAsBmaFormatWithCache(String currency) throws ExecutionException {
        final String safeCurrency = this.blockchainService.safeGetCurrency(currency);
        return peerAsBmaFormatCache.get(safeCurrency, () -> this.getPeersAsBmaFormat(safeCurrency));
    }

    public List<NetworkPeers.Peer> getPeersAsBmaFormat(String currency) {

        // Retrieve the currency to use
        currency = blockchainService.safeGetCurrency(currency);

        final List<Peer> endpointsAsPeer = Lists.newArrayList();
        try {

            // Discovery enable: use index '/<currency>/peer'
            if (pluginSettings.enableSynchroDiscovery()) {
                endpointsAsPeer.addAll(peerDao.getUpPeersByCurrencyId(currency, null));
            }

            // Discovery disable, so get it from Duniter node
            else {
                List<Peer> configIncludedPeers = getConfigIncludesPeers(currency);
                if (CollectionUtils.isNotEmpty(configIncludedPeers)) {
                    configIncludedPeers.stream()
                            // Update config peers, by calling /network/peering
                            .map(this::updatePeering)
                            // Filter on UP peers
                            .filter(Peers::isReacheable)
                            .forEach(peer -> endpointsAsPeer.add(peer));
                }
            }

            // Add the pod itself
            if (StringUtils.isNotBlank(pluginSettings.getClusterRemoteHost())) {
                NetworkPeering peering = getPeering(currency, true);
                if (peering != null) {
                    List<Peer> clusterEndpoints = Stream.of(peering.getEndpoints())
                        .map(ep -> Peer.newBuilder()
                                    .setDns(pluginSettings.getClusterRemoteHost())
                                    .setPort(pluginSettings.getClusterRemotePort())
                                    .setUseSsl(pluginSettings.getClusterRemoteUseSsl())
                                    .setEndpoint(ep)
                                    .setPubkey(peering.getPubkey())
                                    .setPeering(peering)
                                    .setStats(peering)
                                    .build()
                        ).collect(Collectors.toList());
                    endpointsAsPeer.addAll(clusterEndpoints);
                }
            }

            return Peers.toBmaPeers(endpointsAsPeer);
        }
        catch (Exception e) {
            logger.error("Could not get peers (BMA format)", e);
            return null;
        }

    }

    public List<NetworkWs2pHeads.Head> getWs2pHeads(String currency) {

        // Retrieve the currency to use
        currency = blockchainService.safeGetCurrency(currency);

        List<NetworkWs2pHeads.Head> result = null;
        try {

            // Discovery enable: use index '/<currency>/peer'
            if (pluginSettings.enableSynchroDiscovery()) {
                result = peerDao.getWs2pPeersByCurrencyId(currency, null);
            }

            // Discovery disable, so get it from Duniter node
            else {
                List<Peer> configIncludedPeers = getConfigIncludesPeers(currency);

                if (CollectionUtils.isNotEmpty(configIncludedPeers)) {

                    result = configIncludedPeers.stream()
                            // Update config peers, by calling /network/peering
                            .map(this::updatePeering)
                            .filter(Peers::isReacheable)
                            // Convert peer into WS2P head
                            // FIXME: miss message & signature fields !
                            .map(Peers::toWs2pHead).collect(Collectors.toList())
                        ;
                }
            }
            return result;
        }
        catch (Exception e) {
            logger.error("Could not get peers (BMA format)", e);
            return result;
        }

    }


    public boolean isEsNodeAliveAndValid(Peer peer, String... excludePubkeys) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        // Make sure the peer's currency is indexed by the cluster
        Currency currency = currencyDao.getById(peer.getCurrency());
        if (currency == null) {
            logger.debug(String.format("[%s] [%s] Peer used a unknown currency", peer.getCurrency(), peer));
            return false;
        }

        // Make sure peer is not self
        try {
            NetworkPeering peering = httpService.executeRequest(peer, String.format("/%s/network/peering", peer.getCurrency()), NetworkPeering.class);

            // Exclude given pubkeys
            if (peering != null && peering.getPubkey() != null && excludePubkeys != null && Arrays.asList(excludePubkeys).contains(peering.getPubkey())) {
                logger.debug(String.format("[%s] [%s] Same pubkey as node's. Skipping synchronization (seems to be self)", peer.getCurrency(), peer));
                return false;
            }

            // Make sure to update pubkey, because can changed!
            peer.setPubkey(peering.getPubkey());

            // Peer is alive, fine !
            return true;
        }
        catch(Exception e) {
            // Unable to check peering, so continue
        }

        try {
            // TODO: check version is compatible
            //String version = networkService.getVersion(peer);

            BlockchainBlock block = httpService.executeRequest(peer, String.format("/%s/block/0/_source", peer.getCurrency()), BlockchainBlock.class);

            return Objects.equals(block.getCurrency(), peer.getCurrency()) &&
                    Objects.equals(block.getSignature(), currency.getFirstBlockSignature());

        }
        catch(Exception e) {
            logger.debug(String.format("[%s] [%s] Peer not alive or invalid: %s", peer.getCurrency(), peer, e.getMessage()));
            return false;
        }
    }

    public void registerPeeringTargetApi(EndpointApi api) {
        Preconditions.checkNotNull(api);
        registerPeeringTargetApi(api.name());
    }

    public void registerPeeringTargetApi(String api) {
        Preconditions.checkNotNull(api);

        if (!registeredPeeringTargetedApis.contains(api)) {
            if (pluginSettings.enablePeering() && CollectionUtils.isEmpty(pluginSettings.getPeeringTargetedApis())) {
                logger.info(String.format("Adding {%s} as target endpoint for the peering document.", api));
            }
            registeredPeeringTargetedApis.add(api);
        }
    }

    public Set<String> getPeeringTargetedApis() {
        Set<String> configOverride = pluginSettings.getPeeringTargetedApis();
        if (CollectionUtils.isNotEmpty(configOverride)) {
            return configOverride;
        }

        return registeredPeeringTargetedApis;
    }

    public void registerPeeringPublishApi(String api) {
        Preconditions.checkNotNull(api);

        if (!registeredPeeringPublishedApis.contains(api)) {
            if (pluginSettings.enablePeering() && CollectionUtils.isEmpty(pluginSettings.getPeeringPublishedApis())) {
                logger.info(String.format("Adding {%s} as published endpoint, inside the peering document.", api));
            }
            registeredPeeringPublishedApis.add(api);
        }
    }

    public void registerPeeringPublishApi(EndpointApi api) {
        Preconditions.checkNotNull(api);
        registerPeeringPublishApi(api.name());
    }

    public Set<String> getPeeringPublishedApis() {
        Set<String> configOverride = pluginSettings.getPeeringPublishedApis();
        if (CollectionUtils.isNotEmpty(configOverride)) {
            return configOverride;
        }

        return registeredPeeringPublishedApis;
    }

    public NetworkPeering getPeering(String currency, boolean useCache) {

        waitReady();

        // Retrieve the currency to use
        currency = blockchainService.safeGetCurrency(currency);

        // Get result from cache, if enable
        if (useCache) {
            NetworkPeering result = peeringByCurrencyCache.get(currency);
            if (result != null) return result;
        }

        // create and fill a new peering object
        NetworkPeering result = new NetworkPeering();

        // Get current block
        BlockchainBlock currentBlock = pluginSettings.enableBlockchainIndexation() ? blockchainService.getCurrentBlock(currency) : null;
        if (currentBlock == null) {
            currentBlock = DEFAULT_BLOCK;
            currency = currentBlock.getCurrency();
        }

        result.setVersion(Integer.valueOf(Protocol.PEER_VERSION));
        result.setCurrency(currency);
        result.setBlock(String.format("%s-%s", currentBlock.getNumber(), currentBlock.getHash()));
        result.setPubkey(pluginSettings.getNodePubkey());
        result.setStatus("UP");

        // Add endpoints
        Collection<String> publishedApis = getPeeringPublishedApis();
        if (CollectionUtils.isNotEmpty(publishedApis)) {
            List<NetworkPeering.Endpoint> endpoints = publishedApis.stream()
                    .map(endpointApi -> {
                        NetworkPeering.Endpoint ep = new NetworkPeering.Endpoint();
                        ep.setDns(pluginSettings.getClusterRemoteHost());
                        ep.setApi(endpointApi);
                        ep.setPort(pluginSettings.getClusterRemotePort());
                        return ep;
                    })
                    .collect(Collectors.toList());
            result.setEndpoints(endpoints.toArray(new NetworkPeering.Endpoint[endpoints.size()]));
        }
        else {
            logger.warn("No endpoint apis to publish inside peering document.");
        }

        // Compute raw, then sign it
        String raw = result.toString();
        String signature = cryptoService.sign(raw, pluginSettings.getNodeKeypair().getSecKey());
        raw += signature + "\n";

        result.setRaw(raw);
        result.setSignature(signature);

        // Add result to cache
        peeringByCurrencyCache.put(currency, result);

        return result;
    }

    public NetworkPeering getLastPeering(String currency) {
        return getPeering(currency, true);
    }

    public Optional<ScheduledActionFuture<?>> startPublishingPeerDocumentToNetwork() {

        if (CollectionUtils.isEmpty(getPeeringPublishedApis())) {
            logger.debug("Skipping peer document publishing (No endpoint API to publish)");
            return Optional.empty();
        }
        if (CollectionUtils.isEmpty(getPeeringTargetedApis())) {
            logger.debug("Skipping peer document publishing (No endpoint API to target)");
            return Optional.empty();
        }

        final ScheduledActionFuture future = new ScheduledActionFuture(null);

        // Launch once, at startup (after a delay)
        future.setDelegate(threadPool.schedule(() -> {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("Publishing endpoints %s to targeted peers %s", getPeeringPublishedApis(), getPeeringTargetedApis()));
            }
            boolean launchAtStartup;
            try {
                // wait for some peers
                launchAtStartup = waitPeersReady(getPeeringTargetedApis());
            } catch (InterruptedException e) {
                return; // stop
            }

            if (launchAtStartup) {
                publishPeerDocumentToNetwork();
            }

            // Schedule next execution
            future.setDelegate(
                    threadPool.scheduleAtFixedRate(
                    this::publishPeerDocumentToNetwork,
                    pluginSettings.getPeeringInterval(),
                    pluginSettings.getPeeringInterval(),
                    TimeUnit.SECONDS));
        },
        30, TimeUnit.SECONDS // wait 30s
        ));

        return Optional.of(future);
    }

    public NetworkPeering checkAndSavePeering(String currency, String peeringDocument) {

        if (!isReady()) throw new IllegalStateException("Could not save peering document (service is not started)");

        Preconditions.checkNotNull(peeringDocument);
        NetworkPeering peering;
        try {
            peering = NetworkPeerings.parse(peeringDocument);
        }
        catch(Exception e) {
            throw new TechnicalException("Invalid peer document: " + e.getMessage(), e);
        }

        // Check validity then save
        return checkAndSavePeering(currency, peering);

    }

    public NetworkPeering checkAndSavePeering(String currency, NetworkPeering peering) {

        // Check currency
        if (peering.getCurrency() == null) {
            throw new TechnicalException("Invalid document: missing currency");
        }

        // Skip if no endpoints
        if (ArrayUtils.isEmpty(peering.getEndpoints())) {
            logger.debug("Ignoring peer document (no endpoint to process)");
            return peering;
        }

        // Retrieve the currency to use
        currency = blockchainService.safeGetCurrency(currency);

        // Check currency
        if (!peering.getCurrency().equals(currency)) {
            throw new TechnicalException(String.format("Invalid document currency. Expected '%s', but found '%s'.", currency, peering.getCurrency()));
        }

        // Check signature
        checkSignature(peering);

        // Transform endpoint to peers
        List<Peer> peers = Lists.newArrayList();
        for (NetworkPeering.Endpoint ep : peering.getEndpoints()) {
            Peer peer = Peer.newBuilder()
                    .setCurrency(peering.getCurrency())
                    .setPubkey(peering.getPubkey())
                    .setEndpoint(ep)
                    .build();

            Peer.Stats stats = new Peer.Stats();
            peer.setStats(stats);

            String blockStamp = peering.getBlock();
            if (StringUtils.isNotBlank(blockStamp)) {
                String[] blockParts = blockStamp.split("-");
                stats.setBlockNumber(Integer.parseInt(blockParts[0]));
                stats.setBlockHash(blockParts[1]);
            }

            // If not a local address, add to the list
            if (InetAddressUtils.isNotLocalAddress(peer.getHost())) {
                peers.add(peer);
            }
        }

        // Save peers (if not empty)
        if (CollectionUtils.isNotEmpty(peers)) {
            peerService.save(peering.getCurrency(), peers, true, true);
        }

        return peering;
    }

    /* -- protected -- */

    public void checkSignature(NetworkPeering peering) {
        Preconditions.checkNotNull(peering);
        Preconditions.checkNotNull(peering.getRaw());
        Preconditions.checkNotNull(peering.getPubkey());

        // Get unsigned raw document
        String unsignedRaw = peering.getRaw();
        if (unsignedRaw == null) {
            unsignedRaw = peering.toUnsignedRaw();
        }

        // Check signature
        if (!cryptoService.verify(unsignedRaw, peering.getSignature(), peering.getPubkey())) {
            throw new TechnicalException("Invalid document signature");
        }
    }

    protected void publishPeerDocumentToNetwork() {
        Set<String> currencyIds;
        try {
            currencyIds = currencyDao.getAllIds();
        }
        catch (Exception e) {
            logger.error("Could not retrieve indexed currencies", e);
            currencyIds = null;
        }
        if (CollectionUtils.isEmpty(currencyIds)) {
            logger.warn("Skipping publication of peer document (no indexed currency)");
            return;
        }

        Set<String> targetedApis = getPeeringTargetedApis();
        Set<String> publishedApis = getPeeringTargetedApis();

        if (CollectionUtils.isEmpty(targetedApis) ||
            CollectionUtils.isEmpty(publishedApis)) {
            logger.warn("Skipping the publication of peer document (no targeted API, or no API to publish)");
            return;
        }

        Peer clusterPeer = pluginSettings.getClusterPeer().orElse(null);

        // For each currency
        currencyIds.forEach(currencyId -> {

            logger.debug(String.format("[%s] Publishing peer document to network... {peers discovery: %s}", currencyId, pluginSettings.enableSynchroDiscovery()));

            // Create a new peer document (will add it to cache)
            NetworkPeering peeringDocument = getPeering(currencyId, false/*force new peering*/);

            if (peeringDocument != null) {

                // Get peers for targeted APIs
                Collection<Peer> peers = getPeersFromApis(currencyId, targetedApis);

                // Exclude peers when equals to the current cluster
                if (CollectionUtils.isNotEmpty(peers) && clusterPeer != null) {
                    peers = peers.stream().filter(p -> !pluginSettings.sameAsClusterPeer(p))
                            .collect(Collectors.toList());
                }

                if (CollectionUtils.isNotEmpty(peers)) {
                    // Send document to every peers
                    long count = peers.stream().map(p -> this.safePublishPeerDocumentToPeer(currencyId, p, peeringDocument.toString())).filter(Boolean.TRUE::equals).count();

                    logger.info(String.format("[%s] Peering document sent to %s/%s peers", currencyId, count, peers.size()));
                } else {
                    logger.debug(String.format("[%s] Peering document not published to network: no peers with targeted apis %s", currencyId, targetedApis));
                }
            }
        });
    }

    protected boolean safePublishPeerDocumentToPeer(final String currencyId, final Peer peer, final String peerDocument) {
        Preconditions.checkNotNull(currencyId);
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getApi());
        Preconditions.checkNotNull(peer.getUrl());
        Preconditions.checkNotNull(peerDocument);

        try {
            if (logger.isDebugEnabled()) logger.debug(String.format("[%s] [%s] Sending peer document...", currencyId, peer));

            networkRemoteService.postPeering(peer, peerDocument);
            return true;
        }
        catch(HttpBadRequestException e) {
            // Document already known = OK
            if (e.getCode() == 1015) return true;

            if (logger.isDebugEnabled()) logger.debug(String.format("[%s] [%s] Error when sending peer document: %s", currencyId, peer, e.getMessage()));
            return false;
        }
        catch(Exception e) {
            if (logger.isDebugEnabled()) logger.debug(String.format("[%s] [%s] Error when sending peer document: %s", currencyId, peer, e.getMessage()));
            return false;
        }

    }

    protected Peer updatePeering(Peer peer)  {
        if (!isReady()) throw new TechnicalException("Node is not ready yet. Skipping peering update");

        try {
            NetworkPeering peering = networkRemoteService.getPeering(peer);
            Peers.setPeering(peer, peering);
            Peers.setStats(peer, peering);
        } catch(Exception e) {
            logger.error(String.format("[%s] Error while getting peering document: %s", peer, e.getMessage()), e);
            peer.getStats().setStatus(Peer.PeerStatus.DOWN);
        }
        return peer;
    }


}
