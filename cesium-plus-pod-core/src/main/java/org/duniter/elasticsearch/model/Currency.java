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


import org.duniter.core.client.model.bma.BlockchainParameters;

import java.io.Serializable;

/**
 * Created by eis on 05/02/15.
 */
public class Currency implements Serializable {

    public static final String PROPERTY_CURRENCY  = "currency";

    private String currency;
    private Integer membersCount;
    private String firstBlockSignature;
    private Long lastUD;
    private BlockchainParameters parameters;

    private String[] tags;
    private String issuer;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public String getFirstBlockSignature() {
        return firstBlockSignature;
    }

    public void setFirstBlockSignature(String firstBlockSignature) {
        this.firstBlockSignature = firstBlockSignature;
    }

    public Long getLastUD() {
        return lastUD;
    }

    public void setLastUD(Long lastUD) {
        this.lastUD = lastUD;
    }

    public BlockchainParameters getParameters() {
        return parameters;
    }

    public void setParameters(BlockchainParameters parameters) {
        this.parameters = parameters;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}