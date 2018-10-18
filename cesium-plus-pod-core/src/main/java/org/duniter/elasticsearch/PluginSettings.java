package org.duniter.elasticsearch;

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


import com.google.common.collect.ImmutableSet;
import org.apache.commons.io.FileUtils;
import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.config.ConfigurationOption;
import org.duniter.core.client.config.ConfigurationProvider;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.crypto.CryptoUtils;
import org.duniter.core.util.crypto.KeyPair;
import org.duniter.elasticsearch.i18n.I18nInitializer;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.nuiton.config.ApplicationConfig;
import org.nuiton.config.ApplicationConfigHelper;
import org.nuiton.config.ApplicationConfigProvider;
import org.nuiton.config.ArgumentsParserException;
import org.nuiton.i18n.I18n;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.nuiton.i18n.I18n.t;

/**
 * Access to configuration options
 * @author Benoit Lavenier <benoit.lavenier@e-is.pro>
 * @since 1.0
 */
public class PluginSettings extends AbstractLifecycleComponent<PluginSettings> {

    private static KeyPair nodeKeyPair;
    private static boolean isRandomNodeKeyPair;
    private static  String nodePubkey;

    protected final Settings settings;
    private List<String> i18nBundleNames = new ArrayList<>(); // Default
    private String clusterRemoteUrl;
    private final CryptoService cryptoService;

    /**
     * Delegate application config.
     */
    protected final ApplicationConfig applicationConfig;
    protected final org.duniter.core.client.config.Configuration clientConfig;

    @Inject
    public PluginSettings(org.elasticsearch.common.settings.Settings settings,
                          CryptoService cryptoService) {
        super(settings);

        this.settings = settings;
        this.cryptoService = cryptoService;
        this.applicationConfig = new ApplicationConfig();

        // Cascade the application config to the client module
        clientConfig = new org.duniter.core.client.config.Configuration(this.applicationConfig);
        Configuration.setInstance(clientConfig);

        // Set the default bundle name
        addI18nBundleName(getI18nBundleName());
    }

