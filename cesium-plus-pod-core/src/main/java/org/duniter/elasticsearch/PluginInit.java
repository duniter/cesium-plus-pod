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

/**
 * Created by blavenie on 17/06/16.
 */
public class PluginInit extends AbstractLifecycleComponent<PluginInit> {

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
        threadPool.scheduleOnClusterReady(() -> {
            createIndices();

            // Waiting cluster back to GREEN or YELLOW state, before doAfterStart
            threadPool.scheduleOnClusterReady(this::doAfterStart);

        });
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() {

    }

    protected void createIndices() {

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

    protected void doAfterStart() {

        // Synchronize blockchain
        if (pluginSettings.enableBlockchainIndexation()) {

            Peer peer = pluginSettings.checkAndGetDuniterPeer();

            Currency currency;
            try {
                // Index (or refresh) node's currency
                currency = injector.getInstance(CurrencyService.class)
                        .indexCurrencyFromPeer(peer, true);
            } catch(Throwable e){
                logger.error(String.format("Error while indexing currency. Skipping blockchain indexation.", e.getMessage()), e);
                throw e;
            }

            final String currencyId = currency.getId();
            peer.setCurrency(currencyId);

            // Define the main peer for this currency (will fill a cache in PeerService)
            injector.getInstance(PeerService.class).setCurrencyMainPeer(currencyId, peer);

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
                            currencyId,
                            BlockDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyId,
                            BlockDao.TYPE)

                    // Add access to <currency>/blockStat index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyId,
                            BlockStatDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyId,
                            BlockStatDao.TYPE)

                    // Add access to <currency>/peer index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyId,
                            PeerDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyId,
                            PeerDao.TYPE)

                    // Add access to <currency>/movement index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyId,
                            MovementDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyId,
                            MovementDao.TYPE)

                    // Add access to <currency>/member index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyId,
                            MemberDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyId,
                            MemberDao.TYPE)

                    // Add access to <currency>/synchro index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyId,
                            SynchroExecutionDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyId,
                            SynchroExecutionDao.TYPE);

            /* TODO à décommenter quand les pending seront sauvegardés
            injector.getInstance(DocStatService.class)
            .registerIndex(currencyName,
                            PendingRegistrationDao.TYPE);
             */

            // If partial reload (from a block)
            if (pluginSettings.reloadBlockchainIndices() && pluginSettings.reloadBlockchainIndicesFrom() > 0) {
                // Delete blocs range [from,to]
                if (pluginSettings.reloadBlockchainIndicesTo() > pluginSettings.reloadBlockchainIndicesFrom()) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("/!\\ Re-indexing blockchain range [%s-%s]...",
                                pluginSettings.reloadBlockchainIndicesFrom(),
                                pluginSettings.reloadBlockchainIndicesTo()));
                    }

                    injector.getInstance(BlockchainService.class)
                            .deleteRange(currencyId,
                                    pluginSettings.reloadBlockchainIndicesFrom(),
                                    pluginSettings.reloadBlockchainIndicesTo());
                }
                else {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("/!\\ Re-indexing blockchain from block #%s...", pluginSettings.reloadBlockchainIndicesFrom()));
                    }

                    injector.getInstance(BlockchainService.class)
                            .deleteFrom(currencyId, pluginSettings.reloadBlockchainIndicesFrom());
                }
            }
            else {
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("[%s] Indexing blockchain...", currencyId));
                }
            }


            // Wait end of currency index creation, then index blocks
            threadPool.scheduleOnClusterReady(() -> {

                // Reindex range
                if (pluginSettings.reloadBlockchainIndices()
                        && pluginSettings.reloadBlockchainIndicesFrom() > 0
                        && pluginSettings.reloadBlockchainIndicesTo() > pluginSettings.reloadBlockchainIndicesFrom()) {
                        injector.getInstance(BlockchainService.class)
                                .indexBlocksRange(peer,
                                        pluginSettings.reloadBlockchainIndicesFrom(),
                                        pluginSettings.reloadBlockchainIndicesTo());
                }

                // Index blocks (and listen if new block appear)
                startIndexBlocks(peer);

                // Index WoT members
                startIndexMembers(peer);

                // Index peers (and listen if new peer appear)
                startIndexPeers(peer);

                // Start synchro and peering
                startSynchroAndPeering();

                // Start doc stats
                startDocStatistics();
            });

        }

        // No blockchain indexation
        else {

            // Wait end of currency index creation
            threadPool.scheduleOnClusterReady(() -> {
                // Start synchro and peering
                // (this is possible because default peers can be define in config, by including static endpoints)
                startSynchroAndPeering();

                // Start doc stats
                startDocStatistics();
            });
        }

        // Allow scroll search (need by synchro from other peers)
        injector.getInstance(RestSecurityController.class)
                .allow(RestRequest.Method.POST, "^/_search/scroll$");
    }

    protected void startIndexBlocks(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        if (pluginSettings.enableBlockchainIndexation()) {

            // Index blocks (and listen if new block appear)
            try {
                injector.getInstance(BlockchainService.class)
                        .indexLastBlocks(peer)
                        .listenAndIndexNewBlock(peer);

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

            // Index Wot members
            try {
                injector.getInstance(WotService.class)
                        .indexMembers(peer.getCurrency())
                        .listenAndIndexMembers(peer.getCurrency());

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
            logger.info(String.format("[%s] Indexing blockchain peers...", peer.getCurrency()));
            injector.getInstance(PeerService.class)
                    .listenAndIndexPeers(peer);
        }
    }

    protected void startSynchroAndPeering() {

        // Start synchro, if enable in config
        if (pluginSettings.enableSynchro()) {
            injector.getInstance(SynchroService.class)
                    .startScheduling();
        }

        // Start publish peering to network, if enable in config
        if (pluginSettings.enablePeering()) {
            injector.getInstance(NetworkService.class)
                    .startPublishingPeerDocumentToNetwork();
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
            threadPool.scheduleOnClusterReady(docStatService::startScheduling);
        }
    }
}
