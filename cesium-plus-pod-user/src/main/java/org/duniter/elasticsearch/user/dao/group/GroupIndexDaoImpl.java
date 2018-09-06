package org.duniter.elasticsearch.user.dao.group;

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
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.dao.AbstractIndexDao;
import org.duniter.elasticsearch.dao.handler.AddSequenceAttributeHandler;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.dao.CommentDao;
import org.duniter.elasticsearch.user.dao.RecordDao;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * Created by blavenie on 03/04/17.
 */
public class GroupIndexDaoImpl extends AbstractIndexDao<GroupIndexDao> implements GroupIndexDao {


    private PluginSettings pluginSettings;
    private RecordDao recordDao;
    private CommentDao commentDao;

    @Inject
    public GroupIndexDaoImpl(PluginSettings pluginSettings, GroupRecordDao recordDao, GroupCommentDao commentDao) {
        super(INDEX);

        this.pluginSettings = pluginSettings;
        this.commentDao = commentDao;
        this.recordDao = recordDao;
    }

    @Override
    protected void createIndex() throws JsonProcessingException {
        logger.info(String.format("Creating index [%s]", getIndex()));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(getIndex());
        org.elasticsearch.common.settings.Settings indexSettings = org.elasticsearch.common.settings.Settings.settingsBuilder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1)
                //.put("analyzer", createDefaultAnalyzer())
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(recordDao.getType(), recordDao.createTypeMapping());
        createIndexRequestBuilder.addMapping(commentDao.getType(), commentDao.createTypeMapping());
        createIndexRequestBuilder.execute().actionGet();
    }

}
