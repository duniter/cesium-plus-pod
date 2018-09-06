package org.duniter.elasticsearch.subscription.synchro;

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
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.subscription.dao.SubscriptionIndexDao;
import org.duniter.elasticsearch.subscription.dao.execution.SubscriptionExecutionDao;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.synchro.AbstractSynchroAction;
import org.elasticsearch.common.inject.Inject;

public class SynchroSubscriptionExecutionIndexAction extends AbstractSynchroAction {

    @Inject
    public SynchroSubscriptionExecutionIndexAction(Duniter4jClient client,
                                                   PluginSettings pluginSettings,
                                                   CryptoService cryptoService,
                                                   ThreadPool threadPool,
                                                   SynchroService synchroService) {
        super(SubscriptionIndexDao.INDEX, SubscriptionExecutionDao.TYPE, client, pluginSettings.getDelegate(),
                cryptoService, threadPool);


        synchroService.register(this);
    }

    @Override
    public EndpointApi getEndPointApi() {
        return EndpointApi.ES_SUBSCRIPTION_API;
    }
}
