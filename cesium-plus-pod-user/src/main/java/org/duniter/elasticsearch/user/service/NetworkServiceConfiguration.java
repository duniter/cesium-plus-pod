package org.duniter.elasticsearch.user.service;

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.elasticsearch.service.NetworkService;
import org.elasticsearch.common.inject.Inject;

public class NetworkServiceConfiguration implements Bean {


    @Inject
    public NetworkServiceConfiguration(NetworkService networkService) {

        // Register ES_USER_API, as an API to publish inside the peer document
        networkService.registerPeeringPublishApi(EndpointApi.ES_USER_API);
    }
}
