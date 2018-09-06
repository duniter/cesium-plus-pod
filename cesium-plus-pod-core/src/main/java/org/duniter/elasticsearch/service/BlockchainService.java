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


import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.BlockchainRemoteService;
import org.duniter.core.client.service.bma.NetworkRemoteService;
import org.duniter.core.client.service.exception.BlockNotFoundException;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.model.NullProgressionModel;
import org.duniter.core.model.ProgressionModel;
import org.duniter.core.model.ProgressionModelImpl;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.json.JsonAttributeParser;
import org.duniter.core.util.websocket.WebsocketClientEndpoint;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.exception.DuplicateIndexIdException;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.inject.Inject;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.*;

/**
 * Created by Benoit on 30/03/2015.
 */
public class BlockchainService extends AbstractService {

    public static final String BLOCK_TYPE = BlockDao.TYPE;
    public static final String CURRENT_BLOCK_ID = "current";

    private static final int SYNC_MISSING_BLOCK_MAX_RETRY = 5;

    private final ProgressionModel nullProgressionModel = new NullProgressionModel();

    private BlockchainRemoteService blockchainRemoteService;
    private List<WebsocketClientEndpoint.ConnectionListener> connectionListeners = new ArrayList<>();
    private final WebsocketClientEndpoint.ConnectionListener dispatchConnectionListener;

    private final JsonAttributeParser<Integer> blockNumberParser = new JsonAttributeParser<>("number", Integer.class);
    private final JsonAttributeParser<String> blockCurrencyParser = new JsonAttributeParser<>("currency", String.class);
    private final JsonAttributeParser<String> blockHashParser = new JsonAttributeParser<>("hash", String.class);
    private final JsonAttributeParser<String> blockPreviousHashParser = new JsonAttributeParser<>("previousHash", String.class);

    private BlockDao blockDao;

    @Inject
    public BlockchainService(Duniter4jClient client,
                             PluginSettings settings,
                             ThreadPool threadPool,
                             BlockDao blockDao,
                             final ServiceLocator serviceLocator){
        super("duniter.blockchain", client, settings);
        this.client = client;
        this.blockDao = blockDao;
        threadPool.scheduleOnStarted(() -> {
            blockchainRemoteService = serviceLocator.getBlockchainRemoteService();
            setIsReady(true);
        });
        dispatchConnectionListener = new WebsocketClientEndpoint.ConnectionListener() {
            @Override
            public void onSuccess() {
                synchronized (connectionListeners) {
                    connectionListeners.stream().forEach(connectionListener -> connectionListener.onSuccess());
                }
            }
            @Override
            public void onError(Exception e, long lastTimeUp) {
                synchronized (connectionListeners) {
                    connectionListeners.stream().forEach(connectionListener -> connectionListener.onError(e, lastTimeUp));
                }
            }
        };
    }


    public void registerConnectionListener(WebsocketClientEndpoint.ConnectionListener listener) {
        synchronized (connectionListeners) {
            connectionListeners.add(listener);
        }
    }

    public BlockchainService listenAndIndexNewBlock(final Peer peer){
        WebsocketClientEndpoint wsEndPoint = blockchainRemoteService.addBlockListener(peer, message -> indexLastBlockFromJson(peer, message), true /*autoreconnect*/);
        wsEndPoint.registerListener(dispatchConnectionListener);
        return this;
    }

    public BlockchainService indexLastBlocks(Peer peer) {
        indexLastBlocks(peer, nullProgressionModel);
        return this;
    }

    public BlockchainService indexLastBlocks(Peer peer, ProgressionModel progressionModel) {
        boolean bulkIndex = pluginSettings.isIndexBulkEnable();

        progressionModel.setStatus(ProgressionModel.Status.RUNNING);
        progressionModel.setTotal(100);
        long timeStart = System.currentTimeMillis();

        try {
            // Get the blockchain name from node
            BlockchainParameters parameter = blockchainRemoteService.getParameters(peer);
            if (parameter == null) {
                progressionModel.setStatus(ProgressionModel.Status.FAILED);
                logger.error(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.remoteParametersError",peer));
                return this;
            }
            String currencyName = parameter.getCurrency();

            progressionModel.setTask(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.task", currencyName, peer));
            logger.info(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.task", currencyName, peer));

            // Then index allOfToList blocks
            BlockchainBlock peerCurrentBlock = blockchainRemoteService.getCurrentBlock(peer);

            if (peerCurrentBlock != null) {
                final int peerCurrentBlockNumber = peerCurrentBlock.getNumber();

                // Get the last indexed block number
                int startNumber = 0;

                // Check if a previous sync has been done
                BlockchainBlock indexedCurrentBlock = getCurrentBlock(currencyName);
                if (indexedCurrentBlock != null && indexedCurrentBlock.getNumber() != null) {
                    int indexedCurrentBlockNumber = indexedCurrentBlock.getNumber();

                    // Make sure this block has been indexed by its number (not only with _id='current')
                    indexedCurrentBlock = getBlockById(currencyName, indexedCurrentBlockNumber);

                    // If current block exists on index, by _id=number AND _id=current
                    // then keep it and sync only next blocks
                    if (indexedCurrentBlock != null) {
                        startNumber = indexedCurrentBlockNumber + 1;
                    }
                }

                // When current block not found,
                // try to use the max(number), because block with _id='current' may not has been indexed
                if (startNumber <= 1 ){
                    startNumber = blockDao.getMaxBlockNumber(currencyName) + 1;
                }

                // If some block has been already indexed: detect and resolve fork
                if (startNumber > 0) {
                    String peerStartPreviousHash;
                    try {
                        BlockchainBlock peerStartBlock = blockchainRemoteService.getBlock(peer, startNumber - 1);
                        peerStartPreviousHash = peerStartBlock.getHash();
                    }
                    catch(BlockNotFoundException e) {
                        // block not exists: use a fake hash for fork detection (will force to compare previous blocks)
                        peerStartPreviousHash = "--";
                    }
                    boolean resolved = detectAndResolveFork(peer, currencyName, peerStartPreviousHash, startNumber - 1);
                    if (!resolved) {
                        // Bad blockchain ! skipping sync
                        logger.error(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.invalidBlockchain", currencyName, peer));
                        return this;
                    }
                }

                if (startNumber <= peerCurrentBlockNumber) {
                    Collection<String> missingBlocks = bulkIndex
                            ? indexBlocksUsingBulk(peer, currencyName, startNumber, peerCurrentBlockNumber, progressionModel, true)
                            : indexBlocksNoBulk(peer, currencyName, startNumber, peerCurrentBlockNumber, progressionModel, true);

                    // If some blocks are missing, try to get it using other peers
                    if (CollectionUtils.isNotEmpty(missingBlocks)) {
                        progressionModel.setTask(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.otherPeers.task", currencyName));
                        missingBlocks = indexMissingBlocksFromOtherPeers(peer, peerCurrentBlock, missingBlocks, 1);
                    }

                    if (CollectionUtils.isEmpty(missingBlocks)) {
                        logger.info(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.succeed", currencyName, peer, (System.currentTimeMillis() - timeStart)));
                        progressionModel.setStatus(ProgressionModel.Status.SUCCESS);
                    }
                    else {
                        logger.warn(String.format("[%s] [%s] Could not indexed some blocks. Missing %s blocks.", currencyName, peer, missingBlocks.size()));
                        progressionModel.setStatus(ProgressionModel.Status.FAILED);
                    }
                }
                else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("[%s] [%s] Already up to date at block #%s.", currencyName, peer, peerCurrentBlockNumber));
                    }
                    progressionModel.setStatus(ProgressionModel.Status.SUCCESS);
                }
            }
        } catch(Exception e) {
            logger.error("Error during indexLastBlocks: " + e.getMessage(), e);
            progressionModel.setStatus(ProgressionModel.Status.FAILED);
        }

        return this;
    }

    public BlockchainService indexBlocksRange(Peer peer, int firstNumber, int lastNumber) {
        indexBlocksRange(peer, nullProgressionModel, firstNumber, lastNumber);
        return this;
    }

    public BlockchainService indexBlocksRange(Peer peer, ProgressionModel progressionModel, int firstNumber, int lastNumber) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(progressionModel);
        Preconditions.checkArgument(firstNumber < lastNumber);

        boolean bulkIndex = pluginSettings.isIndexBulkEnable();

        progressionModel.setStatus(ProgressionModel.Status.RUNNING);
        progressionModel.setTotal(100);
        long timeStart = System.currentTimeMillis();

        try {
            // Get the blockchain name from node
            BlockchainParameters parameter = blockchainRemoteService.getParameters(peer);
            if (parameter == null) {
                progressionModel.setStatus(ProgressionModel.Status.FAILED);
                logger.error(I18n.t("duniter4j.blockIndexerService.indexBlocksRange.remoteParametersError",peer));
                return this;
            }
            String currencyName = parameter.getCurrency();

            progressionModel.setTask(I18n.t("duniter4j.blockIndexerService.indexBlocksRange.task", currencyName, peer, firstNumber, lastNumber));
            logger.info(I18n.t("duniter4j.blockIndexerService.indexBlocksRange.task", currencyName, peer, firstNumber, lastNumber));

            // Then index allOfToList blocks
            BlockchainBlock peerCurrentBlock = blockchainRemoteService.getCurrentBlock(peer);

            if (peerCurrentBlock != null) {
                final int peerCurrentBlockNumber = peerCurrentBlock.getNumber();


                boolean isLastCurrent = lastNumber >= peerCurrentBlockNumber;
                if (lastNumber > peerCurrentBlockNumber) {
                    lastNumber = peerCurrentBlockNumber;
                }

                if (firstNumber <= peerCurrentBlockNumber) {
                    Collection<String> missingBlocks = bulkIndex
                            ? indexBlocksUsingBulk(peer, currencyName, firstNumber, lastNumber, progressionModel, isLastCurrent)
                            : indexBlocksNoBulk(peer, currencyName, firstNumber, lastNumber, progressionModel, isLastCurrent);

                    // If some blocks are missing, try to get it using other peers
                    if (CollectionUtils.isNotEmpty(missingBlocks)) {
                        progressionModel.setTask(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.otherPeers.task", currencyName));
                        missingBlocks = indexMissingBlocksFromOtherPeers(peer, peerCurrentBlock, missingBlocks, 1);
                    }

                    if (CollectionUtils.isEmpty(missingBlocks)) {
                        logger.info(I18n.t("duniter4j.blockIndexerService.indexBlocksRange.succeed", currencyName, peer, firstNumber, lastNumber, (System.currentTimeMillis() - timeStart)));
                        progressionModel.setStatus(ProgressionModel.Status.SUCCESS);
                    }
                    else {
                        logger.warn(String.format("[%s] [%s] Could not indexed some blocks from range [%s-%s]. Missing %s blocks.", currencyName, peer, firstNumber, lastNumber, missingBlocks.size()));
                        progressionModel.setStatus(ProgressionModel.Status.FAILED);
                    }
                }
                else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("[%s] [%s] Invalid block range [%s-%s]. Current block number is #%s", currencyName, peer, firstNumber, lastNumber, peerCurrentBlockNumber));
                    }
                    progressionModel.setStatus(ProgressionModel.Status.SUCCESS);
                }
            }
        } catch(Exception e) {
            logger.error("Error during indexBlocksRange: " + e.getMessage(), e);
            progressionModel.setStatus(ProgressionModel.Status.FAILED);
        }

        return this;
    }

    /**
     * Create or update a block, depending on its existence and hash
     * @param block
     * @param updateWhenSameHash if true, always update an existing block. If false, update only if hash has changed.
     * @param wait wait indexBlocksFromNode end
     * @throws DuplicateIndexIdException
     */
    public void saveBlock(BlockchainBlock block, boolean updateWhenSameHash, boolean wait) throws DuplicateIndexIdException {
        Preconditions.checkNotNull(block, "block could not be null") ;
        Preconditions.checkNotNull(block.getCurrency(), "block attribute 'blockchain' could not be null");
        Preconditions.checkNotNull(block.getNumber(), "block attribute 'number' could not be null");
        Preconditions.checkNotNull(block.getHash(), "block attribute 'hash' could not be null");

        BlockchainBlock existingBlock = blockDao.getBlockById(block.getCurrency(), getBlockId(block.getNumber()));

        // Currency not exists, or has changed, so create it
        if (existingBlock == null) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Insert new block [%s]", block.getNumber()));
            }

            // Create new block
            blockDao.create(block, wait);
        }

        // Exists, so check the owner signature
        else {
            boolean doUpdate;
            if (updateWhenSameHash) {
                doUpdate = true;
                if (logger.isTraceEnabled() && doUpdate) {
                    logger.trace(String.format("Update block [%s]", block.getNumber()));
                }
            }
            else {
                doUpdate = !StringUtils.equals(existingBlock.getHash(), block.getHash());
                if (logger.isTraceEnabled()) {
                    if (doUpdate) {
                        logger.trace(String.format("Update block [%s]: hash has been changed, old=[%s] new=[%s]", block.getNumber(), existingBlock.getHash(), block.getHash()));
                    }
                    else {
                        logger.trace(String.format("Skipping update block [%s]: hash is up to date.", block.getNumber()));
                    }
                }
            }

            // Update existing block
            if (doUpdate) {
                blockDao.update(block, wait);
            }
        }
    }

    /**
     * Index the given block, as the last (current) block. This will check is a fork has occur, and apply a rollback so.
     * @param peer a source peer
     * @param json block as json
     */
    public BlockchainService indexLastBlockFromJson(Peer peer, String json) {
        Preconditions.checkNotNull(json);
        Preconditions.checkArgument(json.length() > 0);

        indexBlockFromJson(peer, json, true /*is current*/, true/*check fork*/, true/*wait*/);

        return this;
    }

    /**
     *
     * @param json block as json
     * @param wait need to wait until processed ?
     */
    public BlockchainService indexBlockFromJson(Peer peer, String json, boolean isCurrent, boolean detectFork, boolean wait) {
        Preconditions.checkNotNull(json);
        Preconditions.checkArgument(json.length() > 0);

        String currencyName = blockCurrencyParser.getValue(json);
        Integer number = blockNumberParser.getValue(json);
        String hash = blockHashParser.getValue(json);

        Preconditions.checkNotNull(number);
        logger.info(I18n.t("duniter4j.blockIndexerService.indexBlock", currencyName, peer, number, hash));
        if (logger.isTraceEnabled()) {
            logger.trace(json);
        }

        // Detecting fork and rollback is necessary
        if (detectFork) {
            String previousHash = blockPreviousHashParser.getValue(json);
            boolean resolved = detectAndResolveFork(peer, currencyName, previousHash, number - 1);
            if (!resolved) {
                // Bad blockchain ! Skipping block indexation
                logger.error(I18n.t("duniter4j.blockIndexerService.detectFork.invalidBlockchain", currencyName, peer, number, hash));
                return this;
            }
        }

        // Workaround for https://github.com/duniter/duniter/issues/1042
        if (json.contains("[object Object]")) {
            // Getting block using GET request '/blochain/block'
            json = blockchainRemoteService.getBlockAsJson(peer, number);
        }

        // Index new block
        blockDao.create(currencyName, getBlockId(number), json.getBytes(), wait);

        // Update current
        if (isCurrent) {
            indexCurrentBlockFromJson(currencyName, json, true /*wait*/);
        }

        return this;
    }

    /**
     *
     * @param currentBlock
     */
    public void indexCurrentBlock(BlockchainBlock currentBlock, boolean wait) {
        Preconditions.checkNotNull(currentBlock);
        Preconditions.checkArgument(StringUtils.isNotBlank(currentBlock.getCurrency()));
        Preconditions.checkNotNull(currentBlock.getHash());
        Preconditions.checkNotNull(currentBlock.getNumber());

        // Serialize into JSON
        // WARN: must use GSON, to have same JSON result (e.g identities and joiners field must be converted into String)
        try {
            String json = getObjectMapper().writeValueAsString(currentBlock);
            indexCurrentBlockFromJson(currentBlock.getCurrency(), json, wait);
        } catch(IOException e) {
            throw new TechnicalException(e);
        }
    }

   /**
    *
    * @param currencyName
    * @param json block as JSON
    * @pram wait need to wait until block processed ?
    */
    public void indexCurrentBlockFromJson(final String currencyName, final String json, final boolean wait) {
        Preconditions.checkNotNull(json);
        Preconditions.checkArgument(json.length() > 0);
        Preconditions.checkArgument(StringUtils.isNotBlank(currencyName));

        // Preparing indexBlocksFromNode
        if (blockDao.isExists(currencyName, CURRENT_BLOCK_ID)) {
            blockDao.update(currencyName, CURRENT_BLOCK_ID, json.getBytes(), wait);
        }
        else {
            blockDao.create(currencyName, CURRENT_BLOCK_ID, json.getBytes(), wait);
        }
    }

    public BlockchainBlock getBlockById(final String currencyName, final int number) {
        return blockDao.getBlockById(currencyName, String.valueOf(number));
    }

    public BlockchainBlock getCurrentBlock(final String currencyName) {
        return blockDao.getBlockById(currencyName, CURRENT_BLOCK_ID);
    }

    public void deleteFrom(final String currencyName, final int fromBlock) {
        int maxBlock = blockDao.getMaxBlockNumber(currencyName);

        blockDao.deleteRange(currencyName, fromBlock, maxBlock);

        // Delete current also
        blockDao.deleteById(currencyName, CURRENT_BLOCK_ID);

    }


    public void deleteRange(final String currencyName, final int fromBlock, int toBlock) {
        int maxBlock = blockDao.getMaxBlockNumber(currencyName);

        boolean isLastBlock = toBlock >= maxBlock;

        blockDao.deleteRange(currencyName, fromBlock, (isLastBlock ? maxBlock : toBlock));

        // Delete current also, if last block
        if (isLastBlock) {
            blockDao.deleteById(currencyName, CURRENT_BLOCK_ID);
        }

    }

    /* -- Internal methods -- */

    private Collection<String> indexBlocksNoBulk(Peer peer, String currencyName, int firstNumber, int lastNumber, ProgressionModel progressionModel, boolean isLastCurrent) {
        Set<String> missingBlockNumbers = new LinkedHashSet<>();

        for (int curNumber = firstNumber; curNumber <= lastNumber; curNumber++) {
            if (curNumber != 0 && curNumber % 1000 == 0) {

                // Check is stopped
                if (progressionModel.isCancel()) {
                    progressionModel.setStatus(ProgressionModel.Status.STOPPED);
                    if (logger.isInfoEnabled()) {
                        logger.info(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.stopped", peer));
                    }
                    return missingBlockNumbers;
                }

                // Report progress
                reportIndexBlocksProgress(progressionModel, currencyName, peer, firstNumber, lastNumber, curNumber);
            }

            try {
                String blockAsJson = blockchainRemoteService.getBlockAsJson(peer, curNumber);
                blockDao.create(currencyName, getBlockId(curNumber), blockAsJson.getBytes(), true /*wait*/);

                // If last block
                if (isLastCurrent && curNumber == lastNumber - 1) {
                    // update the current block
                    indexCurrentBlockFromJson(currencyName, blockAsJson, true /*wait*/);
                }
            }
            catch(Throwable t) {
                logger.debug(String.format("Error while getting block #%s: %s. Skipping this block.", curNumber, t.getMessage()));
                missingBlockNumbers.add(String.valueOf(curNumber));
            }
        }

        return missingBlockNumbers;
    }

    private Collection<String> indexBlocksUsingBulk(Peer peer, String currencyName, int firstNumber, int lastNumber, ProgressionModel progressionModel,
                                                    boolean isLastCurrentNumber) {
        Set<String> missingBlockNumbers = new LinkedHashSet<>();

        boolean debug = logger.isDebugEnabled();

        int batchSize = pluginSettings.getIndexBulkSize();
        String currentBlockJson = null;

        for (int batchFirstNumber = firstNumber; batchFirstNumber < lastNumber; ) {
            // Check if stop (e.g. ask by user)
            if (progressionModel.isCancel()) {
                progressionModel.setStatus(ProgressionModel.Status.STOPPED);
                if (logger.isInfoEnabled()) {
                    logger.info(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.stopped", currencyName, peer.getUrl()));
                }
                return missingBlockNumbers;
            }

            String[] blocksAsJson = null;
            try {
                final int batchFirstNumberFinal = batchFirstNumber;
                blocksAsJson = executeWithRetry(()->blockchainRemoteService.getBlocksAsJson(peer, batchSize, batchFirstNumberFinal));
            } catch(TechnicalException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("[%s] [%s] Error while getting blocks from #%s (count=%s): %s. Skipping blocks.",currencyName, peer, batchFirstNumber, batchSize, e.getMessage()));
                }
            }

            // Peer send no blocks
            if (CollectionUtils.isEmpty(blocksAsJson)) {

                // Add range to missing blocks
                missingBlockNumbers.add(batchFirstNumber + "-" + (batchFirstNumber+batchSize));

                // Update counter
                batchFirstNumber += batchSize;
            }

            // Process received blocks
            else {

                List<Integer> processedBlockNumbers = Lists.newArrayList();
                BulkRequestBuilder bulkRequest = client.prepareBulk();
                for (String blockAsJson : blocksAsJson) {
                    Integer itemNumber = blockNumberParser.getValue(blockAsJson);

                    // update curNumber with max number;
                    if (itemNumber > batchFirstNumber) {
                        batchFirstNumber = itemNumber;
                    }

                    if (itemNumber != null && !processedBlockNumbers.contains(itemNumber)) {
                        // Add to bulk
                        bulkRequest.add(client.prepareIndex(currencyName, BLOCK_TYPE, itemNumber.toString())
                                .setRefresh(false) // recommended for heavy indexing
                                .setSource(blockAsJson)
                        );
                        processedBlockNumbers.add(itemNumber);
                    }

                    // If last block : also update the current block
                    if (isLastCurrentNumber && itemNumber == lastNumber) {
                        currentBlockJson = blockAsJson;
                    }
                }

                if (bulkRequest.numberOfActions() > 0) {

                    // Flush the bulk if not empty
                    BulkResponse bulkResponse = bulkRequest.get();

                    // If failures, continue but save missing blocks
                    if (bulkResponse.hasFailures()) {
                        // process failures by iterating through each bulk response item
                        for (BulkItemResponse itemResponse : bulkResponse) {
                            boolean skip = !itemResponse.isFailed()
                                    || Objects.equal(CURRENT_BLOCK_ID, itemResponse.getId())
                                    || missingBlockNumbers.contains(Integer.parseInt(itemResponse.getId()));
                            if (!skip) {
                                int itemNumber = Integer.parseInt(itemResponse.getId());
                                if (debug) {
                                    logger.debug(String.format("Error while getting block #%s: %s. Skipping this block.", itemNumber, itemResponse.getFailureMessage()));
                                }
                                missingBlockNumbers.add(itemResponse.getId());
                            }
                        }
                    }
                }
            }

            // Report progress
            reportIndexBlocksProgress(progressionModel, currencyName, peer, firstNumber, lastNumber, batchFirstNumber);
            batchFirstNumber++; // increment for next loop
        }

        if (StringUtils.isNotBlank(currentBlockJson)) {
            indexCurrentBlockFromJson(currencyName, currentBlockJson, true);
        }

        return missingBlockNumbers;
    }

    /**
     * Get blocks from other peers.
     * WARNING: given list must be ordered (with ascending order)
     * @param peer
     * @param currentBlock
     * @param sortedMissingBlocks
     * @param tryCounter
     */
    private Collection<String> indexMissingBlocksFromOtherPeers(Peer peer, BlockchainBlock currentBlock, Collection<String> sortedMissingBlocks, int tryCounter) {
        Preconditions.checkNotNull(peer);
        Preconditions.checkNotNull(currentBlock);
        Preconditions.checkNotNull(currentBlock.getHash());
        Preconditions.checkNotNull(currentBlock.getNumber());
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(sortedMissingBlocks));
        Preconditions.checkArgument(tryCounter >= 1);

        NetworkRemoteService networkRemoteService = ServiceLocator.instance().getNetworkRemoteService();
        BlockchainRemoteService blockchainRemoteService = ServiceLocator.instance().getBlockchainRemoteService();
        String currencyName = currentBlock.getCurrency();
        boolean debug = logger.isDebugEnabled();

        Set<String> newMissingBlocks = new LinkedHashSet<>();
        newMissingBlocks.addAll(sortedMissingBlocks);

        if (debug) {
            logger.debug(String.format("Missing blocks are: %s", newMissingBlocks.toString()));
        }

        // Select other peers, in filtering on the same blockchain version

        // TODO : a activer quand les peers seront bien mis à jour (UP/DOWN, block, hash...)
        //List<Peer> otherPeers = networkRemoteService.findPeers(peer, "UP", EndpointApi.BASIC_MERKLED_API,
        //        currentBlock.getNumber(), currentBlock.getHash());
        List<Peer> otherPeers = networkRemoteService.findPeers(peer, null, EndpointApi.BASIC_MERKLED_API,
                null, null);

        for(Peer childPeer: otherPeers) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("[%s] Trying to get missing blocks from other peer [%s]...", currencyName, childPeer));
            }
            try {
                for(String blockNumberStr: ImmutableSet.copyOf(sortedMissingBlocks)) {

                    boolean isBlockRange = blockNumberStr.indexOf('-') != -1;

                    // Get using bulk
                    if (isBlockRange) {
                        String[] rangeParts = blockNumberStr.split("-");
                        int firstNumber = Integer.parseInt(rangeParts[0]);
                        int lastNumber = Integer.parseInt(rangeParts[1]);

                        // Remove current blocks range
                        newMissingBlocks.remove(blockNumberStr);

                        Collection<String> bulkMissingBlocks = indexBlocksUsingBulk(childPeer, currencyName, firstNumber, lastNumber, new ProgressionModelImpl(), true);

                        // Re add if new missing blocks
                        if (CollectionUtils.isNotEmpty(bulkMissingBlocks)) {
                            newMissingBlocks.addAll(bulkMissingBlocks);
                        }
                    }

                    // Get blocks one by one
                    else {
                        int blockNumber = Integer.parseInt(blockNumberStr);
                        String blockAsJson = blockchainRemoteService.getBlockAsJson(childPeer, blockNumber);
                        if (StringUtils.isNotBlank(blockAsJson)) {
                            if (debug) {
                                logger.debug(String.format("Found missing block #%s on peer [%s].", blockNumber, childPeer));
                            }

                            // Index the missing block
                            blockDao.create(currencyName, getBlockId(blockNumber), blockAsJson.getBytes(), true/*wait*/);

                            // Remove this block number from the final missing list
                            newMissingBlocks.remove(blockNumber);
                        }
                    }
                }

                if (CollectionUtils.isEmpty(newMissingBlocks)) {
                    break;
                }

                // Update the list, for the next iteration
                sortedMissingBlocks =  newMissingBlocks;
            }
            catch(TechnicalException e) {
                if (debug) {
                    logger.debug(String.format("Error while getting blocks from peer [%s]: %s. Skipping this peer.", childPeer), e.getMessage());
                }

                continue; // skip this peer
            }
        }


        if (CollectionUtils.isEmpty(newMissingBlocks)) {
            return null;
        }

        tryCounter++;
        if (tryCounter >= SYNC_MISSING_BLOCK_MAX_RETRY) {
            // Max retry : stop here
            logger.error("Some blocks are still missing, after %s try: %s", SYNC_MISSING_BLOCK_MAX_RETRY, newMissingBlocks.toArray(new String[0]));
            return newMissingBlocks;
        }

        if (debug) {
            logger.debug("Some blocks are still missing: %s. Will retry later (%s/%s)...", newMissingBlocks.toArray(new String[0]), tryCounter, SYNC_MISSING_BLOCK_MAX_RETRY);
        }
        try {
            Thread.sleep(60 *1000); // wait 1 min

        }
        catch (InterruptedException e) {
            return null; // stop here
        }

        // retrying, with the new new blockchain
        BlockchainBlock newCurrentBlock =  blockchainRemoteService.getCurrentBlock(peer);
        return indexMissingBlocksFromOtherPeers(peer, newCurrentBlock, newMissingBlocks, tryCounter);
    }

    private void reportIndexBlocksProgress(ProgressionModel progressionModel, String currencyName, Peer peer, int firstNumber, int lastNumber, int curNumber) {
        int pct = (curNumber - firstNumber) * 100 / (lastNumber - firstNumber);
        progressionModel.setCurrent(pct);

        progressionModel.setMessage(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.progress", currencyName, peer, curNumber, lastNumber, pct));
        if (logger.isInfoEnabled()) {
            logger.info(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.progress", currencyName, peer, curNumber, lastNumber, pct));
        }

    }

    private boolean isBlockIndexed(String currencyName, int number, String hash) {
        Preconditions.checkNotNull(currencyName);
        Preconditions.checkNotNull(hash);
        // Check if previous block exists
        BlockchainBlock block = getBlockById(currencyName, number);
        boolean blockExists = block != null;
        if (!blockExists) {
            return blockExists;
        }
        return ObjectUtils.equals(block.getHash(), hash);
    }

    private boolean detectAndResolveFork(Peer peer, final String currencyName, final String hash, final int number){
        int forkResyncWindow = pluginSettings.getNodeForkResyncWindow();
        String forkOriginHash = hash;
        int forkOriginNumber = number;
        boolean sameBlockIndexed = isBlockIndexed(currencyName, forkOriginNumber, forkOriginHash);
        while (!sameBlockIndexed && forkOriginNumber > 0) {

            if (!sameBlockIndexed && logger.isInfoEnabled()) {
                logger.info(I18n.t("duniter4j.blockIndexerService.detectFork.invalidBlock", currencyName, peer, forkOriginNumber, forkOriginHash));
            }
            forkOriginNumber -= forkResyncWindow;
            if (forkOriginNumber < 0) {
                forkOriginNumber = 0;
            }

            // Get remote block (with auto-retry)
            try {
                final int currentNumberFinal = forkOriginNumber;
                String testBlock = executeWithRetry(() ->
                    blockchainRemoteService.getBlockAsJson(peer, currentNumberFinal));
                forkOriginHash = blockHashParser.getValue(testBlock);

                // Check is exists on ES index
                sameBlockIndexed = isBlockIndexed(currencyName, forkOriginNumber, forkOriginHash);
            } catch (TechnicalException e) {
                logger.warn(I18n.t("duniter4j.blockIndexerService.detectFork.remoteBlockNotFound", currencyName, peer, forkOriginNumber, e.getMessage()));
                sameBlockIndexed = false; // continue (go back again)
            }
        }

        if (!sameBlockIndexed) {
            return false; // sync could not be done (bad blockchain: no common blocks !)
        }

        if (forkOriginNumber < number) {
            logger.info(I18n.t("duniter4j.blockIndexerService.detectFork.resync", currencyName, peer, forkOriginNumber));
            // Remove some previous block
            blockDao.deleteRange(currencyName, forkOriginNumber/*from*/, number+forkResyncWindow/*to*/);

            // Re-indexing blocks
            indexBlocksUsingBulk(peer, currencyName, forkOriginNumber/*from*/, number, nullProgressionModel, true);
        }

        return true; // sync OK
    }


    private String getBlockId(int number) {
        return number == -1 ? CURRENT_BLOCK_ID : String.valueOf(number);
    }
}
