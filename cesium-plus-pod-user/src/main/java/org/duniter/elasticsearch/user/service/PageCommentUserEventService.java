package org.duniter.elasticsearch.user.service;

/*
 * #%L
 * UCoin Java Client :: Core API
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


import com.google.common.collect.ImmutableList;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.dao.group.GroupCommentDao;
import org.duniter.elasticsearch.user.dao.group.GroupIndexDao;
import org.duniter.elasticsearch.user.dao.page.PageCommentDao;
import org.duniter.elasticsearch.user.dao.page.PageIndexDao;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

import java.util.Collection;
import java.util.List;

/**
 * Created by Benoit on 30/03/2015.
 */
public class PageCommentUserEventService extends AbstractCommentUserEventService {

    static {
        I18n.n("duniter.page.error.comment.recordNotFound");
        I18n.n("duniter.page.event.NEW_COMMENT");
        I18n.n("duniter.page.event.UPDATE_COMMENT");
        I18n.n("duniter.page.event.NEW_REPLY_COMMENT");
        I18n.n("duniter.page.event.UPDATE_REPLY_COMMENT");
        I18n.n("duniter.page.event.FOLLOW_NEW_COMMENT");
        I18n.n("duniter.page.event.FOLLOW_UPDATE_COMMENT");
    }
    private final List<ChangeSource> changeListenSources;

    @Inject
    public PageCommentUserEventService(Duniter4jClient client,
                                       PluginSettings settings,
                                       CryptoService cryptoService,
                                       UserService userService,
                                       UserEventService userEventService,
                                       LikeService likeService) {
        super("duniter.event.comment", client, settings, cryptoService, userService, userEventService, likeService);
        this.changeListenSources = ImmutableList.of(
                new ChangeSource(GroupIndexDao.INDEX, GroupCommentDao.TYPE),
                new ChangeSource(PageIndexDao.INDEX, PageCommentDao.TYPE));
        ChangeService.registerListener(this);

    }

    @Override
    public String getId() {
        return "duniter.event.comment";
    }

    @Override
    public Collection<ChangeSource> getChangeSources() {
        return changeListenSources;
    }

}
