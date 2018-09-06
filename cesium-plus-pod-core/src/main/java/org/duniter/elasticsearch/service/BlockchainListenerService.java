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


import com.fasterxml.jackson.core.JsonProcessingException;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.service.CryptoService;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.BlockStatDao;
import org.duniter.elasticsearch.dao.MovementDao;
import org.duniter.elasticsearch.model.Movement;
import org.duniter.elasticsearch.model.BlockchainBlockStat;
import org.duniter.elasticsearch.model.Movements;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * Created by Benoit on 26/04/2017.
 */
public class BlockchainListenerService extends AbstractBlockchainListenerService {

    private final BlockStatDao blockStatDao;
    private final MovementDao movementDao;

    @Inject
    public BlockchainListenerService(Duniter4jClient client,
                                     PluginSettings settings,
                                     CryptoService cryptoService,
                                     ThreadPool threadPool,
                                     BlockStatDao blockStatDao,
                                     MovementDao movementDao) {
        super("duniter.blockchain.listener", client, settings, cryptoService, threadPool,
                new TimeValue(500, TimeUnit.MILLISECONDS));
        this.blockStatDao = blockStatDao;
        this.movementDao = movementDao;
    }

    @Override
    protected void processBlockIndex(ChangeEvent change) {

        BlockchainBlock block = readBlock(change);

        // Block stat
        {
            BlockchainBlockStat stat = blockStatDao.toBlockStat(block);

            // Add a delete to bulk
            bulkRequest.add(client.prepareDelete(block.getCurrency(), BlockStatDao.TYPE, String.valueOf(block.getNumber()))
                    .setRefresh(false));
            flushBulkRequestOrSchedule();

            // Add a insert to bulk
            try {
                bulkRequest.add(client.prepareIndex(block.getCurrency(), BlockStatDao.TYPE, String.valueOf(block.getNumber()))
                        .setRefresh(false) // recommended for heavy indexing
                        .setSource(getObjectMapper().writeValueAsBytes(stat)));
                flushBulkRequestOrSchedule();
            } catch (JsonProcessingException e) {
                logger.error("Could not serialize BlockStat into JSON: " + e.getMessage(), e);
            }
        }

        // Movements
        {
            // Delete previous indexation
            bulkRequest = movementDao.bulkDeleteByBlock(block.getCurrency(),
                    String.valueOf(block.getNumber()),
                    null, /*do NOT filter on hash = delete by block number*/
                    bulkRequest, bulkSize, false);

            // Add a insert to bulk
            Movements.stream(block)
                .forEach(movement -> {
                    try {
                        bulkRequest.add(client.prepareIndex(block.getCurrency(), MovementDao.TYPE)
                                .setRefresh(false) // recommended for heavy indexing
                                .setSource(getObjectMapper().writeValueAsBytes(movement)));
                        flushBulkRequestOrSchedule();
                    } catch (JsonProcessingException e) {
                        logger.error("Could not serialize BlockOperation into JSON: " + e.getMessage(), e);
                    }
                });
        }
    }

    protected void processBlockDelete(ChangeEvent change) {
        // blockStat
        {
            // Add delete to bulk
            bulkRequest.add(client.prepareDelete(change.getIndex(), BlockStatDao.TYPE, change.getId())
                    .setRefresh(false));
        }

        // Operation
        {
            // Add delete to bulk
            bulkRequest = movementDao.bulkDeleteByBlock(
                    change.getIndex(),
                    change.getId(),
                    null/*do kwown the hash*/,
                    bulkRequest, bulkSize, false);
            flushBulkRequestOrSchedule();
        }
    }

    /* -- internal method -- */

}