    @Override
    protected void doStart() {


        // get all config providers
        Set<ApplicationConfigProvider> providers =
                ImmutableSet.of(new ConfigurationProvider());

        // load all default options
        ApplicationConfigHelper.loadAllDefaultOption(applicationConfig,
                providers);

        // Ovverides defaults
        String baseDir = settings.get("path.home");
        applicationConfig.setDefaultOption(ConfigurationOption.BASEDIR.getKey(), baseDir);
        applicationConfig.setDefaultOption(ConfigurationOption.NODE_HOST.getKey(), getNodeBmaHost());
        applicationConfig.setDefaultOption(ConfigurationOption.NODE_PORT.getKey(), String.valueOf(getNodeBmaPort()));
        applicationConfig.setDefaultOption(ConfigurationOption.NETWORK_TIMEOUT.getKey(), String.valueOf(getNetworkTimeout()));
        applicationConfig.setDefaultOption(ConfigurationOption.NETWORK_MAX_CONNECTIONS.getKey(), String.valueOf(getNetworkMaxConnections()));
        applicationConfig.setDefaultOption(ConfigurationOption.NETWORK_MAX_CONNECTIONS_PER_ROUTE.getKey(), String.valueOf(getNetworkMaxConnectionsPerRoute()));

        try {
            applicationConfig.parse(new String[]{});

        } catch (ArgumentsParserException e) {
            throw new TechnicalException(t("duniter4j.config.parse.error"), e);
        }

        File appBasedir = applicationConfig.getOptionAsFile(
                ConfigurationOption.BASEDIR.getKey());

        if (appBasedir == null) {
            appBasedir = new File("");
        }
        if (!appBasedir.isAbsolute()) {
            appBasedir = new File(appBasedir.getAbsolutePath());
        }
        if (appBasedir.getName().equals("..")) {
            appBasedir = appBasedir.getParentFile().getParentFile();
        }
        if (appBasedir.getName().equals(".")) {
            appBasedir = appBasedir.getParentFile();
        }
        applicationConfig.setOption(
                ConfigurationOption.BASEDIR.getKey(),
                appBasedir.getAbsolutePath());

        // Init i18n
        try {
            initI18n();
        }
        catch(IOException e) {
            logger.error(String.format("Could not init i18n: %s", e.getMessage()), e);
        }

        initVersion(applicationConfig);
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() {

    }

    public Settings getSettings() {
        return settings;
    }

    /* -- settings on cluster -- */

    public String getClusterName() {
        return settings.get("cluster.name", "?");
    }

    public String getClusterRemoteHost() {
        return settings.get("cluster.remote.host");
    }

    public int getClusterRemotePort() {
        return settings.getAsInt("cluster.remote.port", 80);
    }

    public boolean getClusterRemoteUseSsl() {
        return settings.getAsBoolean("cluster.remote.useSsl", getClusterRemotePort() == 443);
    }

    public String getClusterRemoteUrlOrNull() {
        if (StringUtils.isBlank(getClusterRemoteHost())) return null;

        if (clusterRemoteUrl == null) {

            clusterRemoteUrl = Peer.newBuilder().setHost(getClusterRemoteHost())
                    .setPort(getClusterRemotePort())
                    .setUseSsl(getClusterRemoteUseSsl())
                    .build()
                    .getUrl();
        }

        return clusterRemoteUrl;
    }


    /* -- Settings on Duniter node (with BMA API) -- */

    public String getNodeBmaHost() {
        return settings.get("duniter.host", "g1.duniter.org");
    }

    public int getNodeBmaPort() {
        return settings.getAsInt("duniter.port", 10901);
    }

    public boolean getNodeBmaUseSsl() {
        return settings.getAsBoolean("duniter.useSsl", getNodeBmaPort() == 443);
    }

    /* -- Other settings -- */

    public boolean isIndexBulkEnable() {
        return settings.getAsBoolean("duniter.bulk.enable", true);
    }

    public int getIndexBulkSize() {
        return settings.getAsInt("duniter.bulk.size", 1000);
    }

    public int getNodeForkResyncWindow() {
        return settings.getAsInt("duniter.fork.resync.window", 100);
    }

    public String getDefaultStringAnalyzer() {
        return settings.get("duniter.string.analyzer", "english");
    }

    public boolean reloadAllIndices() {
        return settings.getAsBoolean("duniter.indices.reload", false);
    }

    public boolean enableBlockchainIndexation()  {
        return settings.getAsBoolean("duniter.blockchain.enable", false);
    }

    public boolean reloadBlockchainIndices()  {
        return settings.getAsBoolean("duniter.blockchain.reload", false);
    }

    public int reloadBlockchainIndicesFrom()  {
        return settings.getAsInt("duniter.blockchain.reload.from", 0);
    }
    public int reloadBlockchainIndicesTo()  {
        return settings.getAsInt("duniter.blockchain.reload.to", -1);
    }

    public File getTempDirectory() {
        return Configuration.instance().getTempDirectory();
    }

    public int getNetworkTimeout()  {
        return settings.getAsInt("duniter.network.timeout", 30000 /*30s*/);
    }

    public int getNetworkMaxConnections()  {
        return settings.getAsInt("duniter.network.maxConnections", 100);
    }

    public int getNetworkMaxConnectionsPerRoute()  {
        return settings.getAsInt("duniter.network.maxConnectionsPerRoute", 5);
    }

    public boolean enableSynchro()  {
        return settings.getAsBoolean("duniter.p2p.enable", true);
    }

    public boolean enableSynchroWebsocket()  {
        return settings.getAsBoolean("duniter.p2p.ws.enable", true);
    }

    public boolean enablePeering() {
        return this.settings.getAsBoolean("duniter.p2p.peering.enable", enableSynchro());
    }

    /**
     * Endpoint API to publish, in the emitted peer document. By default, plugins will defined their own API
     * @return
     */
    public List<EndpointApi> getPeeringPublishedApis() {
        String[] targetedApis = settings.getAsArray("duniter.p2p.peering.publishedApis");
        if (CollectionUtils.isEmpty(targetedApis)) return null;

        return Arrays.stream(targetedApis).map(EndpointApi::valueOf).collect(Collectors.toList());
    }

    /**
     * Targeted API where to send the peer document.
     * This API should accept a POST request to '/network/peering' (like Duniter node, but can also be a pod)
     * @return
     */
    public List<EndpointApi> getPeeringTargetedApis() {
        String[] targetedApis = settings.getAsArray("duniter.p2p.peering.targetedApis", new String[]{
                EndpointApi.BASIC_MERKLED_API.name(),
                EndpointApi.BMAS.name()
        });
        if (CollectionUtils.isEmpty(targetedApis)) return null;

        return Arrays.stream(targetedApis).map(EndpointApi::valueOf).collect(Collectors.toList());
    }

    /**
     * Interval (in seconds) between publications of the peer document
     * @return
     */
    public int getPeeringInterval() {
        return this.settings.getAsInt("duniter.p2p.peering.interval", 7200 /*=2h*/);
    }

    public boolean fullResyncAtStartup()  {
        return settings.getAsBoolean("duniter.p2p.fullResyncAtStartup", false);
    }

    public int getSynchroTimeOffset()  {
        return settings.getAsInt("duniter.p2p.peerTimeOffset", 60*60/*=1hour*/);
    }

    public String[] getSynchroIncludesEndpoints()  {
        return settings.getAsArray("duniter.p2p.includes.endpoints");
    }

    public String[] getSynchroIncludesPubkeys()  {
        return settings.getAsArray("duniter.p2p.includes.pubkeys");
    }

    public boolean enableSynchroDiscovery()  {
        return settings.getAsBoolean("duniter.p2p.discovery.enable", true);
    }

    public boolean isDevMode() {
        return settings.getAsBoolean("duniter.dev.enable", false);
    }

    public int getNodeRetryCount() {
        return settings.getAsInt("duniter.retry.count", 5);
    }

    /**
     * Time before retry (in millis)
     * @return
     */
    public int getNodeRetryWaitDuration() {
        return settings.getAsInt("duniter.retry.waitDuration", 5000);
    }

    public String getShareBaseUrl() {
        return settings.get("duniter.share.base.url");
    }

    public Peer checkAndGetPeer() {
        if (StringUtils.isBlank(getNodeBmaHost())) {
            logger.error("ERROR: node host is required");
            System.exit(-1);
            return null;
        }
        if (getNodeBmaPort() <= 0) {
            logger.error("ERROR: node port is required");
            System.exit(-1);
            return null;
        }

        Peer peer = Peer.newBuilder().setHost(getNodeBmaHost()).setPort(getNodeBmaPort()).setUseSsl(getNodeBmaUseSsl()).build();
        return peer;
    }

    public String getKeyringSalt() {
        return settings.get("duniter.keyring.salt");
    }

    public String getKeyringPassword() {
        return settings.get("duniter.keyring.password");
    }

    public String getKeyringPublicKey() {
        return settings.get("duniter.keyring.pub");
    }

    public String getKeyringSecretKey() {
        return settings.get("duniter.keyring.sec");
    }

    public boolean enableSecurity() {
        return settings.getAsBoolean("duniter.security.enable", true);
    }

    public int getDocumentTimeMaxPastDelta() {
        return settings.getAsInt("duniter.document.time.maxPastDelta", 7200); // in seconds = 2h
    }

    public int getDocumentTimeMaxFutureDelta() {
        return settings.getAsInt("duniter.document.time.maxFutureDelta", 600); // in seconds = 10min
    }

    public boolean allowDocumentDeletionByAdmin() {
        return settings.getAsBoolean("duniter.document.allowAdminDeletion", true); //
    }

    public String getWebSocketHost()  {
        return settings.get("network.host", "localhost");
    }

    public String getWebSocketPort()  {
        return settings.get("duniter.ws.port", "9400");
    }

    public boolean getWebSocketEnable()  {
        return settings.getAsBoolean("duniter.ws.enable", Boolean.TRUE);
    }

    public String[] getWebSocketChangesListenSource()  {
        return settings.getAsArray("duniter.ws.changes.listenSource", new String[]{"*"});
    }

    public boolean enableDocStats() {
        return settings.getAsBoolean("duniter.stats.enable", true);
    }

    /* protected methods */

    protected void initI18n() throws IOException {
        //if (I18n.getDefaultLocale() != null) return; // already init

        // --------------------------------------------------------------------//
        // init i18n
        // --------------------------------------------------------------------//

        File i18nDirectory = clientConfig.getI18nDirectory();
        if (i18nDirectory.exists()) {
            // clean i18n cache
            FileUtils.cleanDirectory(i18nDirectory);
        }

        FileUtils.forceMkdir(i18nDirectory);

        if (logger.isDebugEnabled()) {
            logger.debug("I18N directory: " + i18nDirectory);
        }

        Locale i18nLocale = clientConfig.getI18nLocale();

        if (logger.isInfoEnabled()) {
            logger.info(String.format("Starts i18n with locale [%s] at [%s]",
                    i18nLocale, i18nDirectory));
        }

        I18n.init(new I18nInitializer(i18nDirectory, getI18nBundleNames()),
                i18nLocale);
    }

    protected String getI18nBundleName() {
        return "cesium-plus-pod-core-i18n";
    }

    protected String[] getI18nBundleNames() {
        return i18nBundleNames.toArray(new String[i18nBundleNames.size()]);
    }

    public void addI18nBundleName(String i18nBundleName) {
        if (!this.i18nBundleNames.contains(i18nBundleName)) {
            this.i18nBundleNames.add(i18nBundleName);
        }
    }

    public Locale getI18nLocale() {
        return clientConfig.getI18nLocale();
    }

    /**
     * Override the version default option, from the MANIFEST implementation version (if any)
     * @param applicationConfig
     */
    protected void initVersion(ApplicationConfig applicationConfig) {
        // Override application version
        String implementationVersion = this.getClass().getPackage().getSpecificationVersion();
        if (implementationVersion != null) {
            applicationConfig.setDefaultOption(
                    ConfigurationOption.VERSION.getKey(),
                    implementationVersion);
        }
    }


    public KeyPair getNodeKeypair() {
        initNodeKeyring();
        return this.nodeKeyPair;
    }

    public boolean isRandomNodeKeypair() {
        initNodeKeyring();
        return this.isRandomNodeKeyPair;
    }

    public String getNodePubkey() {
        initNodeKeyring();
        return this.nodePubkey;
    }

    protected synchronized void initNodeKeyring() {
        if (this.nodeKeyPair != null) return;
        if (StringUtils.isNotBlank(getKeyringSalt()) &&
                StringUtils.isNotBlank(getKeyringPassword())) {
            this.nodeKeyPair = cryptoService.getKeyPair(getKeyringSalt(), getKeyringPassword());
            this.nodePubkey = CryptoUtils.encodeBase58(this.nodeKeyPair.getPubKey());
            this.isRandomNodeKeyPair = false;
        }
        else {
            // Use a ramdom keypair
            this.nodeKeyPair = cryptoService.getRandomKeypair();
            this.nodePubkey = CryptoUtils.encodeBase58(this.nodeKeyPair.getPubKey());
            this.isRandomNodeKeyPair = true;

            logger.warn(String.format("No keyring in config. salt/password (or keyring) is need to signed user event documents. Will use a generated key [%s]", this.nodePubkey));
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("    salt: " + getKeyringSalt().replaceAll(".", "*")));
                logger.debug(String.format("password: " + getKeyringPassword().replaceAll(".", "*")));
            }
        }
    }
}