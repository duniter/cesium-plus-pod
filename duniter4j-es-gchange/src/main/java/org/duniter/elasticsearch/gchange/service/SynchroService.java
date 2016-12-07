package org.duniter.elasticsearch.gchange.service;

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

import org.duniter.core.client.model.local.Peer;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.gchange.PluginSettings;
import org.duniter.elasticsearch.service.AbstractSynchroService;
import org.duniter.elasticsearch.service.ServiceLocator;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;

/**
 * Created by blavenie on 27/10/16.
 */
public class SynchroService extends AbstractSynchroService {

    @Inject
    public SynchroService(Client client, PluginSettings settings, CryptoService cryptoService,
                          ThreadPool threadPool, final ServiceLocator serviceLocator) {
        super(client, settings, cryptoService, threadPool, serviceLocator);
    }

    public void synchronize() {
        logger.info("Synchronizing data...");
        Peer peer = getPeerFromAPI("GCHANGE API");
        synchronize(peer);
    }

    /* -- protected methods -- */

    protected void synchronize(Peer peer) {

        long sinceTime = 0; // TODO: get last sync time from somewhere ? (e.g. a specific index)

        logger.info(String.format("[%s] Synchronizing gchange data since %s...", peer.toString(), sinceTime));

        importMarketChanges(peer, sinceTime);
        importRegistryChanges(peer, sinceTime);

        logger.info(String.format("[%s] Synchronizing gchange data since %s [OK]", peer.toString(), sinceTime));
    }

    protected void importMarketChanges(Peer peer, long sinceTime) {
        importChanges(peer, MarketService.INDEX, MarketService.RECORD_TYPE,  sinceTime);
        importChanges(peer, MarketService.INDEX, MarketService.RECORD_COMMENT_TYPE,  sinceTime);
    }

    protected void importRegistryChanges(Peer peer, long sinceTime) {
        importChanges(peer, RegistryService.INDEX, RegistryService.RECORD_TYPE,  sinceTime);
        importChanges(peer, RegistryService.INDEX, RegistryService.RECORD_COMMENT_TYPE,  sinceTime);
    }
}