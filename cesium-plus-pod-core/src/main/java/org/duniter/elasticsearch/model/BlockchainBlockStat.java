package org.duniter.elasticsearch.model;

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

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by blavenie on 29/11/16.
 */
public class BlockchainBlockStat implements Serializable {

    public static final String PROPERTY_VERSION = "version";
    public static final String PROPERTY_CURRENCY = "currency";
    public static final String PROPERTY_NUMBER = "number";
    public static final String PROPERTY_ISSUER = "issuer";
    public static final String PROPERTY_HASH = "hash";
    public static final String PROPERTY_MEDIAN_TIME = "medianTime";
    public static final String PROPERTY_MEMBERS_COUNT = "membersCount";
    public static final String PROPERTY_MONETARY_MASS = "monetaryMass";
    public static final String PROPERTY_UNITBASE= "unitbase";
    public static final String PROPERTY_DIVIDEND = "dividend";
    public static final String PROPERTY_TX_COUNT = "txCount";
    public static final String PROPERTY_TX_AMOUNT = "txAmount";
    public static final String PROPERTY_TX_CHANGE_COUNT = "txChangeCount";
    public static final String PROPERTY_CERT_COUNT = "certCount";

    // Property copied from Block
    private int version;
    private String currency;
    private Integer number;
    private String issuer;
    private String hash;
    private Long medianTime;
    private Integer membersCount;
    private BigInteger monetaryMass;
    private Integer unitbase;
    private BigInteger dividend;

    // Statistics
    private Integer txCount;
    private BigInteger txAmount;
    private Integer txChangeCount;
    private Integer certCount;

    public BlockchainBlockStat() {
        super();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public BigInteger getDividend() {
        return dividend;
    }

    public void setDividend(BigInteger dividend) {
        this.dividend = dividend;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public BigInteger getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(BigInteger txAmount) {
        this.txAmount = txAmount;
    }

    public Integer getTxChangeCount() {
        return txChangeCount;
    }

    public void setTxChangeCount(Integer txChangeCount) {
        this.txChangeCount = txChangeCount;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getMedianTime() {
        return medianTime;
    }

    public void setMedianTime(Long medianTime) {
        this.medianTime = medianTime;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public BigInteger getMonetaryMass() {
        return monetaryMass;
    }

    public void setMonetaryMass(BigInteger monetaryMass) {
        this.monetaryMass = monetaryMass;
    }

    public Integer getUnitbase() {
        return unitbase;
    }

    public void setUnitbase(Integer unitbase) {
        this.unitbase = unitbase;
    }

    public Integer getCertCount() {
        return certCount;
    }

    public void setCertCount(Integer certCount) {
        this.certCount = certCount;
    }
}
