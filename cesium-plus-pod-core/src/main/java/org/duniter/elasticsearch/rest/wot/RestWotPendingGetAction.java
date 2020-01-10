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
import org.duniter.core.client.model.bma.WotPendingMembership;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.local.Member;
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.PendingMembershipService;
import org.duniter.elasticsearch.service.WotService;
import org.duniter.elasticsearch.util.bytes.JsonNodeBytesReference;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.ByteArray;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.List;

/**
 * A GET request similar as /wot/members in Duniter BMA API
 *
 */
public class RestWotPendingGetAction extends BaseRestHandler {

    private PendingMembershipService service;

    @Inject
    public RestWotPendingGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                   PendingMembershipService service) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/wot/pending");

        controller.registerHandler(RestRequest.Method.GET, "/wot/pending", this);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/pending", this);

        this.service = service;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("index");

        int from = request.paramAsInt("from", 0);
        int size = request.paramAsInt("size", 500);

        ObjectMapper objectMapper = getObjectMapper();

        try {

            List<WotPendingMembership> memberships = service.getPendingMemberships(currency, from, size);
            XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                    .startArray("memberships");
            for (WotPendingMembership membership: memberships) {
                builder.rawValue(new JsonNodeBytesReference(membership, objectMapper));
            }
            builder.endArray().endObject();
            channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/wot/members]: %s", ioe.getMessage()), ioe);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}