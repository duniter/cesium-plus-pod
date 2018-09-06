package org.duniter.elasticsearch.user.rest.user;

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
import org.duniter.elasticsearch.user.service.UserEventService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.rest.RestRequest;

/**
 * Created by blavenie on 13/12/16.
 */
public class RestUserEventSearchAction {

    @Inject
    public RestUserEventSearchAction(RestSecurityController securityController) {
        securityController.allow(RestRequest.Method.GET, String.format("/%s/%s/_search", UserEventService.INDEX, UserEventService.EVENT_TYPE));
        securityController.allow(RestRequest.Method.POST, String.format("/%s/%s/_search", UserEventService.INDEX, UserEventService.EVENT_TYPE));
        securityController.allow(RestRequest.Method.GET, String.format("/%s/%s/_count", UserEventService.INDEX, UserEventService.EVENT_TYPE));
        securityController.allow(RestRequest.Method.POST, String.format("/%s/%s/_count", UserEventService.INDEX, UserEventService.EVENT_TYPE));
    }
}
