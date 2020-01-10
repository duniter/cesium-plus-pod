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

import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.Map;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNodeStatsGetAction extends BaseRestHandler {

    private final ChangeService changeService;

    @Inject
    public RestNodeStatsGetAction(Settings settings, RestController controller, Client client,
                                  RestSecurityController securityController,
                                  ChangeService changeService) {
        super(settings, controller, client);

        this.changeService = changeService;

        securityController.allow(RestRequest.Method.GET, "/node/stats");
        controller.registerHandler(RestRequest.Method.GET, "/node/stats", this);
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, createStats(request, client)));
    }


    public XContentBuilder createStats(RestRequest request, Client client) {
        try {
            final XContentBuilder mapping = RestXContentBuilder.restContentBuilder(request).startObject()
                    .startObject("stats");

            // Listeners by source
            if (request.paramAsBoolean("listeners", true)) {
                mapping.startArray("listeners");
                Map<String, Integer> sourcesListener = changeService.getUsageStatistics();
                for (Map.Entry<String, Integer> entry : sourcesListener.entrySet()) {
                    mapping.startObject()
                            .field("source", entry.getKey())
                            .field("count", entry.getValue())
                            .endObject();
                }
                mapping.endArray();
            }

            // Add cluster info
            if (request.paramAsBoolean("cluster", true)) {
                ClusterStatsResponse response = client.admin().cluster().prepareClusterStats().execute().actionGet();
                mapping.field("cluster").rawValue(new BytesArray(response.toString()));
            }

            mapping.endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/node/summary]: %s", ioe.getMessage()), ioe);
        }
    }
}