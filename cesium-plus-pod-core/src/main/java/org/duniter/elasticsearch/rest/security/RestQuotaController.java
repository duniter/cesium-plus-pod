package org.duniter.elasticsearch.rest.security;

/*
 * #%L
 * Duniter4j :: ElasticSearch Plugin
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

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.util.RestUtils;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestRequest;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by blavenie on 30/12/20.
 */
public class RestQuotaController extends AbstractLifecycleComponent<RestQuotaController> {

    private final ESLogger log;

    private boolean enable;
    private boolean trace;

    private Set<String> ipWhiteList;

    private Map<RestRequest.Method, Map<String, QuotaMapByIp>> quotasByMethod;

    @Inject
    public RestQuotaController(Settings settings, PluginSettings pluginSettings) {
        super(settings);
        this.log = Loggers.getLogger("duniter.security.quota", settings, new String[0]);
        this.trace = log.isTraceEnabled();
        this.enable = pluginSettings.enableQuota();
        this.quotasByMethod = new HashMap<>();
        this.ipWhiteList = Sets.newHashSet(pluginSettings.getIpWhiteList());
        if (!enable) {
            log.warn("/!\\ Security has been disable using option [duniter.security.quota.enable]. This is NOT recommended in production !");
        }
    }

    public RestQuotaController quota(RestRequest.Method method, String regexPath, int maxCount, int duration, TimeUnit unit) {
        Map<String, QuotaMapByIp> quotaByRequest = quotasByMethod.computeIfAbsent(method, k -> new LinkedHashMap<>());


        if (!quotaByRequest.containsKey(regexPath)) {
            quotaByRequest.put(regexPath, new QuotaMapByIp(regexPath, maxCount, duration, unit, log));
        }
        else {
            log.warn(String.format("More than one quota defined for request %s (%s). Skipping new quota config", regexPath, method.toString()));
        }

        return this;
    }

    public boolean isAllow(RestRequest request) {
        if (!this.enable) return true;

        RestRequest.Method method = request.method();
        String path = request.path();
        String ip = RestUtils.getIp(request);

        // Check if whitelist
        if (ip != null && ipWhiteList.contains(ip)) {
            if (trace) log.trace(String.format("Checking quota for %s request [%s]: OK (whitelisted)", method, path));
            return true;
        }

        Map<String, QuotaMapByIp> quotas = quotasByMethod.get(request.method());

        if (trace) log.trace(String.format("Checking quota for %s request [%s]...", method, path));
        if (quotas == null) {
            if (trace) log.trace(String.format("No matching quota defined for %s request [%s]: continue", method, path));
            return true;
        }
        boolean found = false;
        for (String pathRegexp : quotas.keySet()) {
            if (trace) log.trace(String.format(" - Trying against quota [%s] for %s requests", pathRegexp, method));

            // A quota exists for this path
            if (path.matches(pathRegexp)) {
                if (trace) log.trace(String.format("Find matching quota [%s] for %s request [%s]", pathRegexp, method, path));

                // NO IP not allow, because we cannot check
                if (StringUtils.isEmpty(ip)) {
                    if (trace) log.trace(String.format("No IP address found in request: reject", pathRegexp, method, path));
                    return false;
                }

                QuotaMapByIp quota = quotas.get(pathRegexp);

                // If cannot increment: NOT allow
                if (!quota.increment(ip)) {
                    return false;
                }

                found = true;
            }
        }
        if (trace && !found) {
            log.trace(String.format("No matching quota for %s request [%s]: allow", method, path));
        }
        return true;
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

}
