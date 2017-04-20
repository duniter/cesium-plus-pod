package org.duniter.elasticsearch.subscription.service;

/*
 * #%L
 * UCoin Java Client :: Core API
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


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.crypto.CryptoUtils;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.duniter.elasticsearch.subscription.dao.execution.SubscriptionExecutionDao;
import org.duniter.elasticsearch.subscription.dao.record.SubscriptionRecordDao;
import org.duniter.elasticsearch.subscription.model.SubscriptionExecution;
import org.duniter.elasticsearch.subscription.model.SubscriptionRecord;
import org.duniter.elasticsearch.subscription.model.email.EmailSubscription;
import org.duniter.elasticsearch.subscription.util.DateUtils;
import org.duniter.elasticsearch.subscription.util.stringtemplate.DateRenderer;
import org.duniter.elasticsearch.subscription.util.stringtemplate.StringRenderer;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.service.AdminService;
import org.duniter.elasticsearch.user.service.MailService;
import org.duniter.elasticsearch.user.service.UserEventService;
import org.duniter.elasticsearch.user.service.UserService;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Benoit on 30/03/2015.
 */
public class SubscriptionService extends AbstractService {

    private SubscriptionRecordDao subscriptionRecordDao;
    private SubscriptionExecutionDao subscriptionExecutionDao;
    private ThreadPool threadPool;
    private MailService mailService;
    private AdminService adminService;
    private UserEventService userEventService;
    private UserService userService;
    private String emailSubjectPrefix;

    @Inject
    public SubscriptionService(Duniter4jClient client,
                               PluginSettings settings,
                               CryptoService cryptoService,
                               SubscriptionRecordDao subscriptionRecordDao,
                               SubscriptionExecutionDao subscriptionExecutionDao,
                               ThreadPool threadPool,
                               MailService mailService,
                               AdminService adminService,
                               UserService userService,
                               UserEventService userEventService) {
        super("subscription.service", client, settings, cryptoService);
        this.subscriptionRecordDao = subscriptionRecordDao;
        this.subscriptionExecutionDao = subscriptionExecutionDao;
        this.threadPool = threadPool;
        this.mailService = mailService;
        this.adminService = adminService;
        this.userService = userService;
        this.userEventService = userEventService;
        this.emailSubjectPrefix = pluginSettings.getMailSubjectPrefix().trim();
        if (StringUtils.isNotBlank(emailSubjectPrefix)) {
            emailSubjectPrefix += " "; // add one trailing space
        }
    }

    public String create(String json) {
        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a subscription from issuer [%s]", issuer.substring(0, 8)));
        }

