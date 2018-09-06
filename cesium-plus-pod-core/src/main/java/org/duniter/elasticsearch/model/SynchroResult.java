package org.duniter.elasticsearch.model;

/*
 * #%L
 * Duniter4j :: ElasticSearch Core plugin
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by blavenie on 30/12/16.
 */
public class SynchroResult implements Serializable {

    public static final String PROPERTY_INSERTS = "inserts";
    public static final String PROPERTY_UPDATES = "updates";
    public static final String PROPERTY_DELETES = "deletes";
    public static final String PROPERTY_INVALID_SIGNATURES = "invalidSignatures";

    private long insertTotal = 0;
    private long updateTotal = 0;
    private long deleteTotal = 0;
    private long invalidSignatureTotal = 0;
    private long invalidTimeTotal = 0;
    private Map<String, Long> insertHits = new HashMap<>();
    private Map<String, Long> updateHits = new HashMap<>();
    private Map<String, Long> deleteHits = new HashMap<>();
    private Map<String, Long> invalidSignatureHits = new HashMap<>();
    private Map<String, Long> invalidTimeHits = new HashMap<>();

    public void addInserts(String index, String type, long nbHits) {
        insertHits.put(index + "/" + type, getInserts(index, type) + nbHits);
        insertTotal += nbHits;
    }

    public void addUpdates(String index, String type, long nbHits) {
        updateHits.put(index + "/" + type, getUpdates(index, type) + nbHits);
        updateTotal += nbHits;
    }

    public void addDeletes(String index, String type, long nbHits) {
        deleteHits.put(index + "/" + type, getDeletes(index, type) + nbHits);
        deleteTotal += nbHits;
    }

    public void addInvalidSignatures(String index, String type, long nbHits) {
        invalidSignatureHits.put(index + "/" + type, getDeletes(index, type) + nbHits);
        invalidSignatureTotal += nbHits;
    }


    public void addInvalidTimes(String index, String type, long nbHits) {
        invalidTimeHits.put(index + "/" + type, getDeletes(index, type) + nbHits);
        invalidTimeTotal += nbHits;
    }

    @JsonIgnore
    public long getInserts(String index, String type) {
        return insertHits.getOrDefault(index + "/" + type, 0l);
    }

    @JsonIgnore
    public long getUpdates(String index, String type) {
        return updateHits.getOrDefault(index + "/" + type, 0l);
    }

    @JsonIgnore
    public long getInvalidSignatures(String index, String type) {
        return invalidSignatureHits.getOrDefault(index + "/" + type, 0l);
    }

    @JsonIgnore
    public long getDeletes(String index, String type) {
        return deleteHits.getOrDefault(index + "/" + type, 0l);
    }

    public long getInserts() {
        return insertTotal;
    }

    public long getUpdates() {
        return updateTotal;
    }

    public long getDeletes() {
        return deleteTotal;
    }

    public long getInvalidSignatures() {
        return invalidSignatureTotal;
    }

    public long getInvalidTimes() {
        return invalidTimeTotal;
    }

    @JsonIgnore
    public long getTotal() {
        return insertTotal + updateTotal + deleteTotal;
    }

    public void setInserts(long inserts) {
        this.insertTotal = inserts;
    }
    public void setDeletes(long deletes) {
        this.deleteTotal = deletes;
    }
    public void setUpdates(long updates) {
        this.updateTotal = updates;
    }
    public void setInvalidSignatures(long invalidSignatures) {
        this.invalidSignatureTotal = invalidSignatures;
    }
    public void setInvalidTimes(long invalidTimes) {
        this.invalidTimeTotal = invalidTimes;
    }

    public String toString() {
        return new StringBuilder()
            .append("inserts [").append(insertTotal).append("]")
            .append(", updates [").append(updateTotal).append("]")
            .append(", deletes [").append(deleteTotal).append("]")
            .toString();
    }
}
