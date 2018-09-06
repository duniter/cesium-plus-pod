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


import org.duniter.core.service.CryptoService;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.UserEvent;
import org.duniter.elasticsearch.user.model.UserProfile;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

import java.util.Locale;

/**
 * Created by Benoit on 30/03/2015.
 */
public class AdminService extends AbstractService {

    static {
        // Reserve i18n
        I18n.n("duniter.admin.event.subject.INFO");
        I18n.n("duniter.admin.event.subject.WARN");
        I18n.n("duniter.admin.event.subject.ERROR");
    }

    private final UserEventService userEventService;
    private final MailService mailService;

    @Inject
    public AdminService(final Duniter4jClient client,
                        final PluginSettings pluginSettings,
                        final CryptoService cryptoService,
                        final UserEventService userEventService,
                        final MailService mailService) {
        super("duniter.admin", client, pluginSettings, cryptoService);
        this.userEventService = userEventService;
        this.mailService = mailService;
    }

    /**
     * Notify cluster admin
     */
    public void notifyAdmin(UserEvent event) {
        Preconditions.checkNotNull(event);

        String nodePubkey = pluginSettings.getNodePubkey();

        UserProfile adminProfile;
        if (StringUtils.isNotBlank(nodePubkey) && !pluginSettings.isRandomNodeKeypair()) {
            adminProfile = getUserProfile(nodePubkey, UserProfile.PROPERTY_EMAIL, UserProfile.PROPERTY_LOCALE);
        }
        else {
            adminProfile = new UserProfile();
        }

        // Add new event to index
        Locale locale = StringUtils.isNotBlank(adminProfile.getLocale()) ?
                new Locale(adminProfile.getLocale()) :
                I18n.getDefaultLocale();
        if (StringUtils.isNotBlank(nodePubkey)) {
            event.setRecipient(nodePubkey);
            userEventService.indexEvent(locale, event);
        }

        // Send email to admin
        String adminEmail = StringUtils.isNotBlank(adminProfile.getEmail()) ?
                adminProfile.getEmail() :
                pluginSettings.getMailAdmin();
        if (StringUtils.isNotBlank(adminEmail)) {
            String subjectPrefix = pluginSettings.getMailSubjectPrefix();
            mailService.sendTextEmail(
                    I18n.l(locale, "duniter.admin.event.subject."+event.getType().name(), subjectPrefix),
                    event.getLocalizedMessage(locale),
                    adminEmail);
        }
    }

    /* -- Internal methods -- */

    private UserProfile getUserProfile(String pubkey, String... fieldnames) {
        UserProfile result = client.getSourceByIdOrNull(UserService.INDEX, UserService.PROFILE_TYPE, pubkey, UserProfile.class, fieldnames);
        if (result == null) result = new UserProfile();
        return result;
    }



}
