package org.duniter.elasticsearch.subscription.synchro.record;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Subscription plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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

import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.subscription.synchro.AbstractSynchroSubscriptionAction;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.subscription.dao.SubscriptionIndexDao;
import org.duniter.elasticsearch.subscription.dao.record.SubscriptionRecordDao;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.elasticsearch.common.inject.Inject;

public class SynchroSubscriptionRecordAction extends AbstractSynchroSubscriptionAction {

    @Inject
    public SynchroSubscriptionRecordAction(Duniter4jClient client,
                                           PluginSettings pluginSettings,
                                           ThreadPool threadPool,
                                           CryptoService cryptoService,
                                           SynchroService synchroService) {
        super(SubscriptionIndexDao.INDEX, SubscriptionRecordDao.TYPE, client, pluginSettings, cryptoService, threadPool);

        setEnableUpdate(true); // with update

        synchroService.register(this);
    }

}
