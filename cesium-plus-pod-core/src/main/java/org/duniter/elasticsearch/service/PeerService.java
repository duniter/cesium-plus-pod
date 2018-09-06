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
import com.google.common.collect.Lists;
import org.duniter.core.client.dao.PeerDao;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.local.NetworkService;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

import java.util.List;

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
    private List<String> includeEndpointApis = Lists.newArrayList(
            EndpointApi.BASIC_MERKLED_API.name(),
            EndpointApi.BMAS.name(),
            EndpointApi.WS2P.name());

    @Inject
    public PeerService(Duniter4jClient client, PluginSettings settings, ThreadPool threadPool,
                       CryptoService cryptoService, PeerDao peerDao,
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
    }

    public PeerService addIncludeEndpointApi(String api) {
        Preconditions.checkNotNull(api);
        if (!includeEndpointApis.contains(api)) {
            includeEndpointApis.add(api);
        }
        return this;
    }

    public PeerService addIncludeEndpointApi(EndpointApi api) {
        Preconditions.checkNotNull(api);
        addIncludeEndpointApi(api.name());
        return this;
    }

    public PeerService indexPeers(Peer peer) {

        try {
            // Get the blockchain name from node
            BlockchainParameters parameter = blockchainRemoteService.getParameters(peer);
            if (parameter == null) {
                logger.error(I18n.t("duniter4j.es.networkService.indexPeers.remoteParametersError", peer));
                return this;
            }
            String currencyName = parameter.getCurrency();

            indexPeers(currencyName, peer);

        } catch(Exception e) {
            logger.error("Error during indexAllPeers: " + e.getMessage(), e);
        }

        return this;
    }

    public PeerService indexPeers(String currencyName, Peer firstPeer) {
        long timeStart = System.currentTimeMillis();

        try {
            logger.info(I18n.t("duniter4j.es.networkService.indexPeers.task", currencyName, firstPeer));

            // Default filter
            org.duniter.core.client.service.local.NetworkService.Filter filterDef = new org.duniter.core.client.service.local.NetworkService.Filter();
            filterDef.filterType = null;
            filterDef.filterStatus = Peer.PeerStatus.UP;
            filterDef.filterEndpoints = ImmutableList.copyOf(includeEndpointApis);

            // Default sort
            org.duniter.core.client.service.local.NetworkService.Sort sortDef = new org.duniter.core.client.service.local.NetworkService.Sort();
            sortDef.sortType = null;

            List<Peer> peers = networkService.getPeers(firstPeer, filterDef, sortDef, threadPool.scheduler());
            delegate.save(currencyName, peers, true);
            logger.info(I18n.t("duniter4j.es.networkService.indexPeers.succeed", currencyName, firstPeer, peers.size(), (System.currentTimeMillis() - timeStart)));
        } catch(Exception e) {
            logger.error("Error during indexBlocksFromNode: " + e.getMessage(), e);
        }

        return this;
    }

    public void listenAndIndexPeers(final Peer mainPeer) {
        // Get the blockchain name from node
        BlockchainParameters parameter = blockchainRemoteService.getParameters(mainPeer);
        if (parameter == null) {
            logger.error(I18n.t("duniter4j.es.networkService.indexPeers.remoteParametersError", mainPeer));
            return;
        }
        String currencyName = parameter.getCurrency();

        // Default filter
        NetworkService.Filter filterDef = new NetworkService.Filter();
        filterDef.filterType = null;
        filterDef.filterStatus = Peer.PeerStatus.UP;
        filterDef.filterEndpoints = ImmutableList.copyOf(includeEndpointApis);
        filterDef.currency = currencyName;

        // Default sort
        NetworkService.Sort sortDef = new NetworkService.Sort();
        sortDef.sortType = null;

        networkService.addPeersChangeListener(mainPeer,
                peers -> logger.debug(String.format("[%s] Update peers: %s found", currencyName, CollectionUtils.size(peers))),
                filterDef, sortDef, true /*autoreconnect*/, threadPool.scheduler());
    }

    public Long getMaxLastUpTime(String currencyId) {
        return peerDao.getMaxLastUpTime(currencyId);
    }
}