        return subscriptionRecordDao.create(json);
    }

    public void update(String id, String json) {
        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        // Check same document issuer
        subscriptionRecordDao.checkSameDocumentIssuer(id, issuer);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Updating subscription [%s] from issuer [%s]", id, issuer.substring(0, 8)));
        }

        subscriptionRecordDao.update(id, json);
    }

    public SubscriptionService startScheduling() {
        if (!pluginSettings.getMailEnable()) {
            logger.warn(I18n.t("duniter4j.es.subscription.error.mailDisabling"));
            return this;
        }

        // Email subscriptions
        {
            if (logger.isInfoEnabled()) {
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(0);
                cal.set(Calendar.DAY_OF_WEEK, pluginSettings.getEmailSubscriptionsExecuteDayOfWeek());
                String dayOfWeek = new SimpleDateFormat("EEE").format(cal.getTime());
                logger.warn(I18n.t("duniter4j.es.subscription.email.start", pluginSettings.getEmailSubscriptionsExecuteHour(), dayOfWeek));
            }

            // Daily execution
            threadPool.scheduler().scheduleAtFixedRate(
                    () -> executeEmailSubscriptions(EmailSubscription.Frequency.daily),
                    DateUtils.delayBeforeHour(pluginSettings.getEmailSubscriptionsExecuteHour()),
                    DateUtils.DAY_DURATION_IN_MILLIS, TimeUnit.MILLISECONDS);

            // Weekly execution
            threadPool.scheduler().scheduleAtFixedRate(
                    () -> executeEmailSubscriptions(EmailSubscription.Frequency.weekly),
                    DateUtils.delayBeforeDayAndHour(pluginSettings.getEmailSubscriptionsExecuteDayOfWeek(), pluginSettings.getEmailSubscriptionsExecuteHour()),
                    7 * DateUtils.DAY_DURATION_IN_MILLIS, TimeUnit.MILLISECONDS);
        }
        return this;
    }

    public void executeEmailSubscriptions(final EmailSubscription.Frequency frequency) {

        long now = System.currentTimeMillis();
        logger.info(String.format("Executing %s email subscription...", frequency.name()));

        final String senderPubkey = pluginSettings.getNodePubkey();

        int from = 0;
        int size = 10;
        boolean hasMore = true;
        long executionCount=0;
        while (hasMore) {
            List<SubscriptionRecord> subscriptions = subscriptionRecordDao.getSubscriptions(from, size, senderPubkey, EmailSubscription.TYPE);

            // Get profiles titles, for issuers and the sender
            Set<String> issuers =  subscriptions.stream()
                    .map(SubscriptionRecord::getIssuer)
                    .distinct()
                    .collect(Collectors.toSet());
            final Map<String, String> profileTitles = userService.getProfileTitles(
                    ImmutableSet.<String>builder().addAll(issuers).add(senderPubkey).build());
            final String senderName = (profileTitles != null && profileTitles.containsKey(senderPubkey)) ? profileTitles.get(senderPubkey) :
                ModelUtils.minifyPubkey(senderPubkey);

            executionCount += subscriptions.stream()
                    .map(record -> decryptEmailSubscription((EmailSubscription)record))
                    .filter(record -> (record != null && record.getContent().getFrequency() == frequency))
                    .map(record -> processEmailSubscription(record, senderPubkey, senderName, profileTitles))
                    .filter(Objects::nonNull)
                    .map(this::saveExecution)
                    .count();

            hasMore = CollectionUtils.size(subscriptions) >= size;
            from += size;
        }

        logger.info(String.format("Executing %s email subscription... [OK] (%s executions in %s ms)",
                frequency.name(), executionCount, System.currentTimeMillis()-now));

    }

    /* -- protected methods -- */


    protected EmailSubscription decryptEmailSubscription(EmailSubscription subscription) {
        Preconditions.checkNotNull(subscription);
        Preconditions.checkNotNull(subscription.getId());

        if (StringUtils.isBlank(subscription.getRecipientContent()) || StringUtils.isBlank(subscription.getNonce()) ||
                StringUtils.isBlank(subscription.getIssuer())) {
            logger.error(String.format("Invalid subscription [%s]. Missing field 'recipientContent', 'nonce' or 'issuer'.", subscription.getId()));
            return null;
        }

        String jsonContent;
        try {
            jsonContent = cryptoService.openBox(subscription.getRecipientContent(),
                    CryptoUtils.decodeBase58(subscription.getNonce()),
                    CryptoUtils.decodeBase58(subscription.getIssuer()),
                    pluginSettings.getNodeKeypair().getSecKey()
            );
        } catch(Exception e) {
            logger.error(String.format("Could not decrypt email subscription content for subscription [%s]", subscription.getId()));
            return null;
        }

        try {
            EmailSubscription.Content content = objectMapper.readValue(jsonContent, EmailSubscription.Content.class);
            subscription.setContent(content);
        } catch(Exception e) {
            logger.error(String.format("Could not parse email subscription content [%s]: %s", jsonContent, e.getMessage()));
            return null;
        }

        return subscription;
    }

    protected SubscriptionExecution processEmailSubscription(final EmailSubscription subscription,
                                                         final String senderPubkey,
                                                         final String senderName,
                                                         final Map<String, String> profileTitles) {
        Preconditions.checkNotNull(subscription);

        logger.info(String.format("Processing email subscription [%s]", subscription.getId()));

        SubscriptionExecution lastExecution = subscriptionExecutionDao.getLastExecution(subscription);
        Long lastExecutionTime;

        if (lastExecution != null) {
            lastExecutionTime = lastExecution.getTime();
        }
        // If first email execution: only send event from the last 7 days.
        else  {
            Calendar defaultDateLimit = new GregorianCalendar();
            defaultDateLimit.setTimeInMillis(System.currentTimeMillis());
            defaultDateLimit.add(Calendar.DAY_OF_YEAR, - 7);
            defaultDateLimit.set(Calendar.HOUR_OF_DAY, 0);
            defaultDateLimit.set(Calendar.MINUTE, 0);
            defaultDateLimit.set(Calendar.SECOND, 0);
            defaultDateLimit.set(Calendar.MILLISECOND, 0);
            lastExecutionTime = defaultDateLimit.getTimeInMillis() / 1000;
        }

        // Get last user events
        String[] includes = subscription.getContent() == null ? null : subscription.getContent().getIncludes();
        String[] excludes = subscription.getContent() == null ? null : subscription.getContent().getExcludes();
        List<UserEvent> userEvents = userEventService.getUserEvents(subscription.getIssuer(), lastExecutionTime, includes, excludes);

        if (CollectionUtils.isEmpty(userEvents)) return null; // no events: stop here

        // Get user locale
        String[] localParts = subscription.getContent() != null && subscription.getContent().getLocale() != null ?
                subscription.getContent().getLocale().split("-") : new String[]{"en", "GB"};
        Locale issuerLocale = localParts.length >= 2 ? new Locale(localParts[0].toLowerCase(), localParts[1].toUpperCase()) : new Locale(localParts[0].toLowerCase());

        // Configure templates engine
        STGroup templates = new STGroupDir("templates", '$', '$');
        templates.registerRenderer(Date.class, new DateRenderer());
        templates.registerRenderer(String.class, new StringRenderer());
        //templates.registerRenderer(Number.class, new NumberRenderer());

        // Compute text content
        final String text = fillTemplate(
                templates.getInstanceOf("text_email"),
                subscription,
                senderPubkey,
                senderName,
                profileTitles,
                userEvents,
                pluginSettings.getCesiumUrl())
                .render(issuerLocale);

        // Compute HTML content
        final String html = fillTemplate(
                templates.getInstanceOf("html_email_content"),
                subscription,
                senderPubkey,
                senderName,
                profileTitles,
                userEvents,
                pluginSettings.getCesiumUrl())
                .render(issuerLocale);



        // Schedule email sending
        threadPool.schedule(() -> mailService.sendHtmlEmailWithText(
                emailSubjectPrefix + I18n.t("duniter4j.es.subscription.email.subject", userEvents.size()),
                text,
                "<body>" + html + "</body>",
                subscription.getContent().getEmail()));


        // Compute last time (should be the first one, as events are sorted in DESC order)
        Long lastEventTime = userEvents.get(0).getTime();
        if (lastExecution == null) {
            lastExecution = new SubscriptionExecution();
            lastExecution.setRecipient(subscription.getIssuer());
            lastExecution.setRecordType(subscription.getType());
            lastExecution.setRecordId(subscription.getId());
        }
        lastExecution.setTime(lastEventTime);


        return lastExecution;
    }


    public static ST fillTemplate(ST template,
                                  EmailSubscription subscription,
                                  String senderPubkey,
                                  String senderName,
                                  Map<String, String> issuerProfilNames,
                                  List<UserEvent> userEvents,
                                  String cesiumSiteUrl) {
        String issuerName = issuerProfilNames != null && issuerProfilNames.containsKey(subscription.getIssuer()) ?
                issuerProfilNames.get(subscription.getIssuer()) :
                ModelUtils.minifyPubkey(subscription.getIssuer());


        try {
            // Compute body
            template.add("url", cesiumSiteUrl);
            template.add("issuerPubkey", subscription.getIssuer());
            template.add("issuerName", issuerName);
            template.add("senderPubkey", senderPubkey);
            template.add("senderName", senderName);
            userEvents.forEach(userEvent -> {
                String description = userEvent.getParams() != null ?
                        I18n.t("duniter.user.event." + userEvent.getCode().toUpperCase(), userEvent.getParams()) :
                        I18n.t("duniter.user.event." + userEvent.getCode().toUpperCase());
                template.addAggr("events.{description, time}", new Object[]{
                    description,
                    new Date(userEvent.getTime() * 1000)
                });
            });

            return template;

        }
        catch (Exception e) {
          throw new TechnicalException(e);
        }
    }

    protected SubscriptionExecution saveExecution(SubscriptionExecution execution) {
        Preconditions.checkNotNull(execution);
        Preconditions.checkNotNull(execution.getRecipient());
        Preconditions.checkNotNull(execution.getRecordType());
        Preconditions.checkNotNull(execution.getRecordId());

        // Update issuer
        execution.setIssuer(pluginSettings.getNodePubkey());

        // Fill hash + signature
        String json = toJson(execution, true/*skip hash and signature*/);
        execution.setHash(cryptoService.hash(json));
        execution.setSignature(cryptoService.sign(json, pluginSettings.getNodeKeypair().getSecKey()));

        if (execution.getId() == null) {

            subscriptionExecutionDao.create(json, false/*not wait*/);
        }
        else {
            subscriptionExecutionDao.update(execution.getId(), json, false/*not wait*/);
        }
        return execution;
    }

    private String toJson(Record record) {
        return toJson(record, false);
    }

    private String toJson(Record record, boolean cleanHashAndSignature) {
        Preconditions.checkNotNull(record);
        try {
            String json = objectMapper.writeValueAsString(record);
            if (cleanHashAndSignature) {
                json = JacksonUtils.removeAttribute(json, Record.PROPERTY_SIGNATURE);
                json = JacksonUtils.removeAttribute(json, Record.PROPERTY_HASH);
            }
            return json;
        } catch(JsonProcessingException e) {
            throw new TechnicalException("Unable to serialize object " + record.getClass().getName(), e);
        }
    }


}