package org.duniter.elasticsearch.subscription.service;

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.util.CollectionUtils;
import org.duniter.elasticsearch.service.NetworkService;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.elasticsearch.common.inject.Inject;

public class NetworkServiceConfiguration implements Bean {


    @Inject
    public NetworkServiceConfiguration(PluginSettings pluginSettings,
                                       NetworkService networkService) {
        // Register ES_USER_API, if list of APIs has not already defined in settings
        if (CollectionUtils.isEmpty(pluginSettings.getPeeringPublishedApis())
                && pluginSettings.enableSubscription()) {
            networkService.addPublishEndpointApi(EndpointApi.ES_SUBSCRIPTION_API);
        }
    }
}
