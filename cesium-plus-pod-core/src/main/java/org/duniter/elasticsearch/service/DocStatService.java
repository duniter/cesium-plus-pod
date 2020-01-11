package org.duniter.elasticsearch.service;

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


import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.duniter.core.util.DateUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.DocStatDao;
import org.duniter.elasticsearch.model.DocStat;
import org.duniter.elasticsearch.threadpool.ScheduledActionFuture;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Maintained stats on doc (count records)
 * Created by Benoit on 30/03/2015.
 */
public class DocStatService extends AbstractService  {

    private DocStatDao docStatDao;
    private ThreadPool threadPool;
    private List<StatDef> statDefs = Lists.newCopyOnWriteArrayList();

    public interface ComputeListener {
       void onCompute(DocStat stat);
    }

    public class StatDef {
        String index;
        String type;
        QueryBuilder query;
        String queryName;

        List<ComputeListener> listeners;
        StatDef(String index, String type) {
            this.index=index;
            this.type=type;
        }

        StatDef(String index, String type, String queryName, QueryBuilder query) {
            this.index=index;
            this.type=type;
            this.queryName = queryName;
            this.query = query;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof StatDef) &&
                    Objects.equals(((StatDef)obj).index, index) &&
                    Objects.equals(((StatDef)obj).type, type) &&
                    Objects.equals(((StatDef)obj).queryName, queryName);
        }

