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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.local.LocalEntity;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    protected <C> List<C> toList(SearchResponse response, final Function<SearchHit, C> mapper) {
        if (response.getHits() == null || response.getHits().getTotalHits() == 0) return ImmutableList.of();
        return Arrays.stream(response.getHits().getHits())
                .map(hit -> mapper.apply(hit))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected Stream<SearchHit> toStream(SearchResponse response) {
        if (response.getHits() == null || response.getHits().getTotalHits() == 0) return Stream.empty();
        return Arrays.stream(response.getHits().getHits());
    }

    protected <C extends LocalEntity<String>> List<C> toList(SearchResponse response, Class<? extends C> clazz) {
        final ObjectMapper objectMapper = getObjectMapper();
        return toList(response, hit -> readValueOrNull(objectMapper, hit, clazz));
    }

    protected <C extends LocalEntity<String>> List<C> toList(SearchRequestBuilder request, Class<? extends C> clazz) {

        final List<C> result = Lists.newArrayList();
        final ObjectMapper objectMapper = getObjectMapper();

        int size = this.pluginSettings.getIndexBulkSize();
        request.setSize(size);

        long total = -1;
        int from = 0;
        do {
            request.setFrom(from);
            SearchResponse response = request.execute().actionGet();
            toStream(response)
                    .map(hit -> readValueOrNull(objectMapper, hit, clazz))
                    .filter(Objects::nonNull)
                    .forEach(result::add);

            if (total == -1) total = response.getHits().getTotalHits();
            from += size;
        } while(from<total);

        return result;
    }

    protected <C> C readValueOrNull(ObjectMapper objectMapper, SearchHit hit, Class<C> clazz) {
        try {
            return objectMapper.readValue(hit.getSourceRef().streamInput(), clazz);
        }
        catch(IOException e) {
            logger.warn(String.format("Unable to deserialize source [%s/%s/%s] into [%s]: %s", hit.getIndex(), hit.getType(), hit.getId(), clazz.getName(), e.getMessage()));
            return null;
        }
    }

    protected Set<String> executeAndGetIds(SearchResponse response) {
        return toStream(response).map(SearchHit::getId).collect(Collectors.toSet());
    }

    protected Set<String> executeAndGetIds(SearchRequestBuilder request) {

        Set<String> result = Sets.newHashSet();
        int size = this.pluginSettings.getIndexBulkSize();
        request.setSize(size);

        long total = -1;
        int from = 0;
        do {
            request.setFrom(from);
            SearchResponse response = request.execute().actionGet();
            toStream(response).forEach(hit -> result.add(hit.getId()));

            if (total == -1) total = response.getHits().getTotalHits();
            from += size;
        } while(from<total);

        return result;
    }

}
