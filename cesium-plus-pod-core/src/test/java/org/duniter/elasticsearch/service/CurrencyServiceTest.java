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


import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.BlockchainRemoteService;
import org.duniter.elasticsearch.TestResource;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Ignore
public class CurrencyServiceTest {

	private static final Logger log = LoggerFactory.getLogger(CurrencyServiceTest.class);

	@ClassRule
	public static final TestResource resource = TestResource.create();

    private BlockchainRemoteService blockchainRemoteService;
    private CurrencyService service;
    private Configuration config;
    private Peer peer;

    @Before
    public void setUp() throws Exception {
        service = ServiceLocator.instance().getBean(CurrencyService.class);
        blockchainRemoteService = ServiceLocator.instance().getBlockchainRemoteService();
        config = Configuration.instance();
        peer = createTestPeer();
    }

    @Test
    public void createIndexIfNotExists() throws Exception {

        // drop and recreate index
        service.deleteIndex().createIndexIfNotExists();
    }

    @Test
	public void indexCurrencyFromPeer() throws Exception {
        service.createIndexIfNotExists()
                .indexCurrencyFromPeer(peer);
	}

	/* -- internal methods */

    protected Peer createTestPeer() {
        Peer peer = new Peer(
                Configuration.instance().getNodeHost(),
                Configuration.instance().getNodePort());

        return peer;
    }

}
