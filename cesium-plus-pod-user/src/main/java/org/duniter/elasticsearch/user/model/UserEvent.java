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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.duniter.core.util.Preconditions;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.exception.TechnicalException;
import org.nuiton.i18n.I18n;

import java.util.Locale;

/**
 * Created by blavenie on 29/11/16.
 */
public class UserEvent extends Record {

    public enum EventType {
        INFO,
        WARN,
        ERROR
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(UserEvent.EventType type, String code) {
        return new Builder(type, code, null, null);
    }

    public static Builder newBuilder(UserEvent.EventType type, String code, String message, String... params) {
        return new Builder(type, code, message, params);
    }

    public static final String PROPERTY_ID="id";
    public static final String PROPERTY_TYPE="type";
    public static final String PROPERTY_CODE="code";
    public static final String PROPERTY_MESSAGE="message";
    public static final String PROPERTY_PARAMS="params";
    public static final String PROPERTY_REFERENCE="reference";
    public static final String PROPERTY_RECIPIENT="recipient";

    public static final String PROPERTY_READ_SIGNATURE="readSignature";


    private String id;

    private EventType type;

    private String recipient;

    private String code;

    private String message;

    private String[] params;

    private Reference reference;

    private String readSignature;

    public UserEvent() {
        super();
    }

    public UserEvent(EventType type, String code, String message, String... params) {
        super();
        this.type = type;
        this.code = code;
        this.message = message;
        this.params = params;
        setTime(getDefaultTime());
    }

    public UserEvent(UserEvent another) {
        super(another);
        this.type = another.getType();
        this.code = another.getCode();
        this.params = another.getParams();
        this.reference = (another.getReference() != null) ? new Reference(another.getReference()) : null;
        this.message = another.getMessage();
        this.recipient = another.getRecipient();
        this.readSignature = another.getReadSignature();
    }

    public EventType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String[] getParams() {
        return params;
    }

    public Reference getReference() {
        return reference;
    }

    public String getLocalizedMessage(Locale locale) {
        return I18n.l(locale, this.message, this.params);
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @JsonGetter("read_signature")
    public String getReadSignature() {
        return readSignature;
    }

    @JsonSetter("read_signature")
    public void setReadSignature(String readSignature) {
        this.readSignature = readSignature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private static long getDefaultTime() {
        return Math.round(1d * System.currentTimeMillis() / 1000);
    }

    public static class Builder {

        private UserEvent result;

        private Builder() {
            result = new UserEvent();
        }

        public Builder(UserEvent.EventType type, String code, String message, String... params) {
            result = new UserEvent(type, code, message, params);
        }

        public Builder setMessage(String message, String... params) {
            result.setMessage(message);
            result.setParams(params);
            return this;
        }

        public Builder setRecipient(String recipient) {
            result.setRecipient(recipient);
            return this;
        }

        public Builder setIssuer(String issuer) {
            result.setIssuer(issuer);
            return this;
        }

        public Builder setReference(String index, String type, String id) {
            result.setReference(new Reference(index, type, id));
            return this;
        }

        public Builder setReferenceHash(String hash) {
            Preconditions.checkNotNull(result.getReference(), "No reference set. Please call setReference() first");
            result.getReference().setHash(hash);
            return this;
        }

        public Builder setReference(String index, String type, String id, String anchor) {
            result.setReference(new Reference(index, type, id, anchor));
            return this;
        }

        public Builder setReferenceAnchor(String anchor) {
            Preconditions.checkNotNull(result.getReference(), "No reference set. Please call setReference() first");
            result.getReference().setAnchor(anchor);
            return this;
        }

        public Builder setTime(long time) {
            result.setTime(time);
            return this;
        }

        public UserEvent build() {
            if (result.getTime() == null) {
                result.setTime(getDefaultTime());
            }
            return new UserEvent(result);
        }
    }



    public static class Reference {

        public static final String PROPERTY_INDEX="index";
        public static final String PROPERTY_TYPE="type";
        public static final String PROPERTY_ID="id";
        public static final String PROPERTY_ANCHOR="anchor";
        public static final String PROPERTY_HASH="hash";

        private String index;

        private String type;

        private String id;

        private String anchor;

        private String hash;

        public Reference() {
        }

        public Reference(String index, String type, String id) {
            this(index, type, id, null);
        }

        public Reference(String index, String type, String id, String anchor) {
            this.index = index;
            this.type = type;
            this.id = id;
            this.anchor = anchor;
        }

        public Reference(Reference another) {
            this.index = another.getIndex();
            this.type = another.getType();
            this.id = another.getId();
            this.hash = another.getHash();
            this.anchor = another.getAnchor();
        }

        public String getIndex() {
            return index;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getAnchor() {
            return anchor;
        }

        public void setAnchor(String anchor) {
            this.anchor = anchor;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }
}
