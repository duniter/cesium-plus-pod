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


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.duniter.core.client.dao.PeerDao;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.local.NetworkService;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

import java.io.Closeable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Benoit on 30/03/2015.
 */
public class PeerService extends AbstractService  {

    private org.duniter.core.client.service.bma.BlockchainRemoteService blockchainRemoteService;
    private org.duniter.core.client.service.local.NetworkService networkService;
    private org.duniter.core.client.service.local.PeerService delegate;
    private PeerDao peerDao;
    private ThreadPool threadPool;

    // Define endpoint API to include
    // API to include inside when getting peers
    private final static Set<String> indexedEndpointApis = Sets.newHashSet();

    @Inject
    public PeerService(Duniter4jClient client, PluginSettings settings, ThreadPool threadPool,
                       CryptoService cryptoService, org.duniter.core.client.dao.PeerDao peerDao,
                       final ServiceLocator serviceLocator){
        super("duniter.network.peer", client, settings, cryptoService);
        this.threadPool = threadPool;
        this.peerDao = peerDao;
        threadPool.scheduleOnStarted(() -> {
            this.blockchainRemoteService = serviceLocator.getBlockchainRemoteService();
            this.networkService = serviceLocator.getNetworkService();
            this.delegate = serviceLocator.getPeerService();
            setIsReady(true);
        });

        // If filtered API defined in settings, use it
        Collection<EndpointApi> peerIndexedApis = pluginSettings.getPeerIndexedApis();
        if (CollectionUtils.isNotEmpty(peerIndexedApis)) {
            addAllPeerIndexedEndpointApis(peerIndexedApis);
        }
    }

    public PeerService addIndexedEndpointApi(String api) {
        Preconditions.checkNotNull(api);
        if (!indexedEndpointApis.contains(api)) indexedEndpointApis.add(api);
        return this;
    }

    public PeerService addIndexedEndpointApi(EndpointApi api) {
        Preconditions.checkNotNull(api);
        addIndexedEndpointApi(api.name());
        return this;
    }

    public PeerService setCurrencyMainPeer(String currency, Peer peer) {
        delegate.setCurrencyMainPeer(currency, peer);
        return this;
    }

    public String getCurrency(Peer peer) {
        if (peer.getCurrency() != null) return peer.getCurrency();

        // Get the blockchain name from node
        BlockchainParameters parameter = blockchainRemoteService.getParameters(peer);
        if (parameter == null) {
            throw new TechnicalException(I18n.t("duniter4j.es.networkService.indexPeers.remoteParametersError", peer));
        }
        String currency = parameter.getCurrency();
        peer.setCurrency(currency);

        return currency;

    }

    public PeerService indexPeers(Peer peer) {
        Preconditions.checkNotNull(peer);

        try {
            indexPeers(getCurrency(peer), peer);
        } catch(Exception e) {
            logger.error("Error during indexPeers: " + e.getMessage(), e);
        }

        return this;
    }

    public PeerService indexPeers(String currency, Peer firstPeer) {
        long timeStart = System.currentTimeMillis();

        try {
            logger.info(I18n.t("duniter4j.es.networkService.indexPeers.task", currency, firstPeer));


            // Default filter
            NetworkService.Filter filterDef = getDefaultFilter(currency);

            Number currentNumber = client.getTypedFieldById(currency, BlockDao.TYPE, "current", BlockchainBlock.PROPERTY_NUMBER);
            if (currentNumber != null) {
                filterDef.minBlockNumber = currentNumber.intValue() - 100;
            }

            // Default sort
            org.duniter.core.client.service.local.NetworkService.Sort sortDef = new org.duniter.core.client.service.local.NetworkService.Sort();
            sortDef.sortType = null;

            List<Peer> peers = networkService.getPeers(firstPeer, filterDef, sortDef, threadPool.scheduler());

            // Save list
            delegate.save(currency, peers);

            // Set olf peers as Down
            delegate.updatePeersAsDown(currency, filterDef.filterEndpoints);
            logger.info(I18n.t("duniter4j.es.networkService.indexPeers.succeed", currency, firstPeer, peers.size(), (System.currentTimeMillis() - timeStart)));
        } catch(Exception e) {
            logger.error("Error during indexPeers: " + e.getMessage(), e);
        }

        return this;
    }

