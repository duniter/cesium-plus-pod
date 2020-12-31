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

import com.google.common.base.Joiner;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.elasticsearch.Currency;
import org.duniter.core.client.model.local.Identity;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.dao.*;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.*;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ScheduledActionFuture;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestRequest;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

        // Each time the node is the master
        threadPool.onMasterStart(() -> {

            logger.info(String.format("Starting core jobs..."
                            + " {blockchain: block:%s, peer:%s, pending:%s},"
                            + " {p2p: synchro:%s, websocket:%s, emit_peering:%s},"
                            + " {doc_stats:%s}",
                    pluginSettings.enableBlockchainIndexation(),
                    pluginSettings.enableBlockchainPeerIndexation(),
                    pluginSettings.enablePendingMembershipIndexation(),
                    pluginSettings.enableSynchro(),
                    pluginSettings.enableSynchroWebsocket(),
                    pluginSettings.enablePeering(),
                    pluginSettings.enableDocStats()));

            // Create indices (once)
            createIndices();

            threadPool.scheduleOnClusterReady(() -> {

                // Start blockchain indexation (and wait)
                startIndexBlockchain().map(ScheduledActionFuture::actionGet);

                // Start synchro (and wait)
                startSynchro().map(ScheduledActionFuture::actionGet);

                // Start publish peering (and wait)
                startPublishingPeer().map(ScheduledActionFuture::actionGet);

                // Start doc stats (and wait)
                startDocStatistics().map(ScheduledActionFuture::actionGet);

                // Migrate old data (and wait)
                startDataMigration().actionGet();

                logger.info("Starting core jobs... [OK]");
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

                    // Add access to <currency>/pending index
                    .allowIndexType(RestRequest.Method.GET,
                            CURRENCY_NAME_REGEXP,
                            PendingMembershipDao.TYPE)
                    .allowPostSearchIndexType(
                            CURRENCY_NAME_REGEXP,
                            PendingMembershipDao.TYPE)

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
                .allow(RestRequest.Method.DELETE, "^/_search/scroll$"); // WARN: should NEVER authorized URL likes /_search/scroll/all (= ALL scroll )

        // Add access to document/stats index
        if (pluginSettings.enableDocStats()) {

            injector.getInstance(RestSecurityController.class)
                    .allowIndexType(RestRequest.Method.GET,
                            DocStatDao.INDEX,
                            DocStatDao.TYPE)
                    .allowPostSearchIndexType(
                            DocStatDao.INDEX,
                            DocStatDao.TYPE);
        }
    }

    protected ScheduledActionFuture<?> createIndices() {

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

        // IMPORTANT: make sure cluster is ready (=all indices created)
        return threadPool.scheduleOnClusterReady(() -> {});
    }

    protected void checkMasterNode() {
        Preconditions.checkArgument(threadPool.isMasterNode(), "Node must be the master node to execute this job");
    }

    protected void reloadBlockchainIfNeed(Peer peer) {

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

                // Wait cluster finished deletion, then reindex
                threadPool.scheduleOnClusterReady(() -> {
                    injector.getInstance(BlockchainService.class)
                            .indexBlocksRange(peer,
                                    pluginSettings.reloadBlockchainIndicesFrom(),
                                    pluginSettings.reloadBlockchainIndicesTo());
                    })
                    .actionGet();
            }
        }
    }

    protected Optional<ScheduledActionFuture<?>> startIndexBlockchain() {
        if (!pluginSettings.enableBlockchainIndexation()) return Optional.empty(); // Skip

        checkMasterNode();

        try {
            // Index the currency
            Peer peer = pluginSettings.checkAndGetDuniterPeer();
            Currency currency = createCurrencyFromPeer(peer).actionGet();

            // Reload some blockchain blocks
            // TODO: Move this reload feature, into a admin REST service ?
            reloadBlockchainIfNeed(peer);

            if (logger.isInfoEnabled()) {
                logger.info(String.format("[%s] Indexing blockchain...", currency.getId()));
            }

            // Index blocks (and listen if new block appear)
            startIndexBlocks(peer);

            // Index WoT members
            startIndexMembers(peer);

            // Start listening pending memberships
            startIndexPendingMemberships(peer);

            // Index peers (and listen if new peer appear)
            startIndexPeers(peer);

            return Optional.of(threadPool.scheduleOnClusterReady(() -> {}));
        }

        catch (Exception e) {
            // Log, then retying in 2s
            logger.error("Failed during start of blockchain indexation. Retrying in 10s...", e);
            return Optional.of(threadPool.schedule(this::startIndexBlockchain, 10, TimeUnit.SECONDS));
        }

    }

    protected ScheduledActionFuture<Currency> createCurrencyFromPeer(Peer peer) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkArgument(pluginSettings.enableBlockchainIndexation());

        Currency currency;
        try {
            // Index (or refresh) node's currency
            currency = injector.getInstance(CurrencyService.class)
                    .indexCurrencyFromPeer(peer, true);
        } catch (Throwable e) {
            logger.error(String.format("Error while indexing currency: %s", e.getMessage()), e);
            throw e;
        }

        final String currencyId = currency.getId();
        peer.setCurrency(currencyId);

        // Define the main peer for this currency (will fill a cache in PeerService)
        injector.getInstance(PeerService.class)
                .setCurrencyMainPeer(currencyId, peer);

        // Wait enf of currency index creation
        final Currency result = currency;
        return threadPool.scheduleOnClusterReady(() -> result);
    }

    protected void startIndexBlocks(Peer peer) {
        if (!pluginSettings.enableBlockchainIndexation()) return; // Skip

        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        try {
            Closeable stoppable = injector.getInstance(BlockchainService.class)
                // Index last blocks
                .indexLastBlocks(peer)
                // Listen for new blocks
                .listenAndIndexNewBlock(peer);

            threadPool.scheduleOnMasterFirstStop(stoppable);

            if (logger.isInfoEnabled()) logger.info(String.format("[%s] Indexing blockchain [OK]", peer.getCurrency()));

        } catch (Throwable e) {
            logger.error(String.format("[%s] Indexing blockchain error: %s", peer.getCurrency(), e.getMessage()), e);
            throw e;
        }
    }

    protected void startIndexMembers(Peer peer) {
        if (!pluginSettings.enableBlockchainIndexation()) return; // Skip

        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        // Add stats on members
        if (pluginSettings.enableDocStats()) {
            DocStatService docStatService = injector.getInstance(DocStatService.class);

            // Is member
            {
                QueryBuilder query = QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.termQuery(Identity.PROPERTY_IS_MEMBER, true))
                );
                String queryName = Joiner.on('_').join(peer.getCurrency(), Strings.toUnderscoreCase(Identity.PROPERTY_IS_MEMBER));
                docStatService.registerIndex(peer.getCurrency(), MemberDao.TYPE, queryName, query, null);
            }
        }

        try {
            // Index Wot members
            WotService wotService =  injector.getInstance(WotService.class)
                    .indexMembers(peer.getCurrency());

            // Listen new block, to update members
            Closeable stop = wotService.listenAndIndexMembers(peer.getCurrency());

            // Stop to listen, if master stop
            threadPool.scheduleOnMasterFirstStop(stop);


        } catch (Throwable e) {
            logger.error(String.format("[%s] Indexing WoT members error: %s", peer.getCurrency(), e.getMessage()), e);
            throw e;
        }
    }

    protected void startIndexPeers(Peer peer) {
        if (!pluginSettings.enableBlockchainPeerIndexation()) return; // Skip

        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        // Add stats on UP peers, per API
        if (pluginSettings.enableDocStats()) {
            DocStatService docStatService = injector.getInstance(DocStatService.class);

            QueryBuilder statusQuery = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.termQuery(Peer.PROPERTY_STATS + "." + Peer.Stats.PROPERTY_STATUS, Peer.PeerStatus.UP.name()));

            // Peers UP
            {
                QueryBuilder query = QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.nestedQuery(Peer.PROPERTY_STATS, statusQuery)));
                String queryName = Joiner.on('_').join(peer.getCurrency(), PeerDao.TYPE, Peer.PeerStatus.UP.name()).toLowerCase();
                docStatService.registerIndex(peer.getCurrency(), PeerDao.TYPE, queryName, query, null);
            }

            // Peers UP, per API
            Arrays.stream(EndpointApi.values())
                    .forEach(api -> {
                        BoolQueryBuilder apiQuery = QueryBuilders.boolQuery()
                                .filter(QueryBuilders.termQuery(Peer.PROPERTY_API, api.name()));

                        QueryBuilder query = QueryBuilders.constantScoreQuery(apiQuery
                                        .must(QueryBuilders.nestedQuery(Peer.PROPERTY_STATS, statusQuery)));

                        String queryName = Joiner.on('_').join(peer.getCurrency(), PeerDao.TYPE, api.name()).toLowerCase();
                        docStatService.registerIndex(peer.getCurrency(), PeerDao.TYPE, queryName, query, null);
                    });
        }

        try {
            // Index peers (and listen if new peer appear)
            logger.info(String.format("[%s] Indexing peers...", peer.getCurrency()));
            PeerService peerService = injector.getInstance(PeerService.class)
                    .indexPeers(peer);

            Closeable stop = peerService.listenAndIndexPeers(peer);

            // Stop to listen, if master stop
            threadPool.scheduleOnMasterFirstStop(stop);
        }  catch (Throwable e) {
            logger.error(String.format("[%s] Indexing blockchain peers error: %s", peer.getCurrency(), e.getMessage()), e);
            throw e;
        }
    }

    protected void startIndexPendingMemberships(Peer peer) {
        if (!pluginSettings.enablePendingMembershipIndexation()) return; // Skip

        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getCurrency());

        // Register stats on indices
        if (pluginSettings.enableDocStats()) {
            injector.getInstance(DocStatService.class)
                    .registerIndex(peer.getCurrency(), PendingMembershipDao.TYPE);
        }

        try {
            // Index peers (and listen if new peer appear)
            ScheduledActionFuture<?> job = injector.getInstance(PendingMembershipService.class)
                    .indexFromPeer(peer)
                    .startScheduling();

            // Stop to listen, if master stop
            threadPool.scheduleOnMasterFirstStop((Runnable)() -> job.cancel(true));
        }  catch (Throwable e) {
            logger.error(String.format("[%s] Indexing blockchain peers error: %s", peer.getCurrency(), e.getMessage()), e);
            throw e;
        }
    }

    protected Optional<ScheduledActionFuture<?>> startSynchro() {

        checkMasterNode();

        // Start synchro, if enable in config
        if (pluginSettings.enableSynchro()) {
            ScheduledActionFuture future = injector.getInstance(SynchroService.class)
                    .startScheduling();

            // Stop action when master stop
            threadPool.scheduleOnMasterFirstStop((Runnable) () -> future.cancel(true));

            return Optional.of(future);
        }

        return Optional.empty();
    }

    protected Optional<ScheduledActionFuture<?>> startPublishingPeer() {

        checkMasterNode();

        // Start publish peering to network, if enable in config
        if (pluginSettings.enablePeering()) {
            Optional<ScheduledActionFuture<?>> optionalFuture = injector.getInstance(NetworkService.class)
                    .startPublishingPeerDocumentToNetwork()
                    // Stop to listen, if master stop
                    .map(future -> {
                        threadPool.scheduleOnMasterFirstStop((Runnable) () -> future.cancel(true));
                        return future;
                    });

            return optionalFuture;
        }

        return Optional.empty();
    }

    protected Optional<ScheduledActionFuture<?>> startDocStatistics() {
        // Start doc stats, if enable in config
        if (pluginSettings.enableDocStats()) {

            // Add index [currency/record] to stats
            final DocStatService docStatService = injector
                    .getInstance(DocStatService.class)
                    .registerIndex(CurrencyExtendDao.INDEX, CurrencyExtendDao.RECORD_TYPE);

            ScheduledActionFuture<?> future = docStatService.startScheduling();

            // Stop to listen, if master stop
            threadPool.scheduleOnMasterFirstStop((Runnable)() -> future.cancel(true));

            return Optional.of(future);
        }

        return Optional.empty();
    }

    protected ScheduledActionFuture<?> startDataMigration() {
        return threadPool.scheduleOnClusterReady(() -> {
            // Start migration (if need)
            injector.getInstance(DocStatService.class)
                    .startDataMigration();
        });
    }
}
