package org.duniter.elasticsearch.rest.security;

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

import org.duniter.elasticsearch.security.challenge.ChallengeMessageStore;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

public class RestSecurityGetChallengeAction extends BaseRestHandler {

    private ChallengeMessageStore challengeMessageStore;

    @Inject
    public RestSecurityGetChallengeAction(Settings settings, RestController controller, Client client, ChallengeMessageStore challengeMessageStore) {
        super(settings, controller, client);
        this.challengeMessageStore = challengeMessageStore;
        controller.registerHandler(GET, "/auth", this);
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel restChannel, Client client) throws Exception {
        restChannel.sendResponse(new BytesRestResponse(OK, challengeMessageStore.createNewChallenge()));
    }

}