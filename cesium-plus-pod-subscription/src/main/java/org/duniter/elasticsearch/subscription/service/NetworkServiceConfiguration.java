package org.duniter.elasticsearch.subscription.service;

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.elasticsearch.service.NetworkService;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.elasticsearch.common.inject.Inject;

public class NetworkServiceConfiguration implements Bean {


    @Inject
    public NetworkServiceConfiguration(PluginSettings pluginSettings,
                                       NetworkService networkService) {

        if (pluginSettings.enableSubscription()) {
            // Register ES_SUBSCRIPTION_API, as an API to publish inside the peer document
            networkService.registerPeeringPublishApi(EndpointApi.ES_SUBSCRIPTION_API);
        }
    }
}
