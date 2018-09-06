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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.local.LocalEntity;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.List;

/**
 * Created by Benoit on 08/04/2015.
 */
public abstract class AbstractDao implements Bean {


    protected final String loggerName;
    protected ESLogger logger;

    protected Duniter4jClient client;
    protected CryptoService cryptoService;
    protected PluginSettings pluginSettings;

    public AbstractDao(String loggerName) {
        super();
        this.loggerName = loggerName;
    }

    @Inject
    public void setClient(Duniter4jClient client) {
        this.client = client;
    }

    @Inject
    public void setCryptoService(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Inject
    public void setPluginSettings(PluginSettings pluginSettings) {
        this.pluginSettings = pluginSettings;
        this.logger = Loggers.getLogger(loggerName, pluginSettings.getSettings(), new String[0]);
    }

    /* -- protected methods  -- */

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }

    protected <C extends LocalEntity<String>> List<C> toList(SearchResponse response, Class<? extends C> clazz) {
        ObjectMapper objectMapper = getObjectMapper();

        if (response.getHits() == null || response.getHits().getTotalHits() == 0) return null;

        List<C> result = Lists.newArrayList();
        for (SearchHit hit: response.getHits().getHits()) {

            try {
                C value = objectMapper.readValue(hit.getSourceRef().streamInput(), clazz);
                value.setId(hit.getId());
                result.add(value);
            }
            catch(IOException e) {
                logger.warn(String.format("Unable to deserialize source [%s/%s/%s] into [%s]: %s", hit.getIndex(), hit.getType(), hit.getId(), clazz.getName(), e.getMessage()));
            }
        }
        return result;
    }

    protected List<String> toListIds(SearchResponse response) {
        if (response.getHits() == null || response.getHits().getTotalHits() == 0) return null;

        List<String> result = Lists.newArrayList();
        for (SearchHit hit: response.getHits().getHits()) {
            result.add(hit.getId());
        }
        return result;
    }
}
