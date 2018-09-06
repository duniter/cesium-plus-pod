package org.duniter.elasticsearch.security.token;

/*
 * #%L
 * duniter4j :: UI Wicket
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

import org.duniter.core.util.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.StringUtils;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;

import java.util.concurrent.TimeUnit;

/**
 * Created by blavenie on 06/01/16.
 */
public class SecurityTokenStore {

    private static final ESLogger log = ESLoggerFactory.getLogger(SecurityTokenStore.class.getName());

    private String prefix;
    private long validityDurationInSeconds;
    private LoadingCache<String, String> tokenCache;

    @Inject
    public SecurityTokenStore(Settings settings) {
        this.prefix = settings.get("duniter.auth.token.prefix", "duniter-");
        this.validityDurationInSeconds = settings.getAsInt("duniter.auth.tokenValidityDuration", 600 /*= 10min*/ );
        this.tokenCache = initGeneratedMessageCache();
    }

    public boolean validateToken(String token) {
        Preconditions.checkArgument(StringUtils.isNotBlank(token));

        String storedToken = tokenCache.getIfPresent(token);

        // if no value in cache => maybe token expired
        return ObjectUtils.equals(storedToken, token);
    }

    public String createNewToken(String challenge, String signature, String pubkey) {
        String token = newToken(challenge, signature, pubkey);
        tokenCache.put(challenge, challenge);
        return token;
    }

    /* -- internal methods -- */

    protected String newToken(String challenge, String signature, String pubkey) {
        return String.valueOf(pubkey + ":" + challenge + "|" + signature);
    }

    protected LoadingCache<String, String> initGeneratedMessageCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(validityDurationInSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String challenge) throws Exception {
                        // not used. Filled manually
                        return null;
                    }
                });
    }
}
