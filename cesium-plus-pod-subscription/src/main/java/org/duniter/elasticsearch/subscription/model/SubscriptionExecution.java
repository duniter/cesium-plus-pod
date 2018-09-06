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
public class SubscriptionExecution extends Record {

    public static final String PROPERTY_RECIPIENT = "recipient";

    public static final String PROPERTY_RECORD_TYPE = "recordType";

    public static final String PROPERTY_RECORD_ID = "recordId";

    private String recipient;
    private String recordType;
    private String recordId;

    private SubscriptionRecord record;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    @JsonIgnore
    public SubscriptionRecord getRecord() {
        return record;
    }

    @JsonIgnore
    public void setRecord(SubscriptionRecord record) {
        this.record = record;
    }
}
