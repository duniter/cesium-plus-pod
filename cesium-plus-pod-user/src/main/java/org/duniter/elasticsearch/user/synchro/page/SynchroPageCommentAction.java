package org.duniter.elasticsearch.user.synchro.page;

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
import org.duniter.core.client.model.elasticsearch.RecordComment;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.synchro.SynchroAction;
import org.duniter.elasticsearch.synchro.SynchroActionResult;
import org.duniter.elasticsearch.synchro.SynchroService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.user.PluginSettings;
import org.duniter.elasticsearch.user.dao.page.PageCommentDao;
import org.duniter.elasticsearch.user.dao.page.PageIndexDao;
import org.duniter.elasticsearch.user.dao.page.PageRecordDao;
import org.duniter.elasticsearch.user.execption.UserProfileNotFoundException;
import org.duniter.elasticsearch.user.service.PageService;
import org.duniter.elasticsearch.user.synchro.AbstractSynchroUserAction;
import org.elasticsearch.common.inject.Inject;

public class SynchroPageCommentAction extends AbstractSynchroUserAction {

    // Execute AFTER page record (and with a medium priority)
    public static final int EXECUTION_ORDER = Math.max(
            SynchroPageRecordAction.EXECUTION_ORDER + 1,
            SynchroAction.EXECUTION_ORDER_MIDDLE);

    @Inject
    public SynchroPageCommentAction(Duniter4jClient client,
                                    PluginSettings pluginSettings,
                                    CryptoService cryptoService,
                                    ThreadPool threadPool,
                                    SynchroService synchroService) {
        super(PageIndexDao.INDEX, PageCommentDao.TYPE, client, pluginSettings, cryptoService, threadPool);

        setExecutionOrder(EXECUTION_ORDER);

        setEnableUpdate(true); // with update

        addValidationListener(this::onValidate);

        synchroService.register(this);
    }

    protected void onValidate(String id, JsonNode source, SynchroActionResult result) {

        String recordId = source.get(RecordComment.PROPERTY_RECORD).asText();

        // Check issuer has a user profile
        if (client.isDocumentExists(PageIndexDao.INDEX, PageRecordDao.TYPE, recordId)) {
            throw new UserProfileNotFoundException(String.format("Comment on an unknown page {%s}.", recordId));
        }
    }
}
