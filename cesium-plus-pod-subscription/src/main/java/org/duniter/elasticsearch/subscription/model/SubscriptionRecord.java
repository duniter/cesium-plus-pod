package org.duniter.elasticsearch.subscription.model;

/*
 * #%L
 * Duniter4j :: ElasticSearch GChange plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.duniter.core.client.model.elasticsearch.Record;

/**
 * Created by blavenie on 01/12/16.
 */
public class SubscriptionRecord<T> extends Record{

    public static final String PROPERTY_RECIPIENT = "recipient";

    public static final String PROPERTY_NONCE = "nonce";

    public static final String PROPERTY_RECIPIENT_CONTENT = "recipientContent";

    public static final String PROPERTY_ISSUER_CONTENT = "issuerContent";

    public static final String PROPERTY_CONTENT = "content";

    public static final String PROPERTY_TYPE = "type";

    private String recipient;
    private String nonce;
    private String recipientContent;
    private String issuerContent;
    private String type;
    private T content;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getRecipientContent() {
        return recipientContent;
    }

    public void setRecipientContent(String recipientContent) {
        this.recipientContent = recipientContent;
    }

    public String getIssuerContent() {
        return issuerContent;
    }

    public void setIssuerContent(String issuerContent) {
        this.issuerContent = issuerContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public T getContent() {
        return content;
    }

    @JsonIgnore
    public void setContent(T content) {
        this.content = content;
    }
}
