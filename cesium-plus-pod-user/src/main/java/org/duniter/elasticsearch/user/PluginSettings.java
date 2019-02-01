package org.duniter.elasticsearch.user;

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
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

import java.util.Collection;
import java.util.Locale;

/**
 * Access to configuration options
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>
 * @since 1.0
 */
public class PluginSettings extends AbstractLifecycleComponent<PluginSettings> {

    private org.duniter.elasticsearch.PluginSettings delegate;

    @Inject
    public PluginSettings(Settings settings,
                          org.duniter.elasticsearch.PluginSettings delegate) {
        super(settings);
        this.delegate = delegate;

        // Add i18n bundle name
        delegate.addI18nBundleName(getI18nBundleName());
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doClose() {

    }

    @Override
    protected void doStop() {

    }

    public Settings getSettings() {
        return delegate.getSettings();
    }

    public org.duniter.elasticsearch.PluginSettings getDelegate() {
        return delegate;
    }

    public String getDefaultStringAnalyzer() {
        return delegate.getDefaultStringAnalyzer();
    }

    public boolean reloadAllIndices() {
        return delegate.reloadAllIndices();
    }

    public boolean reloadBlockchainIndices() {
        return delegate.reloadBlockchainIndices();
    }

    public int reloadBlockchainIndicesFrom() {
        return delegate.reloadBlockchainIndicesFrom();
    }

    public boolean enableDocStats() {
        return delegate.enableDocStats();
    }

    public boolean enableSynchro() {
        return delegate.enableSynchro();
    }

    public boolean enablePeering() {
        return this.delegate.enablePeering();
    }

    public Collection<EndpointApi> getPeeringTargetedApis() {
        return this.delegate.getPeeringTargetedApis();
    }

    public Collection<EndpointApi> getPeeringPublishedApis() {
        return this.delegate.getPeeringPublishedApis();
    }

    public int getSynchroTimeOffset() {
        return settings.getAsInt("duniter.synchro.timeOffsetInSec", 60*60 /*1 hour*/ );
    }


    public boolean getMailEnable() {
        return settings.getAsBoolean("duniter.mail.enable", Boolean.TRUE);
    }

    public String getMailSmtpHost()  {
        return settings.get("duniter.mail.smtp.host", "localhost");
    }

    public int getMailSmtpPort()  {
        return settings.getAsInt("duniter.mail.smtp.port", 25);
    }

    public String getMailSmtpUsername()  {
        return settings.get("duniter.mail.smtp.username");
    }

    public String getMailSmtpPassword()  {
        return settings.get("duniter.mail.smtp.password");
    }

    public boolean isMailSmtpStartTLS()  {
        return settings.getAsBoolean("duniter.mail.smtp.starttls", false);
    }

    public boolean isMailSmtpUseSSL()  {
        return settings.getAsBoolean("duniter.mail.smtp.ssl", false);
    }

    public String getMailAdmin()  {
        return settings.get("duniter.mail.admin");
    }

    public String getMailFrom()  {
        return settings.get("duniter.mail.from", "no-reply@duniter.fr");
    }

    public String getMailSubjectPrefix()  {
        return settings.get("duniter.mail.subject.prefix", "[Cesium+]");
    }

    /* -- delegate methods -- */

    public String getClusterName() {
        return delegate.getClusterName();
    }

    public String getClusterRemoteHost() {
        return delegate.getClusterRemoteHost();
    }

    public int getClusterRemotePort() {
        return delegate.getClusterRemotePort();
    }

    public boolean getClusterRemoteUseSsl() {
        return delegate.getClusterRemoteUseSsl();
    }

    public String getClusterRemoteUrlOrNull() {
        return delegate.getClusterRemoteUrlOrNull();
    }

    public String getNodeBmaHost() {
        return delegate.getDuniterNodeHost();
    }

    public int getNodeBmaPort() {
        return delegate.getDuniterNodePort();
    }

    public int getIndexBulkSize() {
        return delegate.getIndexBulkSize();
    }

    public boolean enableBlockchainSync() {
        return delegate.enableBlockchainIndexation();
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

    public boolean allowDocumentDeletionByAdmin() {
        return delegate.allowDocumentDeletionByAdmin();
    }

    public void addI18nBundleName(String bundleName) {
        delegate.addI18nBundleName(bundleName);
    }

    public Locale getI18nLocale() {
        return delegate.getI18nLocale();
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

    /**
     * @deprecated
     * @return
     */
    @Deprecated
    public String getCesiumUrl() {
        return this.settings.get("duniter.share.cesium.url", "https://g1.duniter.fr");
    }

    public String getSharePageLinkUrl() {
        return this.settings.get("duniter.share.page.link.url", getCesiumUrl() + "#/app/page/view/{id}/{title}");
    }
    public String getShareUserLinkUrl() {
        return this.settings.get("duniter.share.user.link.url", getCesiumUrl() + "#/app/wot/{pubkey}/{title}");
    }

    public String getShareDefaultImageUrl() {
        return this.settings.get("duniter.share.image.default.url", getCesiumUrl() + "/img/logo_200px.png");
    }

    public String getShareSiteName() {
        return this.settings.get("duniter.share.site.name", "Cesium");
    }

    public String getShareBaseUrl() {
        return settings.get("duniter.share.base.url");
    }

    public boolean enableBlockchainUserEventIndexation() {
        return delegate.enableBlockchainIndexation() && settings.getAsBoolean("duniter.blockchain.event.user.enable", true);
    }

    public boolean enableBlockchainAdminEventIndexation() {
        return delegate.enableBlockchainIndexation() && settings.getAsBoolean("duniter.blockchain.event.admin.enable", true);
    }

    /* -- protected methods -- */

    protected String getI18nBundleName() {
        return "cesium-plus-pod-user-i18n";
    }



}
