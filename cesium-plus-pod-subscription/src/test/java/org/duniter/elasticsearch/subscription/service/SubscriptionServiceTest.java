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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.local.Wallet;
import org.duniter.core.client.service.ServiceLocator;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.crypto.CryptoUtils;
import org.duniter.core.util.json.JsonAttributeParser;
import org.duniter.core.util.url.URLs;
import org.duniter.elasticsearch.subscription.TestResource;
import org.duniter.elasticsearch.subscription.model.email.EmailSubscription;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserEventCodes;
import org.duniter.elasticsearch.user.service.UserEventService;
import org.junit.*;
import org.nuiton.i18n.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Benoit on 06/05/2015.
 */
public class SubscriptionServiceTest {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceTest.class);

    @ClassRule
    public static final TestResource resource = TestResource.create();

    private SubscriptionService service;
    private UserEventService userEventService;
    private CryptoService cryptoService;

    @Before
    public void setUp() throws Exception {
        service = ServiceLocator.instance().getBean(SubscriptionService.class);
        cryptoService = ServiceLocator.instance().getCryptoService();
        userEventService = ServiceLocator.instance().getBean(UserEventService.class);
    }

    @Test
    public void create() throws JsonProcessingException {
        Wallet wallet = createTestWallet();

        createAndIndexSubscription(wallet);

    }

    @Test
    public void executeEmailSubscriptions() throws Exception{
        Wallet wallet = createTestWallet();
        try {
            createAndIndexSubscription(wallet);
        } catch(Exception e) {
            Assume.assumeNoException(e);
        }

        userEventService.indexEvent(Locale.getDefault(),
                UserEvent.newBuilder(
                        UserEvent.EventType.INFO,
                        UserEventCodes.MEMBER_JOIN.name())
                        .setRecipient(wallet.getPubKeyHash())
                        .build())
            .get();

        // wait 10s
        Thread.sleep(10000);

        service.executeEmailSubscriptions(EmailSubscription.Frequency.daily);

        // wait 10s
        Thread.sleep(10000);
    }

    @Test
    @Ignore
    public void startNode() throws Exception {

        while(true) {
            Thread.sleep(10000);
        }
    }

    /* -- internal methods -- */

    protected Wallet createTestWallet() {
        Wallet wallet = new Wallet(
                resource.getFixtures().getCurrency(),
                resource.getFixtures().getUid(),
                CryptoUtils.decodeBase58(resource.getFixtures().getUserPublicKey()),
                CryptoUtils.decodeBase58(resource.getFixtures().getUserSecretKey()));

        return wallet;
    }

    protected EmailSubscription createAndIndexSubscription(Wallet wallet) throws JsonProcessingException {

        EmailSubscription subscription = createEmailSubscription(wallet);

        // Compute full JSON (with hash + signature)
        String json = JacksonUtils.getThreadObjectMapper().writeValueAsString(subscription);

        String id = service.create(json);
        Assert.assertNotNull(id);

        subscription.setId(id);
        return subscription;
    }

    protected EmailSubscription createEmailSubscription(Wallet wallet) throws JsonProcessingException {

        ObjectMapper objectMapper = JacksonUtils.getThreadObjectMapper();

        EmailSubscription subscription = new EmailSubscription();
        subscription.setVersion(2);
        subscription.setIssuer(wallet.getPubKeyHash());
        subscription.setTime(System.currentTimeMillis()/1000);
        subscription.setRecipient(resource.getPluginSettings().getNodePubkey());
        subscription.setType(EmailSubscription.TYPE);

        // Encrypt email then fill
        String email = resource.getPluginSettings().getMailAdmin();
        byte[] nonce = cryptoService.getBoxRandomNonce();

        EmailSubscription.Content content = EmailSubscription.newContent();
        content.setEmail(email);
        //content.setFrequency(EmailSubscription.Frequency.daily);
        String jsonContent = objectMapper.writeValueAsString(content);

        String cypherContent = cryptoService.box(jsonContent, nonce, wallet.getSecKey(), wallet.getPubKey());
        subscription.setRecipientContent(cypherContent);
        subscription.setNonce(CryptoUtils.encodeBase58(nonce));

        // Fill hash + signature
        String json = objectMapper.writeValueAsString(subscription);

        json = JsonAttributeParser.newStringParser(Record.PROPERTY_SIGNATURE).removeFromJson(json);
        json = JsonAttributeParser.newStringParser(Record.PROPERTY_HASH).removeFromJson(json);

        String hash = cryptoService.hash(json);
        subscription.setHash(hash);
        subscription.setSignature(cryptoService.sign(hash, wallet.getSecKey()));

        return subscription;
    }
}

