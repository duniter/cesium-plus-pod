package org.duniter.elasticsearch;

/*
 * #%L
 * Duniter4j :: ElasticSearch Plugin
 * %%
 * Copyright (C) 2014 - 2016 EIS
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

import org.duniter.core.client.model.elasticsearch.Currency;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.Preconditions;
import org.duniter.elasticsearch.dao.*;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.*;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestRequest;

import java.io.Closeable;
import java.util.Optional;

/**
 * Created by blavenie on 17/06/16.
 */
public class PluginInit extends AbstractLifecycleComponent<PluginInit> {

    public static final String CURRENCY_NAME_REGEXP = "[a-zA-Z0-9_-]+";

    private final PluginSettings pluginSettings;
    private final ThreadPool threadPool;
    private final Injector injector;
    private final ESLogger logger;

    @Inject
    public PluginInit(Settings settings, PluginSettings pluginSettings, ThreadPool threadPool, final Injector injector) {
        super(settings);
        this.logger = Loggers.getLogger("duniter.core", settings, new String[0]);
        this.pluginSettings = pluginSettings;
        this.threadPool = threadPool;
        this.injector = injector;
    }

    @Override
    protected void doStart() {

        // Configure HTTP API access rules (run once)
        threadPool.scheduleOnStarted(this::defineHttpAccessRules);

        // First time the node is the master
        threadPool.scheduleOnMasterEachStart(() -> {

            // Create indices (once)
            createIndices();

            // Each time node is the master
            threadPool.scheduleOnClusterReady(() -> {

                // Start blockchain indexation (if enable)
                startIndexBlockchain();

                // Start synchro and peering
                startSynchroAndPeering();

                // Start doc stats
                startDocStatistics();
            });
        });
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() {

    }


    protected void defineHttpAccessRules() {
        // Synchronize blockchain
        if (pluginSettings.enableBlockchainIndexation()) {

            // Add access security rules, for the currency indices
            injector.getInstance(RestSecurityController.class)

                    // Add access to currencies/record index
                    .allowIndexType(RestRequest.Method.GET,
                            CurrencyExtendDao.INDEX,
                            CurrencyExtendDao.RECORD_TYPE)
                    .allowPostSearchIndexType(
                            CurrencyExtendDao.INDEX,
                            CurrencyExtendDao.RECORD_TYPE)

                    // Add access to <currency>/block index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            BlockDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            BlockDao.TYPE)

                    // Add access to <currency>/blockStat index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            BlockStatDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            BlockStatDao.TYPE)

                    // Add access to <currency>/peer index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            PeerDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            PeerDao.TYPE)

                    // Add access to <currency>/movement index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            MovementDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            MovementDao.TYPE)

                    // Add access to <currency>/member index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            MemberDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            MemberDao.TYPE)

                    // Add access to <currency>/synchro index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            SynchroExecutionDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            SynchroExecutionDao.TYPE);
        }

        // Allow scroll search (need by synchro from other peers)
        injector.getInstance(RestSecurityController.class)
                .allow(RestRequest.Method.POST, "^/_search/scroll$")
                .allow(RestRequest.Method.DELETE, "^/_search/scroll$"); // WARN: should NOT authorized -XDELETE /_search/scroll/all
    }

    protected void createIndices() {

        checkMasterNode();

        // Reload All indices
        if (pluginSettings.reloadAllIndices()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Reloading indices...");
            }

            injector.getInstance(CurrencyService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();

            if (pluginSettings.enableDocStats()) {
                injector.getInstance(DocStatService.class)
                        .deleteIndex()
                        .createIndexIfNotExists();
            }

            if (logger.isInfoEnabled()) {
                logger.info("Reloading indices [OK]");
            }
        }

        else if (pluginSettings.enableBlockchainIndexation() && pluginSettings.reloadBlockchainIndices() && pluginSettings.reloadBlockchainIndicesFrom() <= 0) {
            if (logger.isWarnEnabled()) {
                logger.warn("/!\\ Reloading blockchain indices...");
            }
            injector.getInstance(CurrencyService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();

            if (logger.isInfoEnabled()) {
                logger.info("Reloading blockchain indices [OK]");
            }
        }

        else {


            if (logger.isDebugEnabled()) {
                logger.debug("Checking indices...");
            }

            injector.getInstance(CurrencyService.class)
                    .createIndexIfNotExists();

            if (pluginSettings.enableDocStats()) {
                injector.getInstance(DocStatService.class)
                        .createIndexIfNotExists();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Checking indices [OK]");
            }
        }
    }

    protected void checkMasterNode() {
        Preconditions.checkArgument(threadPool.isMasterNode(), "Node must be the master node to execute this job");
    }

    protected void reloadBlockchain(Peer peer, Runnable callback) {

        // Reload the blockchain
        if (pluginSettings.reloadBlockchainIndices()) {

            // If partial reload (from a block)
            if (pluginSettings.reloadBlockchainIndicesFrom() > 0) {
                // Delete blocs range [from,to]
                if (pluginSettings.reloadBlockchainIndicesTo() >= pluginSettings.reloadBlockchainIndicesFrom()) {
                    logger.warn(String.format("[%s] /!\\ Re-indexing blockchain range [%s-%s]...",
                            peer.getCurrency(),
                            pluginSettings.reloadBlockchainIndicesFrom(),
                            pluginSettings.reloadBlockchainIndicesTo()));

                    injector.getInstance(BlockchainService.class)
                            .deleteRange(peer.getCurrency(),
                                    pluginSettings.reloadBlockchainIndicesFrom(),
                                    pluginSettings.reloadBlockchainIndicesTo());
                }
                else {
                    logger.warn(String.format("[%s] /!\\ Re-indexing blockchain from block #%s...", peer.getCurrency(), pluginSettings.reloadBlockchainIndicesFrom()));

                    injector.getInstance(BlockchainService.class)
                            .deleteFrom(peer.getCurrency(), pluginSettings.reloadBlockchainIndicesFrom());
                }
            }

            // Reindex range
            if (pluginSettings.reloadBlockchainIndicesFrom() > 0 &&
                    pluginSettings.reloadBlockchainIndicesTo() >= pluginSettings.reloadBlockchainIndicesFrom()) {

                // Wait cluster finished deletion
                threadPool.scheduleOnClusterReady(() -> {

                    injector.getInstance(BlockchainService.class)
                            .indexBlocksRange(peer,
                                    pluginSettings.reloadBlockchainIndicesFrom(),
                                    pluginSettings.reloadBlockchainIndicesTo());

                    if (callback != null) callback.run();
                });
            }
            else {
                if (callback != null) callback.run();
            }

        }
        else {
            if (callback != null) callback.run();
        }
    }

    protected void startIndexBlockchain() {
        checkMasterNode();

        // Synchronize blockchain
        if (pluginSettings.enableBlockchainIndexation()) {

            Peer peer = pluginSettings.checkAndGetDuniterPeer();
            Currency currency = createCurrencyFromPeer(peer)
                    .orElseThrow(() -> new TechnicalException(String.format("Cannot load currency from peer {%s}", peer.toString())));

            // Wait end of currency index creation, then index blocks
            Runnable onCurrencyReady = () -> {
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("[%s] Indexing blockchain...", currency.getId()));
                }

                // Index blocks (and listen if new block appear)
                startIndexBlocks(peer);

                // Index WoT members
                startIndexMembers(peer);

                // Index peers (and listen if new peer appear)
                startIndexPeers(peer);
            };

            // Wait block chain reload, then run jobs
            // TODO: Move this reload feature, into a admin REST service
            reloadBlockchain(peer, onCurrencyReady);
        }
    }