        public void addListener(ComputeListener listener) {
            if (listeners == null) {
                listeners = new ArrayList<>();
            }
            listeners.add(listener);
        }
    }

    @Inject
    public DocStatService(Duniter4jClient client, PluginSettings settings, ThreadPool threadPool,
                          DocStatDao docStatDao){
        super("duniter.data.stats", client, settings);
        this.threadPool = threadPool;
        this.docStatDao = docStatDao;
        setIsReady(true);
    }

    public DocStatService createIndexIfNotExists() {
        docStatDao.createIndexIfNotExists();
        return this;
    }

    public DocStatService deleteIndex() {
        docStatDao.deleteIndex();
        return this;
    }

    public DocStatService registerIndex(String index, String type) {
        return registerIndex(index, type, null, null, null);
    }

    public DocStatService registerIndex(String index, String type, String queryName, QueryBuilder query, ComputeListener listener) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        StatDef statDef = new StatDef(index, type, queryName, query);
        if (!statDefs.contains(statDef)) {
            statDefs.add(statDef);
        }

        if (listener != null) {
            addListener(index, type, listener);
        }

        return this;
    }

    public DocStatService addListener(String index, String type, String queryName, ComputeListener listener) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        Preconditions.checkNotNull(listener);

        // Find the existing def
        StatDef spec = new StatDef(index, type, queryName, null);
        StatDef statDef = statDefs.stream().filter(sd -> sd.equals(spec)).findFirst().get();
        Preconditions.checkNotNull(statDef);

        statDef.addListener(listener);
        return this;
    }

    public DocStatService addListener(String index, String type, ComputeListener listener) {
        addListener(index, type, null, listener);
        return this;
    }

    /**
     * Start scheduling doc stats update
     * @return
     */
    public ScheduledActionFuture<?> startScheduling() {
        long delayBeforeNextHour = DateUtils.delayBeforeNextHour();

        return threadPool.scheduleAtFixedRate(
                this::safeComputeStats,
                delayBeforeNextHour,
                60 * 60 * 1000 /* every hour */,
                TimeUnit.MILLISECONDS);
    }

    public void safeComputeStats() {
        try {
            computeStats();
        }
        catch(Exception e) {
            logger.error("Error during doc stats computation: " + e.getMessage(), e);
        }
    }

    public void computeStats() {

        // Skip if empty
        if (CollectionUtils.isEmpty(statDefs)) return;

        int bulkSize = pluginSettings.getIndexBulkSize();
        long now = System.currentTimeMillis()/1000;
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        DocStat stat = new DocStat();
        stat.setTime(now);

        int counter = 0;

        for (StatDef statDef: statDefs) {
            try {
                long count = docStatDao.countDoc(statDef.index, statDef.type, statDef.query);

                // Update stat properties (reuse existing obj)
                stat.setIndex(statDef.index);
                stat.setType(statDef.type);
                stat.setCount(count);

                // Apply the query name, to be able to filter the doc stats later
                if (StringUtils.isNotBlank(statDef.queryName)) {
                    stat.setQueryName(statDef.queryName);
                }

                // Call compute listeners if any
                if (CollectionUtils.isNotEmpty(statDef.listeners)) {
                    statDef.listeners.forEach(l -> l.onCompute(stat));
                }

                // Add insertion into bulk
                IndexRequestBuilder request = docStatDao.prepareIndex(stat);
                bulkRequest.add(request);
                counter++;

                // Flush the bulk if not empty
                if ((counter % bulkSize) == 0) {
                    client.flushBulk(bulkRequest);
                    bulkRequest = client.prepareBulk();
                }
            }
            catch(Exception e) {
                logger.error(String.format("Failed to execute doc stats on {%s/%s} %s: %s.",
                        statDef.index, statDef.type, statDef.index, statDef.queryName, e.getMessage()), e);
            }
        }

        // last flush
        if ((counter % bulkSize) != 0) {
            client.flushBulk(bulkRequest);
        }
    }


    public DocStatService startDataMigration() {
        if (!client.existsIndex(DocStatDao.OLD_INDEX)) return this; // Skip migration

        // Skip if empty
        if (CollectionUtils.isEmpty(statDefs)) return this;

        logger.info(String.format("Start document stats migration from {%s/%s} to {%s/%s}...",
                DocStatDao.OLD_INDEX, DocStatDao.OLD_TYPE, DocStatDao.INDEX, DocStatDao.TYPE));

        int size = Math.min(1000, pluginSettings.getIndexBulkSize());
        long now = System.currentTimeMillis()/1000;
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        SearchRequestBuilder searchRequest = client.prepareSearch(DocStatDao.OLD_INDEX)
                .setTypes(DocStatDao.OLD_TYPE)
                .setScroll("1m")
                .setSize(size)
                .setFetchSource(true);

        try {
            int from = 0;
            long total = -1;
            String scrollId = null;
            SearchResponse response = null;

            do {
                if (scrollId == null) {
                    response = searchRequest.setFrom(from)
                            .execute().actionGet();
                    scrollId = response.getScrollId();
                }
                else {
                    response = client.prepareSearchScroll(scrollId).get();
                }

                // Read response
                SearchHit[] searchHits = response.getHits().getHits();
                for (SearchHit searchHit : searchHits) {
                    Map<String, Object> source = searchHit.sourceAsMap();
                    if (source != null) {
                        DocStat stat = new DocStat();
                        stat.setIndex(MapUtils.getString(source, DocStat.PROPERTY_INDEX));
                        stat.setType(MapUtils.getString(source, DocStat.PROPERTY_INDEX_TYPE));
                        stat.setTime(MapUtils.getLongValue(source, DocStat.PROPERTY_TIME));
                        stat.setCount(MapUtils.getLongValue(source, DocStat.PROPERTY_COUNT));
                        stat.setQueryName(null); // was not exists in old index

                        // Add insertion into bulk
                        bulkRequest.add(docStatDao.prepareIndex(stat));
                    }
                }

                // Flush the bulk if not empty
                client.flushBulk(bulkRequest);
                bulkRequest = client.prepareBulk();

                // Prepare next iteration
                from += size;
                if (total == -1) total = response.getHits().getTotalHits();
            }
            while(from < total);

            // last flush
            client.flushBulk(bulkRequest);

            // Clear scroll (async)
            if (scrollId != null) {
                client.prepareClearScroll().addScrollId(scrollId).execute();
            }

            logger.info(String.format("Document stats migration succeed. %s stats migrated in %s ms. Deleting old index...",
                    total,
                    System.currentTimeMillis() - now));

        } catch (Exception e) {
            // Failed or no item on index
            logger.error(String.format("Error while doc stats migration: %s. Rollback migration.", e.getMessage()), e);

            // Clean the new index (to avoid duplicated entries next time migration is executed)
            deleteIndex().createIndexIfNotExists();

            // Do NOT delete if something wrong occur !
            return this;
        }

        // Delete the old index
        threadPool.scheduleOnClusterReady(() -> {
            client.deleteIndexIfExists(DocStatDao.OLD_INDEX);
        }).actionGet();

        return this;
    }
}
