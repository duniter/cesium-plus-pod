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

import org.duniter.core.client.model.elasticsearch.Record;

/**
 * Created by blavenie on 29/11/16.
 */
public class LikeRecord extends Record {

    public enum Kind {
        VIEW,
        LIKE,
        DISLIKE,
        STAR,
        FOLLOW,
        ABUSE
    }

    public static final String ANONYMOUS_ISSUER="anonymous";

    public static final String PROPERTY_INDEX="index";
    public static final String PROPERTY_TYPE="type";
    public static final String PROPERTY_ID="id";
    public static final String PROPERTY_ANCHOR="anchor";
    public static final String PROPERTY_KIND ="kind";
    public static final String PROPERTY_COMMENT ="comment";
    public static final String PROPERTY_LEVEL ="level";

    private String index;
    private String type;
    private String id;
    private String anchor;

    private Kind kind;

    private String comment;
    private Integer level;

    public LikeRecord() {
        super();
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

}
