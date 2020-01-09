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
import org.apache.commons.io.Charsets;
import org.duniter.core.exception.TechnicalException;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.joda.time.DateTime;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ChangeEvent {
    private final String id;
    private final String index;
    private final String type;
    private final DateTime timestamp;
    private final Operation operation;
    private final long version;
    private final BytesReference source;
    private String sourceText; // cache

    public enum Operation {
        INDEX,CREATE,DELETE
    }

    public ChangeEvent(String index, String type, String id, DateTime timestamp, Operation operation, long version, BytesReference source) {
        this.id = id;
        this.index = index;
        this.type = type;
        this.timestamp = timestamp;
        this.operation = operation;
        this.version = version;
        this.source = source;
    }

    protected ChangeEvent(ChangeEvent event, boolean copySource) {
        this.id = event.getId();
        this.index = event.getIndex();
        this.type = event.getType();
        this.timestamp = event.getTimestamp();
        this.operation = event.getOperation();
        this.version = event.getVersion();
        this.source = copySource ? event.getSource() : null;
    }

    public String getId() {
        return id;
    }

    public Operation getOperation() {
        return operation;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public long getVersion() {
        return version;
    }

    public BytesReference getSource() {
        return source;
    }

    @JsonIgnore
    public boolean hasSource() {
        return source != null;
    }

    @JsonIgnore
    public String getSourceText(){
        if (sourceText != null) return sourceText;
        if (source == null) return null;
        try {
            sourceText = new String(source.toBytes(), StandardCharsets.UTF_8);
            return sourceText;
        } catch (Exception e) {
            throw new TechnicalException("Error while generating JSON from source", e);
        }
    }

}
