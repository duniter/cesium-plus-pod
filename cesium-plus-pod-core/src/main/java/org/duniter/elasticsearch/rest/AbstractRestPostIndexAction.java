package org.duniter.elasticsearch.rest;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
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

import org.duniter.core.exception.BusinessException;
import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;
import static org.elasticsearch.rest.RestStatus.OK;

public abstract class AbstractRestPostIndexAction extends BaseRestHandler {

    private final ESLogger log;

    private final JsonIndexer indexer;


    public AbstractRestPostIndexAction(Settings settings, RestController controller, Client client,
                                       RestSecurityController securityController,
                                       String indexName,
                                       String typeName,
                                       JsonIndexer indexer) {
        super(settings, controller, client);
        log = Loggers.getLogger("duniter.rest" + indexName, settings, String.format("[%s]", indexName));
        controller.registerHandler(POST,
                String.format("/%s/%s", indexName, typeName),
                this);
        securityController.allowIndexType(POST, indexName, typeName);
        securityController.allowIndexType(GET, indexName, typeName);
        this.indexer = indexer;
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel channel, Client client) throws Exception {

        try {
            String id = indexer.handleJson(request.content().toUtf8());
            channel.sendResponse(new BytesRestResponse(OK, id));
        }
        catch(DuniterElasticsearchException | BusinessException e) {
            log.error(e.getMessage(), e);
            channel.sendResponse(new XContentThrowableRestResponse(request, e));
        }
        catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    public interface JsonIndexer {
        String handleJson(String json) throws DuniterElasticsearchException, BusinessException;
    }



}