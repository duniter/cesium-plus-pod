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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.duniter.core.util.Preconditions;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.exception.TechnicalException;
import org.nuiton.i18n.I18n;

import java.util.Locale;

/**
 * Created by blavenie on 29/11/16.
 */
public class Message extends Record {


    public static final String PROPERTY_NONCE="nonce";
    public static final String PROPERTY_TITLE="title";
    public static final String PROPERTY_CONTENT="content";
    public static final String PROPERTY_RECIPIENT="recipient";
    public static final String PROPERTY_READ_SIGNATURE="read_signature";

    private String nonce;

    private String recipient;

    private String content;

    private String readSignature;

    public Message() {
        super();
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @JsonGetter(PROPERTY_READ_SIGNATURE)
    public String getReadSignature() {
        return readSignature;
    }

    @JsonSetter(PROPERTY_READ_SIGNATURE)
    public void setReadSignature(String readSignature) {
        this.readSignature = readSignature;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    @JsonIgnore
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsString(this);
        } catch(Exception e) {
            throw new TechnicalException(e);
        }
    }

}