    protected Optional<Currency> createCurrencyFromPeer(Peer peer) {
        Preconditions.checkNotNull(peer);

        if (pluginSettings.enableBlockchainIndexation()) {
            Currency currency;
            try {
                // Index (or refresh) node's currency
                currency = injector.getInstance(CurrencyService.class)
                        .indexCurrencyFromPeer(peer, true);
            } catch (Throwable e) {
                logger.error(String.format("Error while indexing currency. Skipping blockchain indexation.", e.getMessage()), e);
                throw e;
            }

            final String currencyId = currency.getId();
            peer.setCurrency(currencyId);

            // Define the main peer for this currency (will fill a cache in PeerService)
            injector.getInstance(PeerService.class)
                    .setCurrencyMainPeer(currencyId, peer);

            return Optional.of(currency);
        }

        return Optional.empty();
    }

    protected void startIndexBlocks(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        if (pluginSettings.enableBlockchainIndexation()) {

            try {
                BlockchainService blockchainService = injector.getInstance(BlockchainService.class);

                // Index last blocks
                blockchainService.indexLastBlocks(peer);

                // Listen for new blocks
                Closeable listener = blockchainService.listenAndIndexNewBlock(peer);
                threadPool.scheduleOnMasterFirstStop(listener);

                if (logger.isInfoEnabled()) logger.info(String.format("[%s] Indexing blockchain [OK]", peer.getCurrency()));

            } catch (Throwable e) {
                logger.error(String.format("[%s] Indexing blockchain error: %s", peer.getCurrency(), e.getMessage()), e);
                throw e;
            }
        }
    }

