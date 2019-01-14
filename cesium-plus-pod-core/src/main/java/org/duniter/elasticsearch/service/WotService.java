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


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.BlockchainRemoteService;
import org.duniter.core.client.service.bma.NetworkRemoteService;
import org.duniter.core.client.service.bma.WotRemoteService;
import org.duniter.core.client.service.exception.BlockNotFoundException;
import org.duniter.core.client.util.KnownBlocks;
import org.duniter.core.client.util.KnownCurrencies;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.model.NullProgressionModel;
import org.duniter.core.model.ProgressionModel;
import org.duniter.core.model.ProgressionModelImpl;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.cache.SimpleCache;
import org.duniter.core.util.json.JsonAttributeParser;
import org.duniter.core.util.websocket.WebsocketClientEndpoint;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.dao.CurrencyExtendDao;
import org.duniter.elasticsearch.exception.DuplicateIndexIdException;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.*;

/**
 * Created by Benoit on 30/03/2015.
 */
public class WotService extends AbstractService {

    private BlockDao blockDao;
    private CurrencyExtendDao currencyDao;
    private WotRemoteService wotRemoteService;
    private BlockchainService blockchainService;

    @Inject
    public WotService(Duniter4jClient client,
                      PluginSettings settings,
                      ThreadPool threadPool,
                      BlockDao blockDao,
                      CurrencyDao currencyDao,
                      BlockchainService blockchainService,
                      final ServiceLocator serviceLocator){
        super("duniter.wot", client, settings);
        this.client = client;
        this.blockDao = blockDao;
        this.currencyDao = (CurrencyExtendDao) currencyDao;
        this.blockchainService = blockchainService;
        threadPool.scheduleOnStarted(() -> {
            wotRemoteService = serviceLocator.getWotRemoteService();
            setIsReady(true);
        });
    }

    public Map<String, String> getMembers(String currency) {

        currency = safeGetCurrency(currency);

        if (pluginSettings.enableBlockchainIndexation()) {
            BlockchainParameters p = blockchainService.getParameters(currency);
            return blockDao.getMembers(p);
        }
        else {
            // TODO: check if it works !
            return wotRemoteService.getMembersUids(currency);
        }

    }

    /**
     * Return the given currency, or the default currency
     * @param currency
     * @return
     */
    protected String safeGetCurrency(String currency) {
        if (StringUtils.isNotBlank(currency)) return currency;
        return currencyDao.getDefaultCurrencyName();
    }
}
