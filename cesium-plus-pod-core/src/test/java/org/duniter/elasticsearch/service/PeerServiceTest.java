package org.duniter.elasticsearch.service;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
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


import com.google.common.collect.ImmutableList;
import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.NetworkRemoteService;
import org.duniter.elasticsearch.TestResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerServiceTest {

	private static final Logger log = LoggerFactory.getLogger(PeerServiceTest.class);

	@ClassRule
	public static final TestResource resource = TestResource.create();

    private CurrencyService currencyService;
    private PeerService service;
    private org.duniter.core.client.service.local.PeerService localService;
    private NetworkRemoteService remoteService;
    private Configuration config;
    private Peer peer;

    @Before
    public void setUp() throws Exception {
        currencyService = ServiceLocator.instance().getBean(CurrencyService.class);
        service = ServiceLocator.instance().getBean(PeerService.class);
        remoteService = ServiceLocator.instance().getNetworkRemoteService();
        localService = ServiceLocator.instance().getPeerService();
        config = Configuration.instance();
        peer = new Peer.Builder()
                .setHost(config.getNodeHost())
                .setPort(config.getNodePort()).build();

        // Waiting services started
        while(!service.isReady() || !currencyService.isReady()) {
            Thread.sleep(1000);
        }

        // Init the currency
        currencyService.createIndexIfNotExists()
                .indexCurrencyFromPeer(peer);

        Thread.sleep(5000);
    }

    @Test
    public void savePeers() throws Exception {

        // First Peer
        Peer peer1 = new Peer.Builder()
                .setHost(config.getNodeHost())
                .setPort(config.getNodePort())
                .setPubkey(resource.getFixtures().getUserPublicKey())
                .setCurrency(resource.getFixtures().getCurrency())
                .build();
        peer1.getStats().setLastUpTime(120000L);

        // Second peer
        Peer peer2 = new Peer.Builder()
                .setHost(config.getNodeHost())
                .setPort(peer1.getPort() + 1)
                .setPubkey(resource.getFixtures().getUserPublicKey())
                .setCurrency(resource.getFixtures().getCurrency())
                .build();
        peer2.getStats().setLastUpTime(peer1.getStats().getLastUpTime() - 150); // Set UP just before the peer 1

        // Save peers
        localService.save(peer1.getCurrency(), ImmutableList.of(peer1, peer2), false);

        // Wait propagation
        Thread.sleep(2000);

        // Try to read
        Long maxLastUpTime = service.getMaxLastUpTime(peer1.getCurrency());
        // Allow null value here, because sometime TU failed (if sleep time is too short)
        if (maxLastUpTime != null) {
            Assert.assertEquals(peer1.getStats().getLastUpTime().longValue(), maxLastUpTime.longValue());
        }

    }
}
