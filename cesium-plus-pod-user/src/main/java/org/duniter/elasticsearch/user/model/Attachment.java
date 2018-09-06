package org.duniter.elasticsearch.user.model;

/*
 * #%L
 * Duniter4j :: Core Client API
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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.duniter.core.client.model.elasticsearch.Record;

/**
 * Created by blavenie on 01/03/16.
 */
public class Attachment extends Record {

    public static final String JSON_PROPERTY_CONTENT_TYPE = "_content_type";
    public static final String JSON_PROPERTY_CONTENT  = "_content";

    public static final String PROPERTY_CONTENT_TYPE = "contentType";
    public static final String PROPERTY_CONTENT = "content";

    private String contentType;

    private String content;

    @JsonSetter(JSON_PROPERTY_CONTENT_TYPE)
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonGetter(JSON_PROPERTY_CONTENT_TYPE)
    public String getContentType() {
        return contentType;
    }

    @JsonGetter(JSON_PROPERTY_CONTENT)
    public String getContent() {
        return content;
    }

    @JsonSetter(JSON_PROPERTY_CONTENT)
    public void setContent(String content) {
        this.content = content;
    }
}
