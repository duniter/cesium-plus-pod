package org.duniter.elasticsearch.rest.attachment;

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

import org.duniter.core.util.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.internal.Join;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestResponseListener;
import org.elasticsearch.search.fetch.source.FetchSourceContext;

import java.util.Arrays;
import java.util.Map;

import static org.elasticsearch.rest.RestStatus.OK;

public class RestImageAttachmentAction extends BaseRestHandler {

    @Inject
    public RestImageAttachmentAction(Settings settings, RestController controller, Client client) {
        super(settings, controller, client);
        controller.registerHandler(RestRequest.Method.GET, "/{index}/{type}/{id}/_image/{field}", this);
    }

    @Override
    protected void handleRequest(final RestRequest request, RestChannel channel, Client client) throws Exception {
        String index = request.param("index");
        String type = request.param("type");
        String id = request.param("id");
        String paramField = request.param("field");
        String[] fieldParts = paramField.split("\\.");
        String extension = null;
        if (fieldParts.length >= 2) {
            extension = fieldParts[fieldParts.length-1];
            paramField = Join.join(".", Arrays.copyOf(fieldParts, fieldParts.length-1));
        }

        final String field = paramField;
        final String expectedContentType = "image/" + extension;

        GetRequest getRequest = new GetRequest(index, type, id)
                .fields(field)
                .fetchSourceContext(FetchSourceContext.FETCH_SOURCE)
                .realtime(true);

        client.get(getRequest, new RestResponseListener<GetResponse>(channel) {
            @Override
            public RestResponse buildResponse(GetResponse response) throws Exception {
                if (response.getSource() == null || !response.getSource().containsKey(field)) {
                    return new BytesRestResponse(RestStatus.BAD_REQUEST, String.format("Field [%s] not exists.", field));
                }
                Object value = response.getSource().get(field);
                if (!(value instanceof Map)) {
                    return new BytesRestResponse(RestStatus.BAD_REQUEST, String.format("Field [%s] is not an attachment type.", field));
                }
                Map<String, String> attachment = (Map<String, String>)value;
                String contentType = attachment.get("_content_type");
                if (StringUtils.isBlank(contentType)) {
                    return new BytesRestResponse(RestStatus.BAD_REQUEST, String.format("Field [%s] not contains key [_content_type].", field));
                }

                if (!expectedContentType.equals(contentType)) {
                    return new BytesRestResponse(RestStatus.BAD_REQUEST, String.format("File extension not compatible with attachment content type [%s]", contentType));
                }

                return new BytesRestResponse(OK,
                        contentType,
                        new BytesArray(Base64.decode(attachment.get("_content"))));
            }
        });
    }


    public static String computeImageUrl(String index,
                                         String type,
                                         String id,
                                         String imageField,
                                         String contentType) {

        int lastSlashIndex = contentType  != null ? contentType.lastIndexOf('/') : -1;
        String extension = (lastSlashIndex >= 0) ? contentType.substring(lastSlashIndex+1) : contentType;

        return String.format("/%s/%s/%s/_image/%s.%s", index, type, id, imageField, extension);
    }
}