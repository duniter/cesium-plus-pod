package org.duniter.elasticsearch.dao;

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
public class SaveResult implements Serializable {

    public static final String PROPERTY_INSERTS = "inserts";
    public static final String PROPERTY_UPDATES = "updates";

    private long insertTotal = 0;
    private long updateTotal = 0;
    private long deleteTotal = 0;
    private Map<String, Long> insertHits = new HashMap<>();
    private Map<String, Long> updateHits = new HashMap<>();
    private Map<String, Long> deleteHits = new HashMap<>();

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

    @JsonIgnore
    public long getInserts(String index, String type) {
        return insertHits.getOrDefault(index + "/" + type, 0l);
    }

    @JsonIgnore
    public long getUpdates(String index, String type) {
        return updateHits.getOrDefault(index + "/" + type, 0l);
    }

    public long getInserts() {
        return insertTotal;
    }

    public long getUpdates() {
        return updateTotal;
    }


    @JsonIgnore
    public long getDeletes(String index, String type) {
        return deleteHits.getOrDefault(index + "/" + type, 0l);
    }

    public long getDeletes() {
        return deleteTotal;
    }

    @JsonIgnore
    public long getTotal() {
        return insertTotal + updateTotal + deleteTotal;
    }

    public void setInserts(long inserts) {
        this.insertTotal = inserts;
    }
    public void setUpdates(long updates) {
        this.updateTotal = updates;
    }
    public void setDeletes(long deletes) {
        this.deleteTotal = deletes;
    }

    public String toString() {
        return String.format("%s insertions, %s updates, %s deletions,",
                insertTotal,
                updateTotal,
                deleteTotal);
    }
}
