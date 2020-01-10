package org.duniter.elasticsearch.user.synchro.invitation;

/*-
 * #%L
 * Duniter4j :: ElasticSearch User plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.synchro.SynchroAction;
import org.duniter.elasticsearch.synchro.SynchroActionResult;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.execption.UserProfileNotFoundException;
import org.duniter.elasticsearch.user.model.Message;
import org.duniter.elasticsearch.user.service.UserInvitationService;
import org.duniter.elasticsearch.user.synchro.AbstractSynchroUserAction;
import org.duniter.elasticsearch.user.synchro.user.SynchroUserProfileAction;
import org.elasticsearch.common.inject.Inject;

public class SynchroInvitationCertificationIndexAction extends AbstractSynchroUserAction {

    private UserInvitationService service;

    // Execute AFTER user profiles
    public static final int EXECUTION_ORDER = Math.max(
            SynchroUserProfileAction.EXECUTION_ORDER + 10,
            SynchroAction.EXECUTION_ORDER_MIDDLE);

    @Inject
    public SynchroInvitationCertificationIndexAction(Duniter4jClient client,
                                                     PluginSettings pluginSettings,
                                                     CryptoService cryptoService,
                                                     ThreadPool threadPool,
                                                     SynchroService synchroService,
                                                     UserInvitationService service) {
        super(UserInvitationService.INDEX, UserInvitationService.CERTIFICATION_TYPE, client,
                pluginSettings, cryptoService, threadPool);

        this.service = service;

        setExecutionOrder(EXECUTION_ORDER);

        addValidationListener(this::onValidate);
        addInsertionListener(this::onInsert);

        synchroService.register(this);
    }

    protected void onInsert(String id, JsonNode source, SynchroActionResult result) {
        service.notifyUser(id, source);
    }

    protected void onValidate(String id, JsonNode source, SynchroActionResult result) {

        String recipient = source.get(Message.PROPERTY_RECIPIENT).asText();

        // Check recipient has settings or a profile
        if (!hasUserSettingsOrProfile(recipient)) {
            throw new UserProfileNotFoundException(String.format("Unknown message recipient {%.8s} (profile or settings not exists).", recipient));
        }

    }
}
