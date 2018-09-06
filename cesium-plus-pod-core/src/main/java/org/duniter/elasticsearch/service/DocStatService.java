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


import org.apache.commons.collections4.CollectionUtils;
import org.duniter.core.util.DateUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.DocStatDao;
import org.duniter.elasticsearch.model.DocStat;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.inject.Inject;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Maintained stats on doc (count records)
 * Created by Benoit on 30/03/2015.
 */
public class DocStatService extends AbstractService  {

    private DocStatDao docStatDao;
    private ThreadPool threadPool;
    private List<StatDef> statDefs = new ArrayList<>();

    public interface ComputeListener {
       void onCompute(DocStat stat);
    }

    public class StatDef {
        String index;
        String type;
        List<ComputeListener> listeners;
        StatDef(String index, String type) {
            this.index=index;
            this.type=type;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof StatDef) &&
                    Objects.equals(((StatDef)obj).index, index) &&
                    Objects.equals(((StatDef)obj).type, type);
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
        return registerIndex(index, type, null);
    }

    public DocStatService registerIndex(String index, String type, ComputeListener listener) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        StatDef statDef = new StatDef(index, type);
        if (!statDefs.contains(statDef)) {
            statDefs.add(statDef);
        }

        if (listener != null) {
            addListener(index, type, listener);
        }

        return this;
    }

    public DocStatService addListener(String index, String type, ComputeListener listener) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        Preconditions.checkNotNull(listener);

        // Find the existsing def
        StatDef spec = new StatDef(index, type);
        StatDef statDef = statDefs.stream().filter(sd -> sd.equals(spec)).findFirst().get();
        Preconditions.checkNotNull(statDef);

        statDef.addListener(listener);
        return this;
    }

    /**
     * Start scheduling doc stats update
     * @return
     */
    public DocStatService startScheduling() {
        long delayBeforeNextHour = DateUtils.delayBeforeNextHour();

        threadPool.scheduleAtFixedRate(
                this::computeStats,
                delayBeforeNextHour,
                60 * 60 * 1000 /* every hour */,
                TimeUnit.MILLISECONDS);
        return this;
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
            long count = docStatDao.countDoc(statDef.index, statDef.type);

            // Update stat properties (resue existing obj)
            stat.setIndex(statDef.index);
            stat.setIndexType(statDef.type);
            stat.setCount(count);

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

        // last flush
        if ((counter % bulkSize) != 0) {
            client.flushBulk(bulkRequest);
        }
    }

}
