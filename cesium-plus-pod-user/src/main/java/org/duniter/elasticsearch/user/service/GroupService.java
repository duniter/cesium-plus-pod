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
import org.apache.commons.collections4.MapUtils;
import org.duniter.core.client.model.elasticsearch.RecordComment;
import org.duniter.core.client.model.elasticsearch.UserGroup;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.NotFoundException;
import org.duniter.elasticsearch.user.dao.group.GroupCommentDao;
import org.duniter.elasticsearch.user.dao.group.GroupIndexDao;
import org.duniter.elasticsearch.user.dao.group.GroupRecordDao;
import org.duniter.elasticsearch.user.dao.page.PageIndexDao;
import org.duniter.elasticsearch.user.PluginSettings;
import org.elasticsearch.common.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Benoit on 30/03/2015.
 */
public class GroupService extends AbstractService {

    private GroupIndexDao indexDao;
    private GroupCommentDao commentDao;
    private GroupRecordDao recordDao;
    private HistoryService historyService;

    @Inject
    public GroupService(Duniter4jClient client,
                        PluginSettings settings,
                        CryptoService cryptoService,
                        GroupIndexDao indexDao,
                        GroupCommentDao commentDao,
                        GroupRecordDao recordDao,
                        HistoryService historyService) {
        super("duniter.group", client, settings, cryptoService);
        this.indexDao = indexDao;
        this.commentDao = commentDao;
        this.recordDao = recordDao;
        this.historyService = historyService;
    }

    /**
     * Create index need for blockchain registry, if need
     */
    public GroupService createIndexIfNotExists() {
        indexDao.createIndexIfNotExists();
        return this;
    }

    public GroupService deleteIndex() {
        indexDao.deleteIndex();
        return this;
    }

    /**
     *
     * Index an record
     * @param json
     * @return the record id
     */
    public String indexRecordProfileFromJson(String json) {

        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String title = getTitle(actualObj);
        String id = computeIdFromTitle(title);
        String issuer = getIssuer(actualObj);

        // Check time is valid - fix #27
        verifyTimeForInsert(actualObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Indexing group [%s] from issuer [%s]", id, issuer.substring(0, 8)));
        }

        return recordDao.create(id, json);
    }

    /**
     * Update a record
     * @param json
     */
    public void updateRecordFromJson(String id, String json) {

        JsonNode actualObj = readAndVerifyIssuerSignature(json);
        String issuer = getIssuer(actualObj);

        // Check same document issuer
        recordDao.checkSameDocumentIssuer(id, issuer);

        // Check time is valid - fix #27
        verifyTimeForUpdate(recordDao.getIndex(), recordDao.getType(), id, actualObj);

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

        // Check time is valid - fix #27
        verifyTimeForInsert(commentObj);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("[%s] Indexing new %s, issuer {%s}", PageIndexDao.INDEX, commentDao.getType(), issuer.substring(0, 8)));
        }
        return commentDao.create(json);
    }

    public void updateCommentFromJson(String id, String json) {
        JsonNode commentObj = readAndVerifyIssuerSignature(json);

        // Check the record document exists
        String recordId = getMandatoryField(commentObj, RecordComment.PROPERTY_RECORD).asText();
        checkRecordExistsOrDeleted(recordId);

        // Check time is valid - fix #27
        verifyTimeForUpdate(commentDao.getIndex(), commentDao.getType(), id, commentObj);

        if (logger.isDebugEnabled()) {
            String issuer = getMandatoryField(commentObj, RecordComment.PROPERTY_ISSUER).asText();
            logger.debug(String.format("[%s] Updating existing %s {%s}, issuer {%s}", PageIndexDao.INDEX, commentDao.getType(), id, issuer.substring(0, 8)));
        }

        commentDao.update(id, json);
    }

    public String getTitleById(String id) {

        Object title = client.getFieldById(recordDao.getIndex(), recordDao.getType(), id, UserGroup.PROPERTY_TITLE);
        if (title == null) return null;
        return title.toString();
    }

    public Map<String, String> getTitlesByNames(Set<String> ids) {

        Map<String, Object> titles = client.getFieldByIds(recordDao.getIndex(), recordDao.getType(), ids, UserGroup.PROPERTY_TITLE);
        if (MapUtils.isEmpty(titles)) return null;
        Map<String, String> result = new HashMap<>();
        titles.entrySet().forEach((entry) -> result.put(entry.getKey(), entry.getValue().toString()));
        return result;
    }

    /* -- Internal methods -- */


    protected String getTitle(JsonNode actualObj) {
        return  getMandatoryField(actualObj, UserGroup.PROPERTY_TITLE).asText();
    }

    protected String computeIdFromTitle(String title) {
        return computeIdFromTitle(title, 0);
    }

    protected String computeIdFromTitle(String title, int counter) {

        String id = title.replaceAll("\\s+", "");
        id  = id.replaceAll("[^a-zA−Z0-9_-]+", "");
        if (counter > 0) {
            id += "_" + counter;
        }

        if (!recordDao.isExists(id)) {
            return id;
        }

        return computeIdFromTitle(title, counter+1);
    }

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
