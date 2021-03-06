package org.duniter.elasticsearch.model;

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


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by eis on 05/02/15.
 */
public class SearchScrollResponse extends SearchResponse {

    protected String scrollId;

    public SearchScrollResponse() {
        super();
    }

    @JsonGetter("_scroll_id")
    public String getScrollId() {
        return scrollId;
    }

    @JsonSetter("_scroll_id")
    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }
}