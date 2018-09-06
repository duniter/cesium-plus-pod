package org.duniter.elasticsearch.service.changes;

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

/*
    Copyright 2015 ForgeRock AS

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.duniter.core.exception.TechnicalException;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.util.bytes.BytesJsonNode;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.joda.time.DateTime;

import java.io.IOException;

public class ChangeEvents {

    private ChangeEvents() {
        // helper class
    }

    public static ChangeEvent fromJson(String json) {
        return fromJson(new ObjectMapper(), json);
    }

    public static ChangeEvent fromJson(ObjectMapper objectMapper, String json) {
        try {
            JsonNode actualObj = objectMapper.readTree(json);
            String index = actualObj.get("_index").asText();
            String type = actualObj.get("_type").asText();
            String id = actualObj.get("_id").asText();
            DateTime timestamp = new DateTime(actualObj.get("_timestamp").asLong());
            ChangeEvent.Operation operation = ChangeEvent.Operation.valueOf(actualObj.get("_operation").asText());
            long version = actualObj.get("_version").asLong();

            JsonNode sourceNode = actualObj.get("_source");
            BytesReference source = null;
            if (sourceNode != null) {
                source = new BytesJsonNode(sourceNode);
            }

            return new ChangeEvent(index, type, id, timestamp, operation, version, source);
        } catch (IOException e) {
            throw new InvalidFormatException("Invalid record JSON: " + e.getMessage(), e);
        }
    }

    public static String toJson(ChangeEvent event) {
        try {
            XContentBuilder builder = new XContentBuilder(JsonXContent.jsonXContent, new BytesStreamOutput());
            builder.startObject()
                    .field("_index", event.getIndex())
                    .field("_type", event.getType())
                    .field("_id", event.getId())
                    .field("_timestamp", event.getTimestamp())
                    .field("_version", event.getVersion())
                    .field("_operation", event.getOperation().toString());
            if (event.hasSource()) {
                builder.rawField("_source", event.getSource());
            }
            builder.endObject();

            return builder.string();
        } catch (IOException e) {
            throw new TechnicalException("Error while generating JSON from change event", e);
        }
    }

    public static JsonNode readTree(BytesReference source) throws IOException {
        if (source  == null) return null;

        if (source instanceof BytesJsonNode) {
            // Avoid new deserialization
            return ((BytesJsonNode) source).toJsonNode();
        }

        return new ObjectMapper().readTree(source.streamInput());
    }

    public static JsonNode readTree(ObjectMapper objectMapper, BytesReference source) throws IOException {
        if (source  == null) return null;

        if (source instanceof BytesJsonNode) {
            // Avoid new deserialization
            return ((BytesJsonNode) source).toJsonNode();
        }

        return objectMapper.readTree(source.streamInput());
    }

    public static <T> T readValue(BytesReference source, Class<T> clazz) throws IOException {
        if (source  == null) return null;

        return new ObjectMapper().readValue(source.streamInput(), clazz);
    }
}
