package org.duniter.elasticsearch.user.service;

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.util.CollectionUtils;
import org.duniter.elasticsearch.service.NetworkService;
import org.duniter.elasticsearch.user.PluginSettings;
import org.elasticsearch.common.inject.Inject;

public class NetworkServiceConfiguration implements Bean {


    @Inject
    public NetworkServiceConfiguration(PluginSettings pluginSettings,
                                       NetworkService networkService) {

        // Register ES_USER_API, if list of APIs has not already defined in settings
        if (CollectionUtils.isEmpty(pluginSettings.getPeeringPublishedApis())) {
            networkService.addPublishEndpointApi(EndpointApi.ES_USER_API);
        }
    }
}
