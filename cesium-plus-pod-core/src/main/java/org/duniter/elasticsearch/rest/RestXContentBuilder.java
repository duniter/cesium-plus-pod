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

import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestRequest;

import java.io.IOException;

public class RestXContentBuilder {

    public static XContentBuilder restContentBuilder(RestRequest request) throws IOException {
        XContentType contentType = XContentType.fromRestContentType(request.param("format", request.header("Content-Type")));
        if (contentType == null) {
            // default to JSON
            contentType = XContentType.JSON;
        }
        XContentBuilder builder = new XContentBuilder(XContentFactory.xContent(contentType),
                new BytesStreamOutput());
        if (request.paramAsBoolean("pretty", false)) {
            builder.prettyPrint().lfAtEnd();
        }
        String casing = request.param("case");
        if (casing != null && "camelCase".equals(casing)) {
            builder.fieldCaseConversion(XContentBuilder.FieldCaseConversion.CAMELCASE);
        } else {
            // we expect all REST interfaces to write results in underscore casing, so
            // no need for double casing
            builder.fieldCaseConversion(XContentBuilder.FieldCaseConversion.NONE);
        }
        return builder;
    }

    public static XContentBuilder emptyBuilder(RestRequest request) throws IOException {
        return restContentBuilder(request).startObject().endObject();
    }

}
