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
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNodeModeratorsGetAction extends BaseRestHandler {

    private final PluginSettings pluginSettings;

    @Inject
    public RestNodeModeratorsGetAction(PluginSettings pluginSettings, Settings settings, RestController controller, Client client, RestSecurityController securityController) {
        super(settings, controller, client);

        this.pluginSettings = pluginSettings;

        securityController.allow(RestRequest.Method.GET, "/node/moderators");
        controller.registerHandler(RestRequest.Method.GET, "/node/moderators", this);
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        XContentBuilder content = createSummary(request);
        channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, content));
    }


    public XContentBuilder createSummary(RestRequest request) {
        try {
            XContentBuilder mapping = RestXContentBuilder.restContentBuilder(request).startObject()
                    .field("moderators", pluginSettings.getDocumentAdminAndModeratorsPubkeys())
                    .endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/node/moderators]: %s", ioe.getMessage()), ioe);
        }
    }


}