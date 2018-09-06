package org.duniter.elasticsearch.rest.node;

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

import org.duniter.core.client.config.Configuration;
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.rest.AbstractRestPostIndexAction;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.XContentThrowableRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.CurrencyService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.*;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNodeSummaryGetAction extends BaseRestHandler {

    @Inject
    public RestNodeSummaryGetAction(Settings settings, RestController controller, Client client, RestSecurityController securityController) {
        super(settings, controller, client);
        securityController.allow(RestRequest.Method.GET, "/node/summary");
        controller.registerHandler(RestRequest.Method.GET, "/node/summary", this);
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        XContentBuilder content = createSummary();
        channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, content));
    }


    public XContentBuilder createSummary() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
                    .startObject("duniter")

                    // software
                    .field("software", "duniter4j-elasticsearch")

                    // version
                    .field("version", Configuration.instance().getVersion().toString())

                    // status
                    .field("status", RestStatus.OK.getStatus())

                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/node/summary]: %s", ioe.getMessage()), ioe);
        }
    }
}