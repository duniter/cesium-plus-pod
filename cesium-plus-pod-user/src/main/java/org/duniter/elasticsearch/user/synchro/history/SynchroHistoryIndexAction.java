package org.duniter.elasticsearch.user.synchro.history;

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
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.synchro.SynchroActionResult;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.service.HistoryService;
import org.duniter.elasticsearch.synchro.AbstractSynchroAction;
import org.elasticsearch.common.inject.Inject;

public class SynchroHistoryIndexAction extends AbstractSynchroAction {

    private HistoryService service;
    @Inject
    public SynchroHistoryIndexAction(final Duniter4jClient client,
                                     PluginSettings pluginSettings,
                                     CryptoService cryptoService,
                                     ThreadPool threadPool,
                                     SynchroService synchroService,
                                     HistoryService service) {
        super(service.INDEX, service.DELETE_TYPE, client, pluginSettings.getDelegate(), cryptoService, threadPool);
        this.service = service;

        addValidationListener(this::onValidate);
        addInsertionListener(this::onInsert);

        synchroService.register(this);
    }

    /* -- protected method -- */

    protected void onValidate(String deleteId, JsonNode source, SynchroActionResult result) {
        try {
            // Check if valid document
            service.checkIsValidDeletion(source);

        } catch(NotFoundException e) {
            // doc not exists: continue
        }
    }

    protected void onInsert(String deleteId, JsonNode source, SynchroActionResult result) {
        try {
            // Delete the document
            service.applyDocDelete(source);

            result.addDelete();

        } catch(NotFoundException e) {
            // doc not exists: continue
            logger.debug("Doc to delete could not be found. Skipping deletion");
        }
    }
}
