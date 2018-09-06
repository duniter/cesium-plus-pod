package org.duniter.elasticsearch.service;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
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

import org.duniter.core.beans.Bean;
import org.duniter.core.client.service.DataContext;
import org.duniter.core.client.service.HttpService;
import org.duniter.core.client.service.bma.BlockchainRemoteService;
import org.duniter.core.client.service.bma.NetworkRemoteService;
import org.duniter.core.client.service.bma.TransactionRemoteService;
import org.duniter.core.client.service.bma.WotRemoteService;
import org.duniter.core.client.service.local.CurrencyService;
import org.duniter.core.service.CryptoService;
import org.duniter.core.service.MailService;
import org.duniter.elasticsearch.PluginInit;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Module;

public class ServiceModule extends AbstractModule implements Module {

    @Override protected void configure() {
        bind(ServiceLocator.class).asEagerSingleton();

        // common services
        bind(PluginSettings.class).asEagerSingleton();
        bind(ThreadPool.class).asEagerSingleton();
        bind(PluginInit.class).asEagerSingleton();
        bind(ChangeService.class).asEagerSingleton();
        bind(DocStatService.class).asEagerSingleton();
        bind(SynchroService.class).asEagerSingleton();

        // blockchain indexation services
        bind(BlockchainService.class).asEagerSingleton();
        bind(BlockchainListenerService.class).asEagerSingleton();
        bind(PeerService.class).asEagerSingleton();

        // Duniter Client API beans
        bindWithLocator(BlockchainRemoteService.class);
        bindWithLocator(NetworkRemoteService.class);
        bindWithLocator(WotRemoteService.class);
        bindWithLocator(TransactionRemoteService.class);
        bindWithLocator(org.duniter.core.client.service.local.PeerService.class);
        bindWithLocator(CurrencyService.class);
        bindWithLocator(HttpService.class);
        //bindWithLocator(CurrencyDao.class);
        //bindWithLocator(PeerDao.class);
        bindWithLocator(DataContext.class);

        // Duniter Shared API beans
        bindWithLocator(CryptoService.class);
        bindWithLocator(MailService.class);
    }



    /* protected methods */

    protected <T extends Bean> void bindWithLocator(Class<T> clazz) {
        bind(clazz).toProvider(new ServiceLocator.Provider<>(clazz));
    }

}