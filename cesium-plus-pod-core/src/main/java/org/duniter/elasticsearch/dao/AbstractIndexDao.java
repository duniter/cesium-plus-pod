package org.duniter.elasticsearch.dao;

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
import org.duniter.core.exception.TechnicalException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;

/**
 * Created by Benoit on 08/04/2015.
 */
public abstract class AbstractIndexDao<T extends IndexDao> extends AbstractDao implements IndexDao<T> {

    private final String index;

    public AbstractIndexDao(String index) {
        super("duniter.dao."+index);
        this.index = index;
    }

    /**
     * Create index
     * @throws JsonProcessingException
     */
    protected abstract void createIndex() throws JsonProcessingException;

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public T createIndexIfNotExists() {
        try {
            if (!client.existsIndex(index)) {
                createIndex();
            }
        }
        catch(JsonProcessingException e) {
            throw new TechnicalException(String.format("Error while creating index [%s]", index));
        }
        return (T)this;
    }

    @Override
    public T deleteIndex() {
        client.deleteIndexIfExists(index);
        return (T)this;
    }

    @Override
    public boolean existsIndex() {
        return client.existsIndex(index);
    }


    /* -- protected methods -- */

    protected void deleteIndexIfExists(){
        if (!client.existsIndex(index)) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("Deleting index [%s]", index));
        }

        DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin().indices().prepareDelete(index);
        deleteIndexRequestBuilder.execute().actionGet();
    }
}
