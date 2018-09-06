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


import org.duniter.core.beans.Bean;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.dao.PeerDao;
import org.duniter.core.client.service.DataContext;
import org.duniter.core.client.service.HttpService;
import org.duniter.core.client.service.HttpServiceImpl;
import org.duniter.core.client.service.bma.*;
import org.duniter.core.client.service.local.CurrencyService;
import org.duniter.core.client.service.local.*;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.service.Ed25519CryptoServiceImpl;
import org.duniter.core.service.MailService;
import org.duniter.core.service.MailServiceImpl;
import org.duniter.elasticsearch.beans.ESBeanFactory;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.dao.impl.BlockDaoImpl;
import org.duniter.elasticsearch.dao.impl.CurrencyDaoImpl;
import org.duniter.elasticsearch.dao.impl.PeerDaoImpl;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.Singleton;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.io.IOException;

@Singleton
public class ServiceLocator
        extends org.duniter.core.client.service.ServiceLocator
        {
    private static final ESLogger logger = ESLoggerFactory.getLogger("duniter.service");

    private static ESBeanFactory beanFactory = null;

    @Inject
    public ServiceLocator() {
        super(getOrCreateBeanFactory());
        if (logger.isDebugEnabled()) {
            logger.debug("Starting Duniter4j ServiceLocator...");
        }

        org.duniter.core.client.service.ServiceLocator.setInstance(this);
    }

    @Override
    public void close() {
        try {
            super.close();
        }
        catch (IOException e) {
            throw new TechnicalException(e);
        }
        org.duniter.core.client.service.ServiceLocator.setInstance(null);
    }

    public static ESBeanFactory getESBeanFactory() {
        return getOrCreateBeanFactory();
    }

    /* -- Internal methods -- */

    public static ESBeanFactory getOrCreateBeanFactory() {
        if (beanFactory != null) {
            return beanFactory;
        }
        beanFactory = new ESBeanFactory();

        beanFactory.bind(BlockchainRemoteService.class, BlockchainRemoteServiceImpl.class)
                .bind(NetworkRemoteService.class, NetworkRemoteServiceImpl.class)
                .bind(WotRemoteService.class, WotRemoteServiceImpl.class)
                .bind(TransactionRemoteService.class, TransactionRemoteServiceImpl.class)
                .bind(CryptoService.class, Ed25519CryptoServiceImpl.class)
                .bind(org.duniter.core.client.service.local.PeerService.class, PeerServiceImpl.class)
                .bind(MailService.class, MailServiceImpl.class)
                .bind(CurrencyService.class, CurrencyServiceImpl.class)
                .bind(NetworkService.class, NetworkServiceImpl.class)
                .bind(HttpService.class, HttpServiceImpl.class)
                // Dao
                .bind(CurrencyDao.class, CurrencyDaoImpl.class)
                .bind(PeerDao.class, PeerDaoImpl.class)
                .bind(BlockDao.class, BlockDaoImpl.class)

                .add(DataContext.class);

        return beanFactory;
    }

    public static class Provider<T extends Bean> implements org.elasticsearch.common.inject.Provider<T> {

        private final Class<T> clazz;

        public Provider(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T get() {
            return getOrCreateBeanFactory().getBean(clazz);
        }
    }
}
