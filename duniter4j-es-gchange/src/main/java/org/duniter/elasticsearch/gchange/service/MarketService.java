package org.duniter.elasticsearch.gchange.service;

/*
 * #%L
 * UCoin Java Client :: Core API
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
import org.duniter.core.client.model.elasticsearch.RecordComment;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.gchange.PluginSettings;
import org.duniter.elasticsearch.gchange.dao.market.MarketCommentDao;
import org.duniter.elasticsearch.gchange.dao.market.MarketIndexDao;
import org.duniter.elasticsearch.gchange.dao.market.MarketRecordDao;
import org.duniter.elasticsearch.user.service.HistoryService;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;

/**
 * Created by Benoit on 30/03/2015.
 */
public class MarketService extends AbstractService {

    private MarketIndexDao indexDao;
    private MarketRecordDao recordDao;
    private MarketCommentDao commentDao;
    private HistoryService historyService;

    @Inject
    public MarketService(Duniter4jClient client, PluginSettings settings,
                         CryptoService cryptoService,
                         HistoryService historyService,
                         MarketIndexDao indexDao,
                         MarketCommentDao commentDao,
                         MarketRecordDao recordDao
                         ) {
        super("gchange.service.market", client, settings, cryptoService);
        this.indexDao = indexDao;
        this.commentDao = commentDao;
        this.recordDao = recordDao;

        this.historyService = historyService;
    }


    /**
     * Create index need for blockchain registry, if need
     */
    public MarketService createIndexIfNotExists() {
        indexDao.createIndexIfNotExists();
        return this;
    }

    public MarketService deleteIndex() {
        indexDao.deleteIndex();
        return this;
    }


    public String indexRecordFromJson(String json) {
        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a %s from issuer [%s]", recordDao.getType(), issuer.substring(0, 8)));
        }

        return recordDao.create(json);
    }

    public void updateRecordFromJson(String id, String json) {
        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        // Check same document issuer
        recordDao.checkSameDocumentIssuer(id, issuer);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Updating %s [%s] from issuer [%s]", recordDao.getType(), id, issuer.substring(0, 8)));
        }

        recordDao.update(id, json);
    }

    public String indexCommentFromJson(String json) {
        JsonNode commentObj = readAndVerifyIssuerSignature(json);
        String issuer = getMandatoryField(commentObj, RecordComment.PROPERTY_ISSUER).asText();

        // Check the record document exists
        String recordId = getMandatoryField(commentObj, RecordComment.PROPERTY_RECORD).asText();
        checkRecordExistsOrDeleted(recordId);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing a %s from issuer [%s]", commentDao.getType(), issuer.substring(0, 8)));
        }
        return commentDao.create(json);
    }

    public void updateCommentFromJson(String id, String json) {
        JsonNode commentObj = readAndVerifyIssuerSignature(json);

        // Check the record document exists
        String recordId = getMandatoryField(commentObj, RecordComment.PROPERTY_RECORD).asText();
        checkRecordExistsOrDeleted(recordId);

        if (logger.isDebugEnabled()) {
            String issuer = getMandatoryField(commentObj, RecordComment.PROPERTY_ISSUER).asText();
            logger.debug(String.format("[%s] Indexing a %s from issuer [%s] on [%s]", commentDao.getType(), commentDao.getType(), issuer.substring(0, 8)));
        }

        commentDao.update(id, json);
    }


    /* -- Internal methods -- */

    // Check the record document exists (or has been deleted)
    private void checkRecordExistsOrDeleted(String id) {
        boolean recordExists;
        try {
            recordExists = recordDao.isExists(id);
        } catch (NotFoundException e) {
            // Check if exists in delete history
            recordExists = historyService.existsInDeleteHistory(recordDao.getIndex(), recordDao.getType(), id);
        }
        if (!recordExists) {
            throw new NotFoundException(String.format("Comment refers a non-existent document [%s/%s/%s].", recordDao.getIndex(), recordDao.getType(), id));
        }
    }
}
