package org.duniter.elasticsearch.user.synchro.like;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.exception.InvalidSignatureException;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.synchro.SynchroAction;
import org.duniter.elasticsearch.synchro.SynchroActionResult;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.model.LikeRecord;
import org.duniter.elasticsearch.user.service.LikeService;
import org.duniter.elasticsearch.user.synchro.AbstractSynchroUserAction;
import org.duniter.elasticsearch.user.synchro.group.SynchroGroupRecordAction;
import org.elasticsearch.common.inject.Inject;

public class SynchroLikeRecordAction extends AbstractSynchroUserAction {

    // Execute at the very end
    public static final int EXECUTION_ORDER = SynchroAction.EXECUTION_ORDER_END;

    private LikeService service;

    @Inject
    public SynchroLikeRecordAction(final Duniter4jClient client,
                                     PluginSettings pluginSettings,
                                     CryptoService cryptoService,
                                     ThreadPool threadPool,
                                     SynchroService synchroService,
                                     LikeService service) {
        super(service.INDEX, service.RECORD_TYPE, client, pluginSettings, cryptoService, threadPool);
        this.service = service;

        setExecutionOrder(EXECUTION_ORDER);

        // Disable signature validation, as anonymous issuer can occur
        setEnableSignatureValidation(false);

        addValidationListener(this::onValidate);

        synchroService.register(this);

    }

    /* -- protected method -- */

    protected void onValidate(String deleteId, JsonNode source, SynchroActionResult result) {

        JsonNode issuerNode = source.get(LikeRecord.ANONYMOUS_ISSUER);
        if (issuerNode == null || issuerNode.isMissingNode() || StringUtils.isBlank(issuerNode.asText())) {
            throw new InvalidSignatureException("Skipping 'like' document with anonymous issuer.");
        }

        // Check issuer signature
        try {
            readAndVerifyIssuerSignature(source, LikeRecord.ANONYMOUS_ISSUER);
        }
        catch(JsonProcessingException e) {
            throw new InvalidFormatException(e);
        }

        // Check if valid document
        try {
            service.checkIsValidLike(source, false);
        } catch(NotFoundException e) {
            // doc not exists: continue, because can be created later in synchro
        }
    }

    protected void onInsert(String likeId, JsonNode source, SynchroActionResult result) {
        // Notify users
        service.notifyOnInsert(source);
    }

}
