package org.duniter.elasticsearch.subscription.synchro;

import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.synchro.AbstractSynchroAction;
import org.duniter.elasticsearch.threadpool.ThreadPool;

public abstract class AbstractSynchroSubscriptionAction extends AbstractSynchroAction {

    private String endpointApi;

    public AbstractSynchroSubscriptionAction(String index, String type,
                                             Duniter4jClient client,
                                             PluginSettings pluginSettings,
                                             CryptoService cryptoService,
                                             ThreadPool threadPool) {
        super(index, type, index, type, client, pluginSettings.getDelegate().getDelegate(), cryptoService, threadPool);

        // Define endpoint API to used by synchronization, to select peers to request
        this.endpointApi = pluginSettings.getSubscriptionEndpointApi();

    }

    public AbstractSynchroSubscriptionAction(String fromIndex, String fromType,
                                             String toIndex, String toType,
                                             Duniter4jClient client,
                                             PluginSettings pluginSettings,
                                             CryptoService cryptoService,
                                             ThreadPool threadPool) {
        super(fromIndex, fromType, toIndex, toType, client, pluginSettings.getDelegate().getDelegate(), cryptoService, threadPool);

        // Define endpoint API to used by synchronization, to select peers to request
        this.endpointApi = pluginSettings.getSubscriptionEndpointApi();
    }

    @Override
    public String getEndPointApi() {
        return endpointApi;
    }
}
