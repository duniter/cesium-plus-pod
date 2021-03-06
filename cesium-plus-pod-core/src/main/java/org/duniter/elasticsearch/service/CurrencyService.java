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


import com.fasterxml.jackson.core.JsonProcessingException;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.dao.PeerDao;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.elasticsearch.Currency;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.BlockchainRemoteService;
import org.duniter.core.client.service.exception.HttpConnectException;
import org.duniter.core.client.service.exception.HttpTimeoutException;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.*;
import org.duniter.elasticsearch.exception.AccessDeniedException;
import org.duniter.elasticsearch.exception.DocumentNotFoundException;
import org.duniter.elasticsearch.exception.DuplicateIndexIdException;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Benoit on 30/03/2015.
 */
public class CurrencyService extends AbstractService {

    public static final String INDEX = CurrencyExtendDao.INDEX;
    public static final String RECORD_TYPE = CurrencyExtendDao.RECORD_TYPE;

    private BlockchainRemoteService blockchainRemoteService;
    private CurrencyExtendDao currencyDao;
    private Map<String, IndexDao<?>> currencyDataDaos = new ConcurrentHashMap<>();
    private Injector injector;

    @Inject
    public CurrencyService(Duniter4jClient client,
                           PluginSettings settings,
                           CryptoService cryptoService,
                           CurrencyDao currencyDao,
                           ThreadPool threadPool,
                           Injector injector,
                            final ServiceLocator serviceLocator) {
        super("duniter." + INDEX, client, settings, cryptoService);
        this.currencyDao = (CurrencyExtendDao)currencyDao;
        this.injector = injector;

        threadPool.scheduleOnStarted(() -> {
            this.blockchainRemoteService = serviceLocator.getBlockchainRemoteService();
            setIsReady(true);
        });
    }

    public CurrencyService createIndexIfNotExists() {
        currencyDao.createIndexIfNotExists();
        return this;
    }

    public CurrencyService deleteIndex() {
        currencyDao.deleteIndex();
        return this;
    }

    public boolean isCurrencyExists(String currencyName) {
        return currencyDao.isExists(currencyName);
    }

    /**
     * Return the given currency, or the default currency
     * @param currency
     * @return
     */
    public String safeGetCurrency(String currency) {

        if (StringUtils.isNotBlank(currency)) return currency;
        return currencyDao.getDefaultId();
    }

    /**
     * Retrieve the blockchain data, from peer
     *
     * @param peer
     * @param autoReconnect
     * @return the created blockchain
     */
    public Currency indexCurrencyFromPeer(Peer peer, boolean autoReconnect) {

        if (!autoReconnect) {
            return indexCurrencyFromPeer(peer);
        }

        while (true) {
            try {
                return indexCurrencyFromPeer(peer);
            } catch (HttpConnectException | HttpTimeoutException | DocumentNotFoundException e) {
                // log then retry
                logger.warn(String.format("[%s] Unable to connect. Retrying in 10s...", peer.toString()));
            }

            try {
                Thread.sleep(10 * 1000); // wait 10s
            } catch (Exception e) {
                throw new TechnicalException(e);
            }
        }
    }

    /**
     * Add a new currency record, from a peer(by getting blockchain parameters)
     *
     * @param peer
     * @return the created blockchain
     */
    public Currency indexCurrencyFromPeer(Peer peer) {

        waitReady();

        BlockchainParameters parameters = blockchainRemoteService.getParameters(peer);
        Currency result;

        // Check not already existing
        String currency = parameters.getCurrency();
        if (currencyDao.isExists(currency)) {
            result = (Currency)currencyDao.getById(currency);
            if (result == null) {
                throw new DocumentNotFoundException(String.format("Currency {%s} not found. Cluster may be not fully started..."));
            }
            return result;
        }

        BlockchainBlock firstBlock = blockchainRemoteService.getBlock(peer, 0l);
        BlockchainBlock currentBlock = blockchainRemoteService.getCurrentBlock(peer);
        Long lastUD = blockchainRemoteService.getLastUD(peer);

        result = new Currency();
        result.setId(parameters.getCurrency());
        result.setFirstBlockSignature(firstBlock.getSignature());
        result.setMembersCount(currentBlock.getMembersCount());
        result.setLastUD(lastUD);
        result.setParameters(parameters);

        // Save it
        save(result);

        return result;
    }

