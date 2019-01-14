package org.duniter.elasticsearch.rest.wot;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.BlockchainService;
import org.duniter.elasticsearch.service.WotService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.Map;

/**
 * A GET request similar as /wot/members in Duniter BMA API
 *
 */
public class RestWotMembersGetAction extends BaseRestHandler {

    private Client client;
    private WotService wotService;

    @Inject
    public RestWotMembersGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                   WotService wotService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/wot/members");

        controller.registerHandler(RestRequest.Method.GET, "/wot/members", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/members", this);

        this.client = client;
        this.wotService = wotService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("index");


        try {
            Map<String, String> members = wotService.getMembers(currency);
            XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                    .startArray("results");
            for (Map.Entry<String, String> entry: members.entrySet()) {
                builder.startObject()
                        .field("pubkey", entry.getKey())
                        .field("uid", entry.getValue())
                        .endObject();
            }
            builder.endArray().endObject();
            channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/wot/lookup]: %s", ioe.getMessage()), ioe);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}