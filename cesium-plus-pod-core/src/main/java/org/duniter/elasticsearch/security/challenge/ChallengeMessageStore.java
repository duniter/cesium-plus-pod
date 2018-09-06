package org.duniter.elasticsearch.security.challenge;

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
public class ChallengeMessageStore {

    private static final ESLogger log = ESLoggerFactory.getLogger(ChallengeMessageStore.class.getName());


    private String prefix;
    private long validityDurationInSeconds;
    private LoadingCache<String, String> chalengeMessageCache;

    @Inject
    public ChallengeMessageStore(Settings settings) {
        this.prefix = settings.get("duniter4j.auth.challenge.prefix", "duniter4j-challenge-");
        this.validityDurationInSeconds = settings.getAsInt("duniter4j.auth.challengeValidityDuration", 10);
        this.chalengeMessageCache = initGeneratedMessageCache();
    }

    public boolean validateChallenge(String challenge) {
        Preconditions.checkArgument(StringUtils.isNotBlank(challenge));

        String storedChallenge = chalengeMessageCache.getIfPresent(challenge);

        // if no value in cache => maybe challenge expired
        return ObjectUtils.equals(storedChallenge, challenge);
    }

    public String createNewChallenge() {
        String challenge = newChallenge();
        chalengeMessageCache.put(challenge, challenge);
        return newChallenge();
    }

    /* -- internal methods -- */

    protected String newChallenge() {
        return String.valueOf(prefix + System.currentTimeMillis() * System.currentTimeMillis());
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
