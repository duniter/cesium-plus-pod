package org.duniter.elasticsearch.user.rest.message.compat;

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

import org.duniter.elasticsearch.user.service.MessageService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.search.RestSearchAction;
import org.elasticsearch.rest.action.support.RestActions;
import org.elasticsearch.rest.action.support.RestStatusToXContentListener;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;

/**
 * /message/record has been replaced by /message/inbox
 * @deprecated
 */
@Deprecated
public class RestMessageRecordSearchAction extends BaseRestHandler {

    @Inject
    public RestMessageRecordSearchAction(Settings settings, RestController controller, Client client) {
        super(settings, controller, client);
        controller.registerHandler(GET, String.format("%s/%s/_search", MessageService.INDEX, MessageService.RECORD_TYPE), this);
        controller.registerHandler(POST, String.format("%s/%s/_search", MessageService.INDEX, MessageService.RECORD_TYPE), this);
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel channel, Client client) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        BytesReference restContent = RestActions.hasBodyContent(request) ? RestActions.getRestContent(request) : null;
        RestSearchAction.parseSearchRequest(searchRequest, request, parseFieldMatcher, restContent);
        searchRequest.indices(MessageService.INDEX); // override type
        searchRequest.types(MessageService.INBOX_TYPE); // override type
        client.search(searchRequest, new RestStatusToXContentListener<>(channel));
    }
}