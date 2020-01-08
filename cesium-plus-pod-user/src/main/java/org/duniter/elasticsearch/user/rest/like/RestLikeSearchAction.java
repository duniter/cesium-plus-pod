package org.duniter.elasticsearch.user.rest.like;

/*
 * #%L
 * Duniter4j :: ElasticSearch User plugin
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

import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.user.service.LikeService;
import org.elasticsearch.common.inject.Inject;

/**
 * Created by blavenie on 13/12/16.
 */
public class RestLikeSearchAction {

    @Inject
    public RestLikeSearchAction(RestSecurityController securityController) {
        securityController
                .allowGetSearchIndexType(LikeService.INDEX, LikeService.RECORD_TYPE)
                .allowPostSearchIndexType(LikeService.INDEX, LikeService.RECORD_TYPE);
    }
}
