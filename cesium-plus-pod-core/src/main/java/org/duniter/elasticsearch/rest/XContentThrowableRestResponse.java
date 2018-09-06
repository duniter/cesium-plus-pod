package org.duniter.elasticsearch.rest;

/*
 * #%L
 * Duniter4j :: ElasticSearch Plugin
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

import org.duniter.elasticsearch.exception.DuniterElasticsearchException;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

import static org.elasticsearch.ExceptionsHelper.detailedMessage;

public class XContentThrowableRestResponse extends XContentRestResponse {

    public XContentThrowableRestResponse(RestRequest request, Throwable t) throws IOException {
        this(request, ((t instanceof ElasticsearchException) ? ((ElasticsearchException) t).status() : ((t instanceof DuniterElasticsearchException) ? ((DuniterElasticsearchException) t).status() : RestStatus.INTERNAL_SERVER_ERROR)), t);
    }

    public XContentThrowableRestResponse(RestRequest request, RestStatus status, Throwable t) throws IOException {
        super(request, status, convert(request, status, t));
    }

    private static XContentBuilder convert(RestRequest request, RestStatus status, Throwable t) throws IOException {
        XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                .field("error", detailedMessage(t))
                .field("status", status.getStatus());
        if (t != null && request.paramAsBoolean("error_trace", false)) {
            builder.startObject("error_trace");
            boolean first = true;
            while (t != null) {
                if (!first) {
                    builder.startObject("cause");
                }
                buildThrowable(t, builder);
                if (!first) {
                    builder.endObject();
                }
                t = t.getCause();
                first = false;
            }
            builder.endObject();
        }
        builder.endObject();
        return builder;
    }

    private static void buildThrowable(Throwable t, XContentBuilder builder) throws IOException {
        builder.field("message", t.getMessage());
        for (StackTraceElement stElement : t.getStackTrace()) {
            builder.startObject("at")
                    .field("class", stElement.getClassName())
                    .field("method", stElement.getMethodName());
            if (stElement.getFileName() != null) {
                builder.field("file", stElement.getFileName());
            }
            if (stElement.getLineNumber() >= 0) {
                builder.field("line", stElement.getLineNumber());
            }
            builder.endObject();
        }
    }
}