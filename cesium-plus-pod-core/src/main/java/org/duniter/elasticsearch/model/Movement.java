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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.local.LocalEntity;
import static org.duniter.core.util.Preconditions.*;
import org.duniter.elasticsearch.dao.BlockDao;

import java.io.Serializable;

/**
 * Created by blavenie on 29/11/16.
 */
public class Movement implements LocalEntity<String>, Serializable {


    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BlockchainBlock block) {
        return new Builder(block);
    }

    public static final String PROPERTY_CURRENCY = "currency";
    public static final String PROPERTY_MEDIAN_TIME = "medianTime";

    public static final String PROPERTY_VERSION = "version";
    public static final String PROPERTY_ISSUER = "issuer";
    public static final String PROPERTY_RECIPIENT = "recipient";
    public static final String PROPERTY_AMOUNT = "amount";
    public static final String PROPERTY_UNITBASE = "unitbase";
    public static final String PROPERTY_COMMENT = "comment";

    public static final String PROPERTY_IS_UD = "isUD";
    public static final String PROPERTY_REFERENCE = "reference";

    // ES identifier
    private String id;

    // Property copied from Block
    private String currency;
    private Long medianTime;

    // Property copied from Tx
    private int version;
    private String issuer;
    private String recipient;
    private Long amount;
    private Integer unitbase;
    private String comment;

    // Specific properties
    private boolean isUD;
    private Reference reference;

    public Movement() {
        super();
    }

    @Override
    @JsonIgnore
    public String getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Long getMedianTime() {
        return medianTime;
    }

    public void setMedianTime(Long medianTime) {
        this.medianTime = medianTime;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getUnitbase() {
        return unitbase;
    }

    public void setUnitbase(Integer unitbase) {
        this.unitbase = unitbase;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isUD() {
        return isUD;
    }

    public void setIsUD(boolean isUD) {
        this.isUD = isUD;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public static class Builder {

        private Movement result;

        private Builder() {
            result = new Movement();
        }

        public Builder(BlockchainBlock block) {
            this();
            setBlock(block);
        }

        public Builder setBlock(BlockchainBlock block) {
            result.setCurrency(block.getCurrency());
            result.setMedianTime(block.getMedianTime());
            result.setReference(new Reference(block.getCurrency(), BlockDao.TYPE, String.valueOf(block.getNumber())));
            setReferenceHash(block.getHash());
            return this;
        }

        public Builder setReferenceHash(String hash) {
            checkNotNull(result.getReference(), "No reference set. Please call setReference() first");
            result.getReference().setHash(hash);
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

        public Builder setVersion(int version) {
            result.setVersion(version);
            return this;
        }

        public Builder setComment(String comment) {
            result.setComment(comment);
            return this;
        }

        public Builder setAmount(long amount, int unitbase) {
            result.setAmount(amount);
            result.setUnitbase(unitbase);
            return this;
        }

        public Builder setIsUD(boolean isUD) {
            result.setIsUD(isUD);
            return this;
        }

        public Movement build() {
            checkNotNull(result);
            checkNotNull(result.getAmount());
            checkNotNull(result.getUnitbase());
            checkNotNull(result.getRecipient());
            checkNotNull(result.getIssuer());
            checkNotNull(result.getCurrency());
            checkNotNull(result.getVersion());

            return result;
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
