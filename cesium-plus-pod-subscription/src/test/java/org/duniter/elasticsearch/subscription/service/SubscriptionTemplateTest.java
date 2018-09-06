package org.duniter.elasticsearch.subscription.service;

/*
 * #%L
 * Duniter4j :: ElasticSearch Indexer
 * %%
 * Copyright (C) 2014 - 2016 EIS
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

import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.subscription.util.stringtemplate.DateRenderer;
import org.duniter.elasticsearch.subscription.util.stringtemplate.StringRenderer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Benoit on 06/05/2015.
 */
public class SubscriptionTemplateTest {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionTemplateTest.class);

    private static final boolean verbose = false;

    @Test
    public void testHtmlEmail() throws Exception{

        try {
            STGroup group = new STGroupDir("templates", '$', '$');

            group.registerRenderer(Date.class, new DateRenderer());
            group.registerRenderer(String.class, new StringRenderer());

            ST tpl = group.getInstanceOf("html_email_content");
            tpl.add("issuerName", "MyIssuerName");
            tpl.add("issuerPubkey", "5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of");
            tpl.add("url", "https://g1.duniter.fr");
            tpl.add("senderPubkey", "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU");
            tpl.add("senderName", ModelUtils.minifyPubkey("G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU"));
            tpl.addAggr("events.{description, time}", new Object[]{"My event description", new Date()});
            tpl.addAggr("events.{description, time}", new Object[]{"My event description 2", new Date()});
            assertNotNull(tpl);

            String email = tpl.render(new Locale("en", "GB"));

            if (verbose) {
                System.out.println(email);
            }
        }
        catch (Exception e) {
            throw new TechnicalException(e);
        }
    }

    @Test
    public void testTextEmail() throws Exception{

        try {
            STGroup group = new STGroupDir("templates", '$', '$');

            group.registerRenderer(Date.class, new DateRenderer());
            group.registerRenderer(String.class, new StringRenderer());

            ST tpl = group.getInstanceOf("text_email");
            tpl.add("issuerPubkey", "5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of");
            tpl.add("issuerName", "kimamila");
            tpl.add("url", "https://g1.duniter.fr");
            tpl.add("senderPubkey", "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU");
            tpl.add("senderName", ModelUtils.minifyPubkey("G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU"));
            tpl.addAggr("events.{description, time}", new Object[]{"My event description", new Date()});
            assertNotNull(tpl);

            String text = tpl.render(new Locale("en", "GB"));

            if (verbose) {
                System.out.println(text);
            }

        }
        catch (Exception e) {
            throw new TechnicalException(e);
        }
    }
}

