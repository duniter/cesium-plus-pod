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

import org.duniter.elasticsearch.PluginSettings;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.RestRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by blavenie on 11/10/16.
 */
public class RestSecurityController extends AbstractLifecycleComponent<RestSecurityController> {

    private final ESLogger log;

    private boolean enable;
    private boolean trace;

    private Map<RestRequest.Method, Set<String>> allowRulesByMethod;

    @Inject
    public RestSecurityController(Settings settings, PluginSettings pluginSettings) {
        super(settings);
        this.log = Loggers.getLogger("duniter.security", settings, new String[0]);
        this.trace = log.isTraceEnabled();
        this.enable = pluginSettings.enableSecurity();
        this.allowRulesByMethod = new HashMap<>();
        if (!enable) {
            log.warn("/!\\ Security has been disable using option [duniter.security.enable]. This is NOT recommended in production !");
        }
    }

    public RestSecurityController allowIndexType(RestRequest.Method method, String index, String type) {
        allow(method, String.format("/%s/%s(/.*)?", index, type));
        return this;
    }

    public RestSecurityController allowPostSearchIndexType(String index, String type) {
        allow(RestRequest.Method.POST, String.format("/%s/%s/_search", index, type));
        return this;
    }

    public RestSecurityController allowImageAttachment(String index, String type, String field) {
        allow(RestRequest.Method.GET, String.format("/%s/%s/[^/]+/_image/%s.*", index, type, field));
        return this;
    }

    public RestSecurityController allow(RestRequest.Method method, String regexPath) {
        Set<String> allowRules = allowRulesByMethod.computeIfAbsent(method, k -> new TreeSet<>());

        if (!allowRules.contains(regexPath)) {
            allowRules.add(regexPath);
        }
        return this;
    }

    public boolean isAllow(RestRequest request) {
        if (!this.enable) return true;

        RestRequest.Method method = request.method();
        String path = request.path();

        Set<String> allowRules = allowRulesByMethod.get(request.method());

        // Trace mode
        if (trace) {
            log.trace(String.format("Checking rules for %s request [%s]...", method, path));
            if (allowRules == null) {
                log.trace(String.format("No matching rules for %s request [%s]: reject", method, path));
            }
            else {
                boolean found = false;
                for (String allowRule : allowRules) {
                    log.trace(String.format(" - Trying against rule [%s] for %s requests: not match", allowRule, method));
                    if (path.matches(allowRule)) {
                        log.trace(String.format("Find matching rule [%s] for %s request [%s]: allow", allowRule, method, path));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    log.trace(String.format("No matching rules for %s request [%s]: reject", method, path));
                }
            }
        }

        // Check if allow
        if (allowRules != null) {
            for (String allowRule : allowRules) {
                if (path.matches(allowRule)) {
                    return true;
                }
            }
        }
        return false;
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

    /* -- Internal method -- */

}
