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


import com.google.common.collect.ImmutableList;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.Preconditions;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.TimeValue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benoit on 26/04/2017.
 */
public abstract class AbstractBlockchainListenerService extends AbstractService implements ChangeService.ChangeListener {

    private static final List<ChangeSource> CHANGE_LISTEN_SOURCES = ImmutableList.of(new ChangeSource("*", BlockchainService.BLOCK_TYPE));

    private static final String LOCK_NAME = "blockchain-listenerListenerService";

    protected final boolean enable;
    protected final String listenerId;
    protected final ThreadPool threadPool;
    protected final int bulkSize;

    private final TimeValue flushInterval;
    protected final Object threadLock = Boolean.TRUE;
    protected BulkRequestBuilder bulkRequest;
    protected boolean flushing;


    @Inject
    public AbstractBlockchainListenerService(String loggerName,
                                             Duniter4jClient client,
                                             PluginSettings settings,
                                             CryptoService cryptoService,
                                             ThreadPool threadPool,
                                             TimeValue processingInterval,
                                             boolean enable) {
        super(loggerName, client, settings, cryptoService);
        this.listenerId = loggerName;
        this.enable = enable;
        this.threadPool = threadPool;

        this.bulkSize = pluginSettings.getIndexBulkSize();
        this.bulkRequest = client.prepareBulk();

        this.flushInterval = processingInterval;
        this.flushing = false;

        if (this.enable) {
            ChangeService.registerListener(this);
        }
    }


    @Override
    public String getId() {
        return listenerId;
    }

    @Override
    public void onChange(ChangeEvent change) {

        // Skip _id=current
        if("current".equals(change.getId())) return;

        switch (change.getOperation()) {
            // on INDEX
            case CREATE:
            case INDEX:
                if (change.getSource() != null) {
                    synchronized (threadLock) {
                        processBlockIndex(change);
                    }
                }
                break;

            // on DELETE
            case DELETE:
                synchronized (threadLock) {
                    processBlockDelete(change);
                }
                break;
        }

    }

    @Override
    public Collection<ChangeSource> getChangeSources() {
        return CHANGE_LISTEN_SOURCES;
    }

    /* -- internal method -- */


    protected abstract void processBlockIndex(ChangeEvent change);

    protected abstract void processBlockDelete(ChangeEvent change);

    protected void flushBulkRequestOrSchedule() {
        if (bulkRequest.numberOfActions() == 0) return;

        // Flush now, if bulk is full
        if (bulkRequest.numberOfActions() % bulkSize == 0) {
            flushBulk();
            flushing = false;
        }
        else if (!flushing){
            flushing = true;
            // Flush later (after the current block processing)
            threadPool.schedule(() -> {
                if (flushing) {
                    try {
                        flushBulk();
                    } finally {
                        flushing = false;
                    }
                }
            }, new TimeValue(500, TimeUnit.MILLISECONDS));
        }
    }

    protected void flushBulk() {
        if (bulkRequest.numberOfActions() != 0) {
            synchronized (threadLock) {
                if (bulkRequest.numberOfActions() != 0) {
                    client.flushBulk(bulkRequest);
                    bulkRequest = client.prepareBulk();
                }
            }
        }
    }

    protected BlockchainBlock readBlock(ChangeEvent change) {
        Preconditions.checkNotNull(change);
        Preconditions.checkNotNull(change.getSource());

        try {
            return getObjectMapper().readValue(change.getSource().streamInput(), BlockchainBlock.class);
        } catch (IOException e) {
            throw new TechnicalException(String.format("Unable to parse received block %s", change.getId()), e);
        }
    }

}
