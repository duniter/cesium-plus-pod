package org.duniter.elasticsearch.user;

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

import org.duniter.elasticsearch.service.DocStatService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.dao.group.GroupCommentDao;
import org.duniter.elasticsearch.user.dao.group.GroupIndexDao;
import org.duniter.elasticsearch.user.dao.group.GroupRecordDao;
import org.duniter.elasticsearch.user.dao.page.PageCommentDao;
import org.duniter.elasticsearch.user.dao.page.PageIndexDao;
import org.duniter.elasticsearch.user.dao.page.PageRecordDao;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.duniter.elasticsearch.user.service.*;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.nuiton.i18n.I18n;

/**
 * Created by blavenie on 17/06/16.
 */
public class PluginInit extends AbstractLifecycleComponent<PluginInit> {

    private final PluginSettings pluginSettings;
    private final ThreadPool threadPool;
    private final Injector injector;
    private final ESLogger logger;
    private final String clusterName;

    @Inject
    public PluginInit(Settings settings, PluginSettings pluginSettings, ThreadPool threadPool, final Injector injector) {
        super(settings);
        this.logger = Loggers.getLogger("duniter.user", settings, new String[0]);
        this.pluginSettings = pluginSettings;
        this.threadPool = threadPool;
        this.injector = injector;
        this.clusterName = settings.get("cluster.name");
    }

    @Override
    protected void doStart() {
        threadPool.scheduleOnClusterReady(() -> {
            createIndices();

            // Waiting cluster back to GREEN or YELLOW state, before doAfterStart
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

        // Reload all indices
        if (pluginSettings.reloadAllIndices()) {
            if (logger.isInfoEnabled()) {
                logger.info("Reloading indices...");
            }
            injector.getInstance(HistoryService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();
            injector.getInstance(MessageService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();
            injector.getInstance(UserService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();
            injector.getInstance(GroupService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();
            injector.getInstance(UserInvitationService.class)
                    .deleteIndex()
                    .createIndexIfNotExists();
            injector.getInstance(PageService.class)
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

            boolean cleanBlockchainUserEvents = injector.getInstance(UserService.class).isIndexExists() && pluginSettings.reloadBlockchainIndices();

            injector.getInstance(HistoryService.class).createIndexIfNotExists();
            injector.getInstance(UserService.class).createIndexIfNotExists();
            injector.getInstance(MessageService.class).createIndexIfNotExists();
            injector.getInstance(GroupService.class).createIndexIfNotExists();
            injector.getInstance(UserInvitationService.class).createIndexIfNotExists();
            injector.getInstance(PageService.class).createIndexIfNotExists();

            if (logger.isDebugEnabled()) {
                logger.debug("Checking indices [OK]");
            }

            // Clean user events on blockchain
            if (cleanBlockchainUserEvents) {
                int blockNumber = pluginSettings.reloadBlockchainIndicesFrom();
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("Deleting user events on blockchain from block #%s (blockchain will be reload)...", blockNumber));
                }

                // Delete events that reference a block
                injector.getInstance(UserEventService.class)
                        .deleteBlockEventsFrom(blockNumber);
                if (logger.isInfoEnabled()) {
                    logger.info("Deleting user events on blockchain [OK]");
                }
            }
        }

        // Register stats on indices
        if (pluginSettings.enableDocStats()) {
            injector.getInstance(DocStatService.class)
                    .registerIndex(UserService.INDEX, UserService.PROFILE_TYPE)
                    .registerIndex(UserService.INDEX, UserService.SETTINGS_TYPE)
                    .registerIndex(MessageService.INDEX, MessageService.INBOX_TYPE)
                    .registerIndex(MessageService.INDEX, MessageService.OUTBOX_TYPE)
                    .registerIndex(UserInvitationService.INDEX, UserInvitationService.CERTIFICATION_TYPE)
                    .registerIndex(UserEventService.INDEX, UserEventService.EVENT_TYPE)
                    .registerIndex(PageIndexDao.INDEX, PageRecordDao.TYPE)
                    .registerIndex(PageIndexDao.INDEX, PageCommentDao.TYPE)
                    .registerIndex(GroupIndexDao.INDEX, GroupRecordDao.TYPE)
                    .registerIndex(GroupIndexDao.INDEX, GroupCommentDao.TYPE)
                    .registerIndex(HistoryService.INDEX, HistoryService.DELETE_TYPE)
            ;
        }

    }

    protected void doAfterStart() {

        // Notify admin
        injector.getInstance(AdminService.class)
                .notifyAdmin(new UserEvent(
                        UserEvent.EventType.INFO,
                        UserEventCodes.NODE_STARTED.name(),
                        I18n.n("duniter.user.event.NODE_STARTED"),
                        clusterName));
    }


}
