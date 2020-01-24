package org.duniter.elasticsearch.user.service;

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


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import org.apache.commons.collections4.MapUtils;
import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.user.model.Attachment;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.rest.attachment.RestImageAttachmentAction;
import org.duniter.elasticsearch.rest.share.AbstractRestShareLinkAction;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.exception.AccessDeniedException;
import org.duniter.elasticsearch.service.AbstractService;
import org.duniter.elasticsearch.user.dao.profile.UserIndexDao;
import org.duniter.elasticsearch.user.dao.profile.UserProfileDao;
import org.duniter.elasticsearch.user.dao.profile.UserSettingsDao;
import org.duniter.elasticsearch.util.opengraph.OGData;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Benoit on 30/03/2015.
 */
public class UserService extends AbstractService {


    private UserIndexDao indexDao;
    private UserProfileDao profileDao;
    private UserSettingsDao settingsDao;

    public static final String INDEX = "user";
    public static final String PROFILE_TYPE = "profile";
    public static final String SETTINGS_TYPE = "settings";

    @Inject
    public UserService(Duniter4jClient client,
                       PluginSettings settings,
                       CryptoService cryptoService,
                       UserIndexDao indexDao,
                       UserProfileDao profileDao,
                       UserSettingsDao settingsDao) {
        super("duniter." + INDEX, client, settings.getDelegate(), cryptoService);
        this.indexDao = indexDao;
        this.profileDao = profileDao;
        this.settingsDao = settingsDao;
    }

    /**
     * Create index need for blockchain mail, if need
     */
    public UserService createIndexIfNotExists() {
        indexDao.createIndexIfNotExists();
        return this;
    }

    /**
     * Create index need for blockchain mail, if need
     */
    public boolean isIndexExists() {
        return indexDao.existsIndex();
    }

    public UserService deleteIndex() {
        indexDao.deleteIndex();
        return this;
    }

    /**
     *
     * Index an user profile
     * @param json
     * @return the profile id
     */
    public String indexProfileFromJson(String json) {
        Preconditions.checkNotNull(json);

        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        // Check time is valid - fix #27
        verifyTimeForInsert(actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a %s from issuer [%.8s]", profileDao.getType(), issuer));
        }

        return profileDao.create(issuer, json);
    }

    /**
     * Update an user profile
     * @param id
     * @param json
     */
    public void updateProfileFromJson(String id, String json) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(json);

        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        if (!Objects.equals(issuer, id)) {
            throw new AccessDeniedException(String.format("Could not update this document: only the issuer can update."));
        }

        // Check same document issuer
        profileDao.checkSameDocumentIssuer(id, issuer);

        // Check time is valid - fix #27
        verifyTimeForUpdate(profileDao.getIndex(), profileDao.getType(), id, actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Updating a user profile from issuer [%.8s]", issuer));
        }

        profileDao.update(id, json);
    }

    /**
     *
     * Index an user settings
     * @param json settings, as JSON string
     * @return the settings id (=the issuer pubkey)
     */
    public String indexSettingsFromJson(String json) {

        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        // Check time is valid - fix #27
        verifyTimeForInsert(actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a user settings from issuer [%.8s]", issuer));
        }

        return settingsDao.create(issuer, json);
    }

    /**
     * Update user settings
     * @param id the doc id (should be =issuer)
     * @param json settings, as JSON string
     */
    public void updateSettingsFromJson(String id, String json) {

        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        if (!Objects.equals(issuer, id)) {
            throw new AccessDeniedException(String.format("Could not update this document: not issuer."));
        }

        // Check time is valid - fix #27
        try {
            verifyTimeForUpdate(INDEX, SETTINGS_TYPE, id, actualObj);
        }
        catch (NotFoundException e) {
          // Settings not exists yet (can occur when user change node in the app settings)
          indexSettingsFromJson(json);
          return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a user settings from issuer [%.8s]", issuer));
        }

        settingsDao.update(issuer, json);
    }

    public Optional<UserProfile> getProfileByPubkey(String pubkey, String... fieldNames) {
        UserProfile result = client.getSourceByIdOrNull(INDEX, PROFILE_TYPE, pubkey, UserProfile.class, fieldNames);
        if (result == null) return Optional.empty();
        return Optional.of(result);
    }

    public Map<String, UserProfile> getProfilesByPubkey(Set<String> pubkeys, String... fieldNames) {
        if (CollectionUtils.isEmpty(pubkeys)) return Maps.newHashMap();
        Map<String, UserProfile> result = client.getSourcesByIds(INDEX, PROFILE_TYPE, pubkeys, UserProfile.class, fieldNames);
        if (result == null) return Maps.newHashMap();
        return result;
    }

    public Optional<String> getProfileTitle(String pubkey) {
        Object title = client.getFieldById(INDEX, PROFILE_TYPE, pubkey, UserProfile.PROPERTY_TITLE);
        if (title == null) return Optional.empty();
        return Optional.of(title.toString().trim());
    }

    public Optional<Locale> getProfileLocale(String pubkey) {
        Object locale = client.getFieldById(INDEX, PROFILE_TYPE, pubkey, UserProfile.PROPERTY_LOCALE);
        if (locale == null) return Optional.empty();
        return Optional.of(new Locale(locale.toString()));
    }

    public Map<String, String> getProfileTitles(Set<String> pubkeys) {

        Map<String, Object> titles = client.getFieldByIds(INDEX, PROFILE_TYPE, pubkeys, UserProfile.PROPERTY_TITLE);
        if (MapUtils.isEmpty(titles)) return null;
        Map<String, String> result = new HashMap<>();
        titles.entrySet().forEach((entry) -> result.put(entry.getKey(), entry.getValue().toString()));
        return result;
    }

    public String joinNamesFromPubkeys(Set<String> pubkeys, String separator, boolean minify) {
        Preconditions.checkNotNull(pubkeys);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(pubkeys.size()>0);

        Map<String, String> profileTitles = getProfileTitles(pubkeys);
        return pubkeys.stream()
                .map(pubkey -> {
                    String title = profileTitles != null ? profileTitles.get(pubkey) : null;
                    // If title is too long, use the pubkey
                    return StringUtils.isNotBlank(title) && title.length() <= 30 ? title :
                            (minify ? ModelUtils.minifyPubkey(pubkey) : pubkey);
                }).collect(Collectors.joining(separator));
    }

    public String joinNamesFromProfiles(Set<String> pubkeys, Map<String, UserProfile> profiles, String separator, boolean minify) {
        Preconditions.checkNotNull(pubkeys);
        Preconditions.checkNotNull(separator);
        Preconditions.checkArgument(pubkeys.size()>0);
        Preconditions.checkNotNull(profiles);
        return pubkeys.stream()
                .map(pubkey -> {
                    UserProfile profile = profiles.get(pubkey);
                    String title = profile != null ? profile.getTitle() : null;
                    // If title is too long, use the pubkey
                    return StringUtils.isNotBlank(title) && title.length() <= 30 ? title :
                            (minify ? ModelUtils.minifyPubkey(pubkey) : pubkey);
                }).collect(Collectors.joining(separator));
    }

    public UserProfile getUserProfileForSharing(String id) {

        return client.getSourceByIdOrNull(INDEX, PROFILE_TYPE, id, UserProfile.class,
                UserProfile.PROPERTY_TITLE,
                UserProfile.PROPERTY_DESCRIPTION,
                UserProfile.PROPERTY_LOCALE,
                UserProfile.PROPERTY_AVATAR);
    }

    /* -- Internal methods -- */

}