    public void save(final Peer peer) {
        delegate.save(peer);
    }

    public void save(final String currencyId, final List<Peer> peers, boolean applyFilter, boolean refreshPeers) {
        if (CollectionUtils.isEmpty(peers)) return; // Skip if empty

        // Filter on endpoint apis
        if (applyFilter) {

            // Default filter
            NetworkService.Filter filterDef = getDefaultFilter(currencyId);

            List<Peer> filteredPeers = peers.stream()
                    .filter(networkService.peerFilter(filterDef))
                    .collect(Collectors.toList());
            save(currencyId, filteredPeers, false, refreshPeers); // Loop, without applying filter
            return;
        }

        if (refreshPeers) {
            final Peer mainPeer = pluginSettings.checkAndGetDuniterPeer();

            // Async refresh
            networkService.refreshPeersAsync(mainPeer, peers, threadPool.scheduler())
                    .exceptionally(throwable -> {
                        logger.error("Could not refresh peers status: " + throwable.getMessage(), throwable);
                        return peers;
                    })
                    // then loop, without refreshing
                    .thenAccept(list -> save(currencyId, list, false, false));
            return;
        }

        if (logger.isDebugEnabled()) logger.debug("Saving peers: " + peers.toString());

        delegate.save(currencyId, peers);
    }

    public Closeable listenAndIndexPeers(final Peer mainPeer) {
        Preconditions.checkNotNull(mainPeer);
        String currency = getCurrency(mainPeer);

        // Default filter
        NetworkService.Filter filterDef = new NetworkService.Filter();
        filterDef.filterType = null;
        filterDef.filterStatus = Peer.PeerStatus.UP;
        filterDef.filterEndpoints = ImmutableList.copyOf(indexedEndpointApis);
        filterDef.currency = currency;

        // Default sort
        NetworkService.Sort sortDef = new NetworkService.Sort();
        sortDef.sortType = null;

        // Create a log listener
        NetworkService.PeersChangeListener logListener = (peers) -> {
            // Count peer by API (for log)
            if (logger.isInfoEnabled()) {
                Map<String, Long> peerCountByApi = Maps.newHashMap();
                peers.stream()
                        .filter(Objects::nonNull)
                        .map(Peer::getApi)
                        .filter(StringUtils::isNotBlank)
                        .forEach(api -> {
                            Long counter = peerCountByApi.get(api.toUpperCase());
                            peerCountByApi.put(api.toUpperCase(), (counter != null ? counter : 0) + 1L);
                        });
                logger.info(String.format("[%s] %s peers UP: %s", currency, CollectionUtils.size(peers), peerCountByApi));
            }
        };

        // Start to listen peers
        return networkService.addPeersChangeListener(mainPeer, logListener,
                filterDef, sortDef, true /*autoreconnect*/, threadPool.scheduler());
    }

    public Long getMaxLastUpTime(String currencyId) {
        return peerDao.getMaxLastUpTime(currencyId);
    }

    public List<Peer> getUpPeersByApis(String currencyId, EndpointApi... endpointApis) {
        return Arrays.stream(endpointApis)
                .flatMap(api -> {
                    List<Peer> apiPeers = peerDao.getPeersByCurrencyIdAndApiAndPubkeys(currencyId, api.name(), null);
                    return CollectionUtils.isEmpty(apiPeers) ? Stream.empty() : apiPeers.stream();
                })
                .collect(Collectors.toList());
    }

    /* -- Internal methods -- */

    protected void addAllPeerIndexedEndpointApis(Collection<EndpointApi> apis) {
        Preconditions.checkNotNull(apis);
        apis.forEach(this::addIndexedEndpointApi);
    }

    protected NetworkService.Filter getDefaultFilter(String currency) {
        org.duniter.core.client.service.local.NetworkService.Filter filterDef = new org.duniter.core.client.service.local.NetworkService.Filter();
        filterDef.filterType = null;
        filterDef.filterStatus = Peer.PeerStatus.UP;
        filterDef.filterEndpoints = ImmutableList.copyOf(indexedEndpointApis);
        filterDef.currency = currency;
        return filterDef;
    }
}
