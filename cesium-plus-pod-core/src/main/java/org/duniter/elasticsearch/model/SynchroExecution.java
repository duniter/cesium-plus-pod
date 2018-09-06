package org.duniter.elasticsearch.model;

/*-
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

import org.duniter.core.client.model.elasticsearch.Record;

public class SynchroExecution extends Record {

    public static final String PROPERTY_CURRENCY = "currency";
    public static final String PROPERTY_PEER = "peer";
    public static final String PROPERTY_API = "api";
    public static final String PROPERTY_RESULT = "result";
    public static final String PROPERTY_EXECUTION_TIME = "executionTime";


    private String currency;
    private String peer;
    private String api;
    private Long executionTime;
    private SynchroResult result;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public SynchroResult getResult() {
        return result;
    }

    public void setResult(SynchroResult result) {
        this.result = result;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }
}
