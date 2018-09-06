package org.duniter.elasticsearch.subscription.dao;

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


import com.fasterxml.jackson.core.JsonProcessingException;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.local.LocalEntity;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.ObjectUtils;
import org.duniter.elasticsearch.subscription.PluginSettings;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by Benoit on 30/03/2015.
 */
public abstract class AbstractSubscriptionIndexTypeDao<T extends AbstractSubscriptionIndexTypeDao> extends org.duniter.elasticsearch.dao.AbstractIndexTypeDao<T> implements SubscriptionIndexTypeDao<T> {

    protected PluginSettings pluginSettings;

    public AbstractSubscriptionIndexTypeDao(String index, String type, PluginSettings pluginSettings) {
        super(index, type);
        this.pluginSettings = pluginSettings;
    }

    @Override
    protected void createIndex() throws JsonProcessingException {
        throw new TechnicalException("not implemented");
    }

    @Override
    public void checkSameDocumentIssuer(String id, String expectedIssuer) {
       String issuer = getMandatoryFieldsById(id, Record.PROPERTY_ISSUER).get(Record.PROPERTY_ISSUER).toString();
       if (!ObjectUtils.equals(expectedIssuer, issuer)) {
           throw new TechnicalException("Not same issuer");
       }
    }

}
