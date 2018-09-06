package org.duniter.elasticsearch.user.service;

/*-
 * #%L
 * Duniter4j :: ElasticSearch User plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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

import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.model.local.Peers;
import org.duniter.elasticsearch.model.SynchroResult;
import org.duniter.elasticsearch.service.CurrencyService;
import org.duniter.elasticsearch.service.ServiceLocator;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.user.TestResource;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by blavenie on 13/09/17.
 */
public class SynchroServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SynchroServiceTest.class);

    @ClassRule
    public static final TestResource resource = TestResource.create();

    private CurrencyService currencyService;
    private SynchroService service;
    private Peer peer;

    @Before
    public void setUp() throws Exception {
        currencyService = ServiceLocator.instance().getBean(CurrencyService.class);
        service = ServiceLocator.instance().getBean(SynchroService.class);
        peer = new Peer.Builder()
                .setHost(resource.getConfiguration().getDataSyncHost())
                .setPort(resource.getConfiguration().getDataSyncPort())
                .setCurrency(resource.getFixtures().getCurrency())
                .build();

        while(!service.isReady()) {
            Thread.sleep(1000);
        }

        // Init the currency index
        Configuration config = Configuration.instance();
        currencyService.createIndexIfNotExists().indexCurrencyFromPeer(new Peer.Builder()
                .setHost(config.getNodeHost())
                .setPort(config.getNodePort()).build());

        // Init data indices
        ServiceLocator.instance().getBean(UserService.class).createIndexIfNotExists();
        ServiceLocator.instance().getBean(HistoryService.class).createIndexIfNotExists();
        ServiceLocator.instance().getBean(PageService.class).createIndexIfNotExists();
        ServiceLocator.instance().getBean(GroupService.class).createIndexIfNotExists();

        Thread.sleep(5000);
    }

    @Test
    public void synchronizePeer() throws Exception {
        // Set a id (require for saving the synchro execution)
        peer.setId("fake-hash");

        // Set the API (require for synchro)
        peer.setApi(EndpointApi.ES_USER_API.name());

        SynchroResult result = service.synchronizePeer(peer, false);
        Assert.assertNotNull(result);

        Assert.assertTrue(result.getInserts() > 0);
    }

    @Test
    @Ignore
    public void startNode() throws Exception {

        while(true) {
            Thread.sleep(10000);
        }
    }
}
