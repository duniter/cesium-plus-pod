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
import org.duniter.elasticsearch.dao.SaveResult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by blavenie on 30/12/16.
 */
public class SynchroResult extends SaveResult {

    public static final String PROPERTY_DELETES = "deletes";
    public static final String PROPERTY_INVALID_SIGNATURES = "invalidSignatures";

    private long deleteTotal = 0;
    private long invalidSignatureTotal = 0;
    private long invalidTimeTotal = 0;
    private Map<String, Long> deleteHits = new HashMap<>();
    private Map<String, Long> invalidSignatureHits = new HashMap<>();
    private Map<String, Long> invalidTimeHits = new HashMap<>();

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
    public long getInvalidSignatures(String index, String type) {
        return invalidSignatureHits.getOrDefault(index + "/" + type, 0l);
    }

    @JsonIgnore
    public long getDeletes(String index, String type) {
        return deleteHits.getOrDefault(index + "/" + type, 0l);
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
        return super.getTotal() + deleteTotal;
    }

    public void setDeletes(long deletes) {
        this.deleteTotal = deletes;
    }
    public void setInvalidSignatures(long invalidSignatures) {
        this.invalidSignatureTotal = invalidSignatures;
    }
    public void setInvalidTimes(long invalidTimes) {
        this.invalidTimeTotal = invalidTimes;
    }

    public String toString() {
        return String.format("%s, %s deletions, %s invalid",
                super.toString(),
                deleteTotal,
                invalidSignatureTotal + invalidTimeTotal
                );
    }
}
