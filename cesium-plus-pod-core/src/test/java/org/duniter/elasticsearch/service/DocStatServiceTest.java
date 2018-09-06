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
import org.duniter.core.client.model.local.Peer;
import org.duniter.elasticsearch.TestResource;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Ignore (it's work but too long)
@Ignore
public class DocStatServiceTest {

	private static final Logger log = LoggerFactory.getLogger(DocStatServiceTest.class);

	@ClassRule
	public static final TestResource resource = TestResource.create();

    private CurrencyService currencyService;
    private DocStatService service;
    private Configuration config;
    private Peer peer;

    @Before
    public void setUp() throws Exception {
        currencyService = ServiceLocator.instance().getBean(CurrencyService.class);
        service = ServiceLocator.instance().getBean(DocStatService.class);
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
    public void computeStats() throws Exception {

        // Add new stats def
        service.registerIndex(CurrencyService.INDEX, CurrencyService.RECORD_TYPE);

        service.computeStats();

    }
}
