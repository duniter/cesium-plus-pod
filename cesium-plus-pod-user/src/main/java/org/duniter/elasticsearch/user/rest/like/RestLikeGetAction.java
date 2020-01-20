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

import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.user.dao.page.PageCommentDao;
import org.duniter.elasticsearch.user.dao.page.PageIndexDao;
import org.duniter.elasticsearch.user.dao.page.PageRecordDao;
import org.duniter.elasticsearch.user.dao.profile.UserProfileDao;
import org.duniter.elasticsearch.user.model.LikeRecord;
import org.duniter.elasticsearch.user.service.LikeService;
import org.duniter.elasticsearch.user.service.UserService;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.*;

public class RestLikeGetAction extends BaseRestHandler {

    public static final String PATH_LIKE = "/_likes";
    public static final String PATH_DISLIKE = "/_dislikes";
    public static final String PATH_ABUSE = "/_abuses";

    private RestSecurityController securityController;

    @Inject
    public RestLikeGetAction(Settings settings, RestController controller,
                             RestSecurityController securityController,
                             Client client) {
        super(settings, controller, client);
        this.securityController = securityController;

        for (LikeRecord.Kind kind: LikeRecord.Kind.values()) {
            controller.registerHandler(RestRequest.Method.GET, "/{index}/{type}/{id}/_" + kind.toString().toLowerCase() + "s", this);
        }

        // Allow some indices
        allowLikeIndex(UserService.INDEX, UserProfileDao.TYPE);
        allowLikeIndex(PageIndexDao.INDEX, PageRecordDao.TYPE);
        allowLikeIndex(PageIndexDao.INDEX, PageCommentDao.TYPE);
    }

    public void allowLikeIndex(String index, String type) {
        for (LikeRecord.Kind kind: LikeRecord.Kind.values()) {
            String pathRegexp = String.format("/%s/%s/[^/]+/_%s", index, type, kind.toString().toLowerCase() + "s");
            securityController.allow(RestRequest.Method.GET, pathRegexp);
        }
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel channel, Client client) {
        String index = request.param("index");
        String type = request.param("type");
        String id = request.param("id");

        // Read kind
        LikeRecord.Kind kind;
        if (request.path().endsWith(PATH_DISLIKE)) {
            kind = LikeRecord.Kind.DISLIKE;
        }
        else if (request.path().endsWith(PATH_ABUSE)) {
            kind = LikeRecord.Kind.ABUSE;
        }
        else {
            kind = LikeRecord.Kind.LIKE;
        }

        // Prepare search request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(LikeService.INDEX)
                .setTypes(LikeService.RECORD_TYPE)
                .setFetchSource(false)
                .setSearchType(SearchType.QUERY_AND_FETCH);

        // Query = filter on index/type/id
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_INDEX, index))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_TYPE, type))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_ID, id))
                .filter(QueryBuilders.termQuery(LikeRecord.PROPERTY_KIND, kind.toString()));

        searchRequest.setQuery(QueryBuilders.constantScoreQuery(boolQuery));

        SearchResponse response = searchRequest
                .setSize(0)
                .get();

        Long count = response.getHits().getTotalHits();
        channel.sendResponse(new BytesRestResponse(RestStatus.OK, BytesRestResponse.TEXT_CONTENT_TYPE, count.toString().getBytes()));
    }

}