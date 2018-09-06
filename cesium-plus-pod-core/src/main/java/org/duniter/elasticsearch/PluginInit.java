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
import org.duniter.elasticsearch.dao.*;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.CurrencyService;
import org.duniter.elasticsearch.service.DocStatService;
import org.duniter.elasticsearch.service.PeerService;
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

        else if (pluginSettings.enableBlockchainSync() && pluginSettings.reloadBlockchainIndices() && pluginSettings.reloadBlockchainIndicesFrom() <= 0) {
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
        if (pluginSettings.enableBlockchainSync()) {

            Peer peer = pluginSettings.checkAndGetPeer();

            Currency currency;
            try {
                // Index (or refresh) node's currency
                currency = injector.getInstance(CurrencyService.class)
                        .indexCurrencyFromPeer(peer, true);
            } catch(Throwable e){
                logger.error(String.format("Error while indexing currency. Skipping blockchain indexation.", e.getMessage()), e);
                throw e;
            }

            final String currencyName = currency.getCurrencyName();

            // Add access security rules, for the currency indices
            injector.getInstance(RestSecurityController.class)

                    // Add access to <currency>/block index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyName,
                            BlockDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyName,
                            BlockDao.TYPE)

                    // Add access to <currency>/blockStat index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyName,
                            BlockStatDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyName,
                            BlockStatDao.TYPE)

                    // Add access to <currency>/peer index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyName,
                            PeerDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyName,
                            PeerDao.TYPE)

                    // Add access to <currency>/movement index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyName,
                            MovementDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyName,
                            MovementDao.TYPE)

                    // Add access to <currency>/synchro index
                    .allowIndexType(RestRequest.Method.GET,
                            currencyName,
                            SynchroExecutionDao.TYPE)
                    .allowPostSearchIndexType(
                            currencyName,
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
                            .deleteRange(currencyName,
                                    pluginSettings.reloadBlockchainIndicesFrom(),
                                    pluginSettings.reloadBlockchainIndicesTo());
                }
                else {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("/!\\ Re-indexing blockchain from block #%s...", pluginSettings.reloadBlockchainIndicesFrom()));
                    }

                    injector.getInstance(BlockchainService.class)
                            .deleteFrom(currencyName, pluginSettings.reloadBlockchainIndicesFrom());
                }
            }
            else {
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("[%s] Indexing blockchain...", currencyName));
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

                try {
                    // Index blocks (and listen if new block appear)
                    injector.getInstance(BlockchainService.class)
                            .indexLastBlocks(peer)
                            .listenAndIndexNewBlock(peer);

                    // Index peers (and listen if new peer appear)
                    injector.getInstance(PeerService.class)
                            .listenAndIndexPeers(peer);


                    // Start synchro
                    if (pluginSettings.enableSynchro()) {
                        injector.getInstance(SynchroService.class)
                                .startScheduling();
                    }

                    if (logger.isInfoEnabled()) {
                        logger.info(String.format("[%s] Indexing blockchain [OK]", currencyName));
                    }

                } catch(Throwable e){
                    logger.error(String.format("[%s] Indexing blockchain error: %s", currencyName, e.getMessage()), e);
                    throw e;
                }

            });

        }

        // If doc stats enable
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
                    .registerIndex(CurrencyService.INDEX, CurrencyService.RECORD_TYPE);

            // Wait end of currency index creation, then index blocks
            threadPool.scheduleOnClusterReady(docStatService::startScheduling);
        }

        // Allow scroll search
        injector.getInstance(RestSecurityController.class)
                .allow(RestRequest.Method.POST, "^/_search/scroll$");
    }
}
