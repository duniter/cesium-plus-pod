package org.duniter.elasticsearch.synchro;

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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.HttpService;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.DateUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.websocket.WebsocketClientEndpoint;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.SynchroExecutionDao;
import org.duniter.elasticsearch.model.SynchroExecution;
import org.duniter.elasticsearch.model.SynchroResult;
import org.duniter.elasticsearch.service.AbstractService;
import org.duniter.elasticsearch.service.NetworkService;
import org.duniter.elasticsearch.service.ServiceLocator;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeEvents;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by blavenie on 27/10/16.
 */
public class SynchroService extends AbstractService {

    private static final String WS_CHANGES_URL = "/ws/_changes";
    private final static Set<EndpointApi> includeEndpointApis = Sets.newHashSet();
    private static List<WebsocketClientEndpoint> wsClientEndpoints = Lists.newArrayList();
    private static List<SynchroAction> actions = Lists.newArrayList();

    private HttpService httpService;
    private final ThreadPool threadPool;
    private final CurrencyDao currencyDao;
    private final SynchroExecutionDao synchroExecutionDao;
    private final NetworkService networkService;

    private boolean forceFullResync = false;

    @Inject
    public SynchroService(Duniter4jClient client,
                          PluginSettings settings,
                          CryptoService cryptoService,
                          ThreadPool threadPool,
                          CurrencyDao currencyDao,
                          SynchroExecutionDao synchroExecutionDao,
                          NetworkService networkService,
                          final ServiceLocator serviceLocator) {
        super("duniter.p2p", client, settings, cryptoService);
        this.threadPool = threadPool;
        this.currencyDao = currencyDao;
        this.synchroExecutionDao = synchroExecutionDao;
        this.networkService = networkService;
        threadPool.scheduleOnStarted(() -> {
            httpService = serviceLocator.getHttpService();
            setIsReady(true);
        });
    }

    public void register(SynchroAction action) {
        Preconditions.checkNotNull(action);
        Preconditions.checkNotNull(action.getEndPointApi());

        if (!includeEndpointApis.contains(action.getEndPointApi())) {
            includeEndpointApis.add(action.getEndPointApi());
        }
        actions.add(action);
    }

    /**
     * Start scheduling doc stats update
     * @return
     */
    public SynchroService startScheduling() {
        // Launch once, at startup (after a delay of 10s)
        threadPool.schedule(() -> {
            boolean launchAtStartup;
            try {
                // wait for some peers
                launchAtStartup = networkService.waitPeersReady(includeEndpointApis);
            } catch (InterruptedException e) {
                return; // stop
            }

            // If can be launched now: do it
            if (launchAtStartup) {

                forceFullResync = pluginSettings.fullResyncAtStartup();

                synchronize();

                forceFullResync = false;
            }

            // Schedule next execution, to 5 min before each hour
            // (to make sure to be ready when computing doc stat - see DocStatService)
            long nextExecutionDelay = DateUtils.nextHour().getTime() - System.currentTimeMillis() - 5 * 60 * 1000;

            // If next execution is too close, skip it
            if (launchAtStartup && nextExecutionDelay < 5 * 60 * 1000) {
                // add an hour
                nextExecutionDelay += 60 * 60 * 1000;
            }

            // Schedule every hour
            threadPool.scheduleAtFixedRate(
                    this::synchronize,
                    nextExecutionDelay,
                    60 * 60 * 1000 /* every hour */,
                    TimeUnit.MILLISECONDS);
        },
        10 * 1000 /*wait 10 s */ ,
        TimeUnit.MILLISECONDS);

        return this;
    }

    public void synchronize() {

        final boolean enableSynchroWebsocket = pluginSettings.enableSynchroWebsocket();

        // Closing all opened WS
        if (enableSynchroWebsocket) {
            closeWsClientEndpoints();
        }

        List<String> currencyIds;
        try {
            currencyIds = currencyDao.getCurrencyIds();
        }
        catch (Exception e) {
            logger.error("Could not retrieve indexed currencies", e);
            currencyIds = null;
        }

        if (CollectionUtils.isEmpty(currencyIds) || CollectionUtils.isEmpty(includeEndpointApis)) {
            logger.warn("Skipping synchronization: no indexed currency or no API configured");
            return;
        }

        currencyIds.forEach(currencyId -> includeEndpointApis.forEach(peerApiFilter -> {

            logger.info(String.format("[%s] [%s] Starting synchronization from network... {peers discovery: %s}", currencyId, peerApiFilter.name(), pluginSettings.enableSynchroDiscovery()));

            // Get peers for currencies and API
            Collection<Peer> peers = networkService.getPeersFromApi(currencyId, peerApiFilter);
            if (CollectionUtils.isNotEmpty(peers)) {
                peers.forEach(p -> synchronizePeer(p, enableSynchroWebsocket));
                logger.info(String.format("[%s] [%s] Synchronization [OK]", currencyId, peerApiFilter.name()));
            } else {
                logger.debug(String.format("[%s] [%s] Synchronization [OK] (no source peer found)", currencyId, peerApiFilter.name()));
            }
            }
        ));
    }