    public void updateMemberCount(String currency, int memberCount){
        Preconditions.checkNotNull(currency, "currency could not be null") ;
        this.currencyDao.updateMemberCount(currency, memberCount);
    }

    public void updateLastUD(String currency, long lastUD){
        Preconditions.checkNotNull(currency, "currency could not be null") ;
        this.currencyDao.updateLastUD(currency, lastUD);
    }

    public long getLastUD(String currency) {
        Preconditions.checkNotNull(currency, "currency could not be null") ;
        return this.currencyDao.getLastUD(currency);
    }

    /**
     * Save a blockchain (update or create) into the blockchain index.
     * @param currency
     * @throws DuplicateIndexIdException
     * @throws AccessDeniedException if exists and user if not the original blockchain sender
     */
    public void save(Currency currency) throws DuplicateIndexIdException {
        Preconditions.checkNotNull(currency, "currency could not be null") ;
        Preconditions.checkNotNull(currency.getId(), "currency attribute 'currency' could not be null");

        boolean exists = currencyDao.isExists(currency.getId());

        // Currency not exists, so create it
        if (!exists) {
            // Save it
            currencyDao.create(currency);

            // Create data index (delete first if exists)
            getCurrencyDataDao(currency.getId())
                .deleteIndex()
                .createIndexIfNotExists();

        }

        // Exists, so check the owner signature
        else {

            // Save changes
            currencyDao.update(currency);

            // Create data index (if need)
            getCurrencyDataDao(currency.getId())
                    .createIndexIfNotExists();
        }
    }

    public Set<String> getAllIds() {
        return currencyDao.getAllIds();
    }

    /* -- Internal methods -- */

    protected IndexDao<?> getCurrencyDataDao(final String currencyId) {
        // Create data
        IndexDao<?> dataDao = currencyDataDaos.get(currencyId);
        if (dataDao == null) {
            dataDao = new AbstractIndexDao(currencyId) {
                @Override
                protected void createIndex() throws JsonProcessingException {
                    logger.info(String.format("Creating index [%s]", currencyId));

                    CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(currencyId);
                    org.elasticsearch.common.settings.Settings indexSettings = org.elasticsearch.common.settings.Settings.settingsBuilder()
                            .put("number_of_shards", 3)
                            .put("number_of_replicas", 1)
                            .build();
                    createIndexRequestBuilder.setSettings(indexSettings);

                    // Add peer type
                    TypeDao<?> peerDao = (TypeDao<?>)ServiceLocator.instance().getBean(PeerDao.class);
                    createIndexRequestBuilder.addMapping(peerDao.getType(), peerDao.createTypeMapping());

                    // Add block type
                    BlockDao blockDao = ServiceLocator.instance().getBean(BlockDao.class);
                    createIndexRequestBuilder.addMapping(blockDao.getType(), blockDao.createTypeMapping());

                    // Add movement type
                    MovementDao movementDao = ServiceLocator.instance().getBean(MovementDao.class);
                    createIndexRequestBuilder.addMapping(movementDao.getType(), movementDao.createTypeMapping());

                    // Add wot type
                    MemberDao memberDao = ServiceLocator.instance().getBean(MemberDao.class);
                    createIndexRequestBuilder.addMapping(memberDao.getType(), memberDao.createTypeMapping());

                    // Add blockStat type
                    BlockStatDao blockStatDao = injector.getInstance(BlockStatDao.class);
                    createIndexRequestBuilder.addMapping(blockStatDao.getType(), blockStatDao.createTypeMapping());

                    // Add synchro execution
                    SynchroExecutionDao synchroExecutionDao = injector.getInstance(SynchroExecutionDao.class);
                    createIndexRequestBuilder.addMapping(synchroExecutionDao.getType(), synchroExecutionDao.createTypeMapping());

                    // Add pending membership
                    TypeDao<?> pendingMembershipDao = ServiceLocator.instance().getBean(PendingMembershipDao.class);
                    createIndexRequestBuilder.addMapping(pendingMembershipDao.getType(), pendingMembershipDao.createTypeMapping());

                    // Creating the index
                    createIndexRequestBuilder.execute().actionGet();
                }
            };
            injector.injectMembers(dataDao);
            currencyDataDaos.put(currencyId, dataDao);
        }

        return dataDao;


    }
}
