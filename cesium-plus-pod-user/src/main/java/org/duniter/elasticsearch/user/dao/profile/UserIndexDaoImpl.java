package org.duniter.elasticsearch.user.dao.profile;

/*
 * #%L
 * Ğchange Pod :: ElasticSearch plugin
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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.duniter.elasticsearch.dao.AbstractIndexDao;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.service.UserEventService;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.common.inject.Inject;

/**
 * Created by blavenie on 03/04/17.
 */
public class UserIndexDaoImpl extends AbstractIndexDao<UserIndexDao> implements UserIndexDao {


    private PluginSettings pluginSettings;
    private UserProfileDao profileDao;
    private UserSettingsDao settingsDao;

    @Inject
    public UserIndexDaoImpl(PluginSettings pluginSettings, UserProfileDao profileDao, UserSettingsDao settingsDao) {
        super(INDEX);

        this.pluginSettings = pluginSettings;
        this.settingsDao = settingsDao;
        this.profileDao = profileDao;
    }

    /**
     * Create index for mail
     * @throws JsonProcessingException
     */
    public void createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s]", getIndex()));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(getIndex());
        org.elasticsearch.common.settings.Settings indexSettings = org.elasticsearch.common.settings.Settings.settingsBuilder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1)
                //.put("analyzer", createDefaultAnalyzer())
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(profileDao.getType(), profileDao.createTypeMapping());
        createIndexRequestBuilder.addMapping(settingsDao.getType(), settingsDao.createTypeMapping());
        createIndexRequestBuilder.addMapping(UserEventService.EVENT_TYPE, UserEventService.createEventType());
        createIndexRequestBuilder.execute().actionGet();
    }
}