    public SynchroResult synchronizePeer(final Peer peer, boolean enableSynchroWebsocket) {
        long startExecutionTime = System.currentTimeMillis();

        // Check if peer alive and valid
        boolean isAliveAndValid = networkService.isEsNodeAliveAndValid(peer);
        if (!isAliveAndValid) {
            logger.warn(String.format("[%s] [%s] Not reachable, or not running on this currency. Skipping.", peer.getCurrency(), peer));
            return null;
        }

        SynchroResult result = new SynchroResult();

        // Get the last execution time (or 0 is never synchronized)
        // If not the first synchro, add a delay to last execution time
        // to avoid missing data because incorrect clock configuration
        long lastExecutionTime = forceFullResync ? 0 : getLastExecutionTime(peer);
        if (logger.isDebugEnabled() && lastExecutionTime > 0) {
            logger.debug(String.format("[%s] [%s] Found last synchronization execution at {%s}. Will apply time offset of {-%s ms}", peer.getCurrency(), peer,
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
                            .format(new Date(lastExecutionTime * 1000)),
                    pluginSettings.getSynchroTimeOffset()));
        }

        final long fromTime = lastExecutionTime > 0 ? lastExecutionTime - pluginSettings.getSynchroTimeOffset() : 0;


        if (logger.isInfoEnabled()) {
            if (fromTime == 0) {
                logger.info(String.format("[%s] [%s] Synchronization {ALL}...", peer.getCurrency(), peer));
            }
            else {
                logger.info(String.format("[%s] [%s] Synchronization delta since {%s}...",
                        peer.getCurrency(),
                        peer,
                        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
                                .format(new Date(fromTime * 1000))));
            }
        }

        // Execute actions
        List<SynchroAction> executedActions = actions.stream()
                .filter(a -> a.getEndPointApi().name().equals(peer.getApi()))
                .map(a -> {
                    try {
                        a.handleSynchronize(peer, fromTime, result);
                    } catch(Exception e) {
                        logger.error(String.format("[%s] [%s] Failed to execute synchro action: %s", peer.getCurrency(), peer, e.getMessage()), e);
                    }
                    return a;
                })
                .collect(Collectors.toList());

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[%s] [%s] Synchronized in %s ms: %s", peer.getCurrency(), peer, System.currentTimeMillis() - startExecutionTime, result.toString()));
        }

        saveExecution(peer, result, startExecutionTime);

        // Start listen changes on this peer
        if (enableSynchroWebsocket) {
            startListenChangesOnPeer(peer, executedActions);
        }

        return result;
    }

    /* -- protected methods -- */

    protected long getLastExecutionTime(Peer peer) {
        Preconditions.checkNotNull(peer);

        try {
            SynchroExecution execution = synchroExecutionDao.getLastExecution(peer);
            return execution != null ? execution.getTime() : 0;
        }
        catch (Exception e) {
            logger.error(String.format("Error while saving last synchro execution time, for peer [%s]. Will resync all.", peer), e);
            return 0;
        }
    }

    protected void saveExecution(Peer peer, SynchroResult result, long startExecutionTime) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(peer.getId());
        Preconditions.checkNotNull(result);

        try {
            SynchroExecution execution = new SynchroExecution();
            execution.setCurrency(peer.getCurrency());
            execution.setPeer(peer.getId());
            execution.setApi(peer.getApi());
            execution.setExecutionTime(System.currentTimeMillis() - startExecutionTime);
            execution.setResult(result);

            // Start execution time (in seconds)
            execution.setTime(startExecutionTime/1000);

            synchroExecutionDao.save(execution);
        }
        catch (Exception e) {
            logger.error(String.format("Error while saving synchro execution on peer [%s]", peer), e);
        }
    }

    protected void closeWsClientEndpoints() {
        synchronized(wsClientEndpoints) {
            // Closing all opened WS
            wsClientEndpoints.forEach(IOUtils::closeQuietly);
            wsClientEndpoints.clear();
        }
    }

    protected void startListenChangesOnPeer(final Peer peer,
                                            final List<SynchroAction> actions) {
        // Listens changes on this peer
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(actions);

        // Compute a change source for ALL indices/types
        final ChangeSource changeSource = new ChangeSource();
        actions.stream()
                .map(SynchroAction::getChangeSource)
                .filter(Objects::nonNull)
                .forEach(changeSource::merge);

        // Prepare a map of actions by index/type
        final ArrayListMultimap<String, SynchroAction> actionsBySource = ArrayListMultimap.create(actions.size(), 2);
        actions.stream()
            .forEach(a -> {
                if (a.getChangeSource() != null) {
                    actionsBySource.put(a.getChangeSource().toString(), a);
                }
            });

        // Get (or create) the websocket endpoint
        WebsocketClientEndpoint wsClientEndPoint = httpService.getWebsocketClientEndpoint(peer, WS_CHANGES_URL, false);

        // filter on selected sources
        wsClientEndPoint.sendMessage(changeSource.toString());

        // add listener
        wsClientEndPoint.registerListener( message -> {
            try {
                ChangeEvent changeEvent = ChangeEvents.fromJson(getObjectMapper(), message);
                String source = changeEvent.getIndex() + "/" +  changeEvent.getType();
                List<SynchroAction> sourceActions = actionsBySource.get(source);

                // Call each mapped actions
                if (CollectionUtils.isNotEmpty(sourceActions)) {
                    sourceActions.forEach(a -> a.handleChange(peer, changeEvent));
                }

            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.warn(String.format("[%s] Unable to process changes received by [/ws/_changes]: %s", peer, e.getMessage()), e);
                }
                else {
                    logger.warn(String.format("[%s] Unable to process changes received by [/ws/_changes]: %s", peer, e.getMessage()));
                }
            }
        });

        // Add to list
        synchronized(wsClientEndpoints) {
            wsClientEndpoints.add(wsClientEndPoint);
        }
    }


}
