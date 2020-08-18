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
import org.duniter.core.client.model.bma.WotRequirements;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.WotService;
import org.duniter.elasticsearch.util.bytes.JsonNodeBytesReference;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.List;

/**
 * A GET request similar as /wot/members in Duniter BMA API
 *
 */
public class RestWotRequirementsGetAction extends BaseRestHandler {

    private WotService wotService;

    @Inject
    public RestWotRequirementsGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController,
                                        WotService wotService) {
        super(settings, controller, client);

        securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/wot/requirements/[^/]+");

        controller.registerHandler(RestRequest.Method.GET, "/wot/requirements/{pubkey}", this);
        controller.registerHandler(RestRequest.Method.GET, "/{currency}/wot/requirements/{pubkey}", this);

        this.wotService = wotService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("currency");
        String pubkey = request.param("pubkey");

        try {
            List<WotRequirements> requirements = wotService.getRequirements(currency, pubkey);
            if (requirements == null) {
                XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                        .field("ucode", 2021)
                        .field("message", "No identity matching this pubkey or uid")
                        .endObject();
                channel.sendResponse(new XContentRestResponse(request, RestStatus.BAD_REQUEST, builder));
            }
            else {
                ObjectMapper objectMapper = getObjectMapper();
                XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                        .startArray("identities");
                for (WotRequirements requirement: requirements) {
                    builder.rawValue(new JsonNodeBytesReference(requirement, objectMapper));
                }
                builder.endArray().endObject();
                channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));

            }
        }
        catch(IOException ioe) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error while generating JSON for [/wot/requirements] from remote peer: " + ioe.getMessage(), ioe);
            }
            else {
                logger.error("Error while generating JSON for [/wot/requirements] from remote peer: " + ioe.getMessage());
            }
            XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                    .field("ucode", 2021)
                    .field("message", "Internal error")
                    .endObject();
            channel.sendResponse(new XContentRestResponse(request, RestStatus.INTERNAL_SERVER_ERROR, builder));
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }
}