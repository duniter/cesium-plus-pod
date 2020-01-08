package org.duniter.elasticsearch.model;

import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.HttpService;
import org.duniter.core.client.service.HttpServiceImpl;
import org.duniter.core.test.TestResource;
import org.duniter.elasticsearch.service.CurrencyService;
import org.duniter.elasticsearch.service.ServiceLocator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class SearchScrollResponseTest {


    @ClassRule
    public static TestResource testResource = org.duniter.elasticsearch.TestResource.create();

    private HttpService service;

    @Before
    public void setUp() throws Exception {
        service = ServiceLocator.instance().getBean(HttpService.class);
        ((HttpServiceImpl)service).afterPropertiesSet();
    }

    @Test
    public void deserialize() {
        Peer peer = Peer.newBuilder().setHost("g1-test.data.duniter.fr").setPort(443).setUseSsl(true).build();
        SearchScrollResponse response = service.executeRequest(peer, "/user/profile/_search?scroll=10s&size=1", SearchScrollResponse.class);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getScrollId());
    }
}
