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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.duniter.core.client.service.ServiceLocator;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.security.challenge.ChallengeMessageStore;
import org.duniter.elasticsearch.security.token.SecurityTokenStore;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.FORBIDDEN;
import static org.elasticsearch.rest.RestStatus.OK;

public class RestSecurityAuthAction extends BaseRestHandler {

    private static final ESLogger log = ESLoggerFactory.getLogger(RestSecurityAuthAction.class.getName());

    private ChallengeMessageStore challengeMessageStore;
    private SecurityTokenStore securityTokenStore;
    private ObjectMapper objectMapper;

    @Inject
    public RestSecurityAuthAction(Settings settings, RestController controller, Client client,
                                  RestSecurityController securityController,
                                  ChallengeMessageStore challengeMessageStore,
                                  SecurityTokenStore securityTokenStore) {
        super(settings, controller, client);
        this.challengeMessageStore = challengeMessageStore;
        this.securityTokenStore = securityTokenStore;
        this.objectMapper = new ObjectMapper();
        controller.registerHandler(POST, "/auth", this);
        securityController.allow(POST, "/auth");
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel restChannel, Client client) throws Exception {

        AuthData authData = objectMapper.readValue(request.content().toUtf8(), AuthData.class);

        // TODO Authorization: Basic   instead ?

        if (StringUtils.isNotBlank(authData.pubkey)) {
            if (challengeMessageStore.validateChallenge(authData.challenge)) {
                boolean signatureOK = ServiceLocator.instance().getCryptoService().verify(authData.challenge, authData.signature, authData.pubkey);
                if (signatureOK) {
                    String token = securityTokenStore.createNewToken(authData.challenge, authData.signature, authData.pubkey);
                    restChannel.sendResponse(new BytesRestResponse(OK, token));
                    return;
                }
            }
        }

        restChannel.sendResponse(new BytesRestResponse(FORBIDDEN, Boolean.FALSE.toString()));
    }

    class AuthData {
        public String pubkey;
        public String challenge;
        public String signature;
    }
}