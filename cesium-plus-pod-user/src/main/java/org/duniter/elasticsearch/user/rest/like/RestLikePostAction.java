package org.duniter.elasticsearch.user.rest.like;

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

import org.duniter.core.exception.BusinessException;
import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.duniter.elasticsearch.rest.XContentThrowableRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.user.dao.page.PageCommentDao;
import org.duniter.elasticsearch.user.dao.page.PageIndexDao;
import org.duniter.elasticsearch.user.dao.page.PageRecordDao;
import org.duniter.elasticsearch.user.dao.profile.UserProfileDao;
import org.duniter.elasticsearch.user.model.LikeRecord;
import org.duniter.elasticsearch.user.service.LikeService;
import org.duniter.elasticsearch.user.service.UserService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.OK;

public class RestLikePostAction extends BaseRestHandler {

    private final ESLogger log;

    private RestSecurityController securityController;

    private LikeService service;

    @Inject
    public RestLikePostAction(Settings settings, RestController controller, Client client,
                              RestSecurityController securityController, LikeService service) {
        super(settings, controller, client);
        this.securityController = securityController;
        this.service = service;

        log = Loggers.getLogger("duniter.rest.like", settings, String.format("[%s]", LikeService.INDEX));

        controller.registerHandler(POST,"/{index}/{type}/{id}/_like",this);

        // Allow some indices
        allowLikeIndex(UserService.INDEX, UserProfileDao.TYPE);
        allowLikeIndex(PageIndexDao.INDEX, PageRecordDao.TYPE);
        allowLikeIndex(PageIndexDao.INDEX, PageCommentDao.TYPE);

    }

    public void allowLikeIndex(String index, String type) {
        for (LikeRecord.Kind kind: LikeRecord.Kind.values()) {
            String pathRegexp = String.format("/%s/%s/[^/]+/_like", index, type, kind.toString().toLowerCase() + "s");
            securityController.allow(RestRequest.Method.POST, pathRegexp);
        }
    }

    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        try {
            String id = service.indexLikeFromJson(request.content().toUtf8(), false);
            channel.sendResponse(new BytesRestResponse(OK, id));
        }
        catch(DuniterElasticsearchException | BusinessException e) {
            log.error(e.getMessage(), e);
            channel.sendResponse(new XContentThrowableRestResponse(request, e));
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}