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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.BlockStatDao;
import org.duniter.elasticsearch.dao.MovementDao;
import org.duniter.elasticsearch.model.BlockchainBlockStat;
import org.duniter.elasticsearch.model.Movement;
import org.duniter.elasticsearch.model.Movements;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.util.bytes.JsonNodeBytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.unit.TimeValue;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Benoit on 26/04/2017.
 */
public class BlockchainListenerService extends AbstractBlockchainListenerService {

    private final BlockStatDao blockStatDao;
    private final MovementDao movementDao;

    private final boolean enableMovementIndexation;
    private final Collection<Pattern> txIncludesCommentPatterns;
    private final Collection<Pattern> txExcludesCommentPatterns;

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

        this.enableMovementIndexation = settings.enableMovementIndexation();

        // Include/Exclude TX by comment patterns
        this.txIncludesCommentPatterns = compilePatternsOrNull(settings.getMovementIncludesComment());
        this.txExcludesCommentPatterns = compilePatternsOrNull(settings.getMovementExcludesComment());

        if (settings.enableBlockchainIndexation()) {
            ChangeService.registerListener(this);
        }
    }

    @Override
    protected void processBlockIndex(ChangeEvent change) {

        ObjectMapper objectMapper = getObjectMapper();
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
                        .setSource(objectMapper.writeValueAsBytes(stat)));
                flushBulkRequestOrSchedule();
            } catch (JsonProcessingException e) {
                logger.error("Could not serialize BlockStat into JSON: " + e.getMessage(), e);
            }
        }

        // Movements
        if (enableMovementIndexation) {

            // Delete previous indexation
            bulkRequest = movementDao.bulkDeleteByBlock(block.getCurrency(),
                    String.valueOf(block.getNumber()),
                    null, /*do NOT filter on hash = delete by block number*/
                    bulkRequest, bulkSize, false);

            // Add a insert to bulk
            Movements.stream(block)
                .filter(this::filterMovement)
                .forEach(movement -> {
                    try {
                        bulkRequest.add(client.prepareIndex(block.getCurrency(), MovementDao.TYPE)
                                .setRefresh(false) // recommended for heavy indexing
                                .setSource(new JsonNodeBytesReference(movement, objectMapper)));
                        flushBulkRequestOrSchedule();
                    } catch (IOException e) {
                        logger.error("Could not serialize BlockOperation into JSON: " + e.getMessage(), e);
                    }
                });
        }

    }

    protected void processBlockDelete(ChangeEvent change) {
        // Block stat
        {
            // Add delete to bulk
            bulkRequest.add(client.prepareDelete(change.getIndex(), BlockStatDao.TYPE, change.getId())
                    .setRefresh(false));
        }

        // Movements
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

    protected Collection<Pattern> compilePatternsOrNull(String[] patterns) {
        return ArrayUtils.isEmpty(patterns) ? null :
                Arrays.stream(patterns)
                        .filter(StringUtils::isNotBlank)
                        .map(p -> "^" + p.replaceAll("[*]", ".*") + "$")
                        .map(Pattern::compile)
                        .collect(Collectors.toSet());
    }

    protected boolean filterMovement(Movement movement) {
        if (this.txIncludesCommentPatterns == null && this.txExcludesCommentPatterns == null) {
            return true;
        }
        String comment = StringUtils.trimToNull(movement.getComment());

        Boolean included = (this.txIncludesCommentPatterns == null) ? null :
                ((comment == null) ? Boolean.FALSE :
                        this.txIncludesCommentPatterns.stream()
                                .filter(pattern -> pattern.matcher(comment).matches()).map(p -> Boolean.TRUE)
                                .findFirst().orElse(Boolean.FALSE));
        Boolean excluded = (this.txExcludesCommentPatterns == null) ? null :
                ((comment == null) ? Boolean.FALSE :
                this.txExcludesCommentPatterns.stream()
                    .filter(pattern -> pattern.matcher(comment).matches()).map(p -> Boolean.TRUE)
                    .findFirst().orElse(Boolean.FALSE));

        boolean result = !Objects.equals(included, Boolean.FALSE) && !Objects.equals(excluded, Boolean.TRUE);
        return result;
    }
}
