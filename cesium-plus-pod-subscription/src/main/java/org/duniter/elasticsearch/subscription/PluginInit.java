package org.duniter.elasticsearch.subscription;

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

import org.duniter.elasticsearch.subscription.dao.SubscriptionIndexDao;
import org.duniter.elasticsearch.subscription.service.SubscriptionService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;

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
        this.logger = Loggers.getLogger("duniter.subscription", settings, new String[0]);
        this.pluginSettings = pluginSettings;
        this.threadPool = threadPool;
        this.injector = injector;
    }

    @Override
    protected void doStart() {
        threadPool.scheduleOnClusterReady(() -> {
            createIndices();

            // Waiting cluster back to GREEN or YELLOW state, before synchronizePeer
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

        boolean reloadIndices = pluginSettings.reloadIndices();

        if (reloadIndices) {
            if (logger.isInfoEnabled()) {
                logger.info("Reloading indices...");
            }
            injector.getInstance(SubscriptionIndexDao.class)
                    .deleteIndex()
                    .createIndexIfNotExists();

            if (logger.isInfoEnabled()) {
                logger.info("Reloading indices [OK]");
            }
        }
        else {
            if (logger.isDebugEnabled()) {
                logger.debug("Checking indices...");
            }
            injector.getInstance(SubscriptionIndexDao.class).createIndexIfNotExists();

            if (logger.isDebugEnabled()) {
                logger.debug("Checking indices [OK]");
            }
        }
    }

    protected void doAfterStart() {

        // Start subscription services
        if (pluginSettings.enableSubscription()) {
            injector.getInstance(SubscriptionService.class)
                    .startScheduling();
        }
    }

}
