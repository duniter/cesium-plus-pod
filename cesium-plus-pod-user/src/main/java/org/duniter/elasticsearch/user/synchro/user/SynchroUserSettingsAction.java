package org.duniter.elasticsearch.user.synchro.user;

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
import org.duniter.elasticsearch.exception.AccessDeniedException;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.synchro.SynchroActionResult;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.dao.profile.UserSettingsDao;
import org.duniter.elasticsearch.user.service.UserService;
import org.duniter.elasticsearch.synchro.AbstractSynchroAction;
import org.elasticsearch.common.inject.Inject;

import java.util.Objects;

public class SynchroUserSettingsAction extends AbstractSynchroAction {

    @Inject
    public SynchroUserSettingsAction(Duniter4jClient client,
                                     PluginSettings pluginSettings,
                                     CryptoService cryptoService,
                                     ThreadPool threadPool,
                                     SynchroService synchroService) {
        super(UserService.INDEX, UserService.SETTINGS_TYPE, client, pluginSettings.getDelegate(), cryptoService, threadPool);

        setEnableUpdate(true); // with update

        addValidationListener(this::onValidate);

        synchroService.register(this);
    }

    protected void onValidate(String id, JsonNode source, SynchroActionResult result) {

        String issuer = getIssuer(source);
        if (!Objects.equals(issuer, id)) {
            throw new AccessDeniedException(String.format("Could not save this document: id must be equals to issuer."));
        }
    }
}