    protected void startIndexMembers(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        if (pluginSettings.enableBlockchainIndexation()) {

            try {
                // Index Wot members
                WotService wotService =  injector.getInstance(WotService.class)
                        .indexMembers(peer.getCurrency());

                // Listen new block, to update members
                Closeable listener = wotService.listenAndIndexMembers(peer.getCurrency());
                threadPool.scheduleOnMasterFirstStop(listener);

            } catch (Throwable e) {
                logger.error(String.format("[%s] Indexing WoT members error: %s", peer.getCurrency(), e.getMessage()), e);
                throw e;
            }
        }
    }

    protected void startIndexPeers(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        // Index peers (and listen if new peer appear)
        if (pluginSettings.enableBlockchainPeerIndexation()) {
            logger.info(String.format("[%s] Indexing peers...", peer.getCurrency()));
            PeerService peerService = injector.getInstance(PeerService.class)
                    .indexPeers(peer);

            Closeable listener = peerService.listenAndIndexPeers(peer);

            // Close listener
            threadPool.scheduleOnMasterFirstStop(listener);
        }
    }

    protected void startSynchroAndPeering() {
        checkMasterNode();

        // Start synchro, if enable in config
        if (pluginSettings.enableSynchro()) {
            Closeable listener = injector.getInstance(SynchroService.class)
                    .startScheduling();

            threadPool.scheduleOnMasterFirstStop(listener);
        }

        // Start publish peering to network, if enable in config
        if (pluginSettings.enablePeering()) {
            Closeable listener = injector.getInstance(NetworkService.class)
                    .startPublishingPeerDocumentToNetwork();

            threadPool.scheduleOnMasterFirstStop(listener);
        }
    }

    protected void startDocStatistics() {
        // Start doc stats, if enable in config
        if (pluginSettings.enableDocStats()) {

            // Add access to docstat index
            injector.getInstance(RestSecurityController.class)
                    .allowIndexType(RestRequest.Method.GET,
                            DocStatDao.INDEX,
                            DocStatDao.TYPE)
                    .allowPostSearchIndexType(
                            DocStatDao.INDEX,
                            DocStatDao.TYPE);

            // Add index [currency/record] to stats
            final DocStatService docStatService = injector
                    .getInstance(DocStatService.class)
                    .registerIndex(CurrencyExtendDao.INDEX, CurrencyExtendDao.RECORD_TYPE);

            // Wait end of currency index creation, then index blocks
            threadPool.scheduleOnClusterReady(() -> {
                Closeable closeable = docStatService.startScheduling();

            });
        }
    }
}
