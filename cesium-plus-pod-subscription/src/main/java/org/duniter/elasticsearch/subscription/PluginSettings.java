package org.duniter.elasticsearch.subscription;

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


import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.util.crypto.KeyPair;
import org.elasticsearch.common.component.*;
import org.elasticsearch.common.inject.Inject;

import java.util.List;

/**
 * Access to configuration options
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>
 * @since 1.0
 */
public class PluginSettings extends AbstractLifecycleComponent<PluginSettings> {

    private org.duniter.elasticsearch.user.PluginSettings delegate;

    private static PluginSettings instance;

    public static final PluginSettings instance() {
        return instance;
    }

    @Inject
    public PluginSettings(org.elasticsearch.common.settings.Settings settings,
                          org.duniter.elasticsearch.user.PluginSettings delegate) {
        super(settings);
        this.delegate = delegate;

        // Add i18n bundle name
        delegate.addI18nBundleName(getI18nBundleName());

        instance = this;
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() {

    }

    public org.duniter.elasticsearch.user.PluginSettings getDelegate() {
        return delegate;
    }


    public boolean enableSubscription() {
        return settings.getAsBoolean("duniter.subscription.enable", Boolean.TRUE);
    }

    public boolean enableDocStats() {
        return delegate.enableDocStats();
    }

    /**
     * @deprecated use getEmailLinkUrl instead
     * @return
     */
    @Deprecated
    public String getCesiumUrl() {
        return this.settings.get("duniter.subscription.email.cesium.url", "https://g1.duniter.fr");
    }

    public String getEmailLinkUrl() {
        return this.settings.get("duniter.subscription.email.link.url", getCesiumUrl());
    }

    /**
     * Should email subscription be send at startup ?
     * @return
     */
    public boolean isEmailSubscriptionsExecuteAtStartup() {
        return settings.getAsBoolean("duniter.subscription.email.atStartup", false);
    }

    /**
     * Should email subscription execute as DEBUG mode ?
     * @return
     */
    public boolean isEmailSubscriptionsDebug() {
        return settings.getAsBoolean("duniter.subscription.email.debug", false);
    }


    /**
     * Day of the week to trigger weekly email subscription (default: 2 = monday)
     * @return
     */
    public int getEmailSubscriptionsExecuteDayOfWeek() {
        return settings.getAsInt("duniter.subscription.email.dayOfWeek", 2);
    }

    /**
     * Hour in day to trigger daily email subscription (default: 3 AM)
     * @return
     */
    public int getEmailSubscriptionsExecuteHour() {
        return settings.getAsInt("duniter.subscription.email.hourOfDay", 3);
    }

    /* -- delegate methods -- */


    public boolean reloadIndices() {
        return delegate.reloadAllIndices();
    }

    public boolean enableSynchro() {
        return delegate.enableSynchro();
    }

    public boolean enablePeering() {
        return delegate.enablePeering();
    }

    public List<EndpointApi> getPeeringTargetedApis() {
        return this.delegate.getPeeringTargetedApis();
    }

    public List<EndpointApi> getPeeringPublishedApis() {
        return this.delegate.getPeeringPublishedApis();
    }

    public int getSynchroTimeOffset() {
        return delegate.getSynchroTimeOffset();
    }

    public boolean getMailEnable() {
        return delegate.getMailEnable();
    }

    public String getMailSmtpHost() {
        return delegate.getMailSmtpHost();
    }

    public int getMailSmtpPort() {
        return delegate.getMailSmtpPort();
    }

    public String getMailSmtpUsername() {
        return delegate.getMailSmtpUsername();
    }

    public String getMailSmtpPassword() {
        return delegate.getMailSmtpPassword();
    }

    public String getMailAdmin() {
        return delegate.getMailAdmin();
    }

    public String getMailFrom() {
        return delegate.getMailFrom();
    }

    public String getMailSubjectPrefix() {
        return delegate.getMailSubjectPrefix();
    }

    public String getClusterName() {
        return delegate.getClusterName();
    }

    public String getNodeBmaHost() {
        return delegate.getNodeBmaHost();
    }

    public int getNodeBmaPort() {
        return delegate.getNodeBmaPort();
    }

    public int getIndexBulkSize() {
        return delegate.getIndexBulkSize();
    }

    public boolean enableBlockchainSync() {
        return delegate.enableBlockchainSync();
    }

    public String getKeyringSalt() {
        return delegate.getKeyringSalt();
    }

    public String getKeyringPassword() {
        return delegate.getKeyringPassword();
    }

    public String getKeyringPublicKey() {
        return delegate.getKeyringPublicKey();
    }

    public String getKeyringSecretKey() {
        return delegate.getKeyringSecretKey();
    }

    public String getDefaultStringAnalyzer() {
        return delegate.getDefaultStringAnalyzer();
    }

    public KeyPair getNodeKeypair() {
        return delegate.getNodeKeypair();
    }

    public boolean isRandomNodeKeypair() {
        return delegate.isRandomNodeKeypair();
    }

    public String getNodePubkey() {
        return delegate.getNodePubkey();
    }

    /* -- protected methods -- */

    protected String getI18nBundleName() {
        return "cesium-plus-pod-subscription-i18n";
    }
}
