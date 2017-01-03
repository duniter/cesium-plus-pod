package org.duniter.elasticsearch.service;

/*
 * #%L
 * UCoin Java Client :: Core API
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
import com.google.gson.Gson;
import org.duniter.core.client.model.bma.BlockchainBlock;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.bma.EndpointProtocol;
import org.duniter.core.client.model.bma.gson.GsonUtils;
import org.duniter.core.client.model.bma.gson.JsonAttributeParser;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.service.bma.BlockchainRemoteService;
import org.duniter.core.client.service.bma.NetworkRemoteService;
import org.duniter.core.client.service.exception.BlockNotFoundException;
import org.duniter.core.client.service.exception.JsonSyntaxException;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.model.NullProgressionModel;
import org.duniter.core.model.ProgressionModel;
import org.duniter.core.model.ProgressionModelImpl;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.StringUtils;
import org.duniter.core.util.websocket.WebsocketClientEndpoint;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.exception.DuplicateIndexIdException;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.*;

/**
 * Created by Benoit on 30/03/2015.
 */
public class BlockchainService extends AbstractService {

    public static final String BLOCK_TYPE = "block";
    public static final String CURRENT_BLOCK_ID = "current";

    private static final int SYNC_MISSING_BLOCK_MAX_RETRY = 5;

    private final ProgressionModel nullProgressionModel = new NullProgressionModel();

    private BlockchainRemoteService blockchainRemoteService;
    private CurrencyService currencyService;
    private ThreadPool threadPool;
    private List<WebsocketClientEndpoint.ConnectionListener> connectionListeners = new ArrayList<>();
    private final WebsocketClientEndpoint.ConnectionListener dispatchConnectionListener;

    private final JsonAttributeParser blockNumberParser = new JsonAttributeParser("number");
    private final JsonAttributeParser blockCurrencyParser = new JsonAttributeParser("currency");
    private final JsonAttributeParser blockHashParser = new JsonAttributeParser("hash");
    private final JsonAttributeParser blockPreviousHashParser = new JsonAttributeParser("previousHash");

    private Gson gson;

    @Inject
    public BlockchainService(Client client, PluginSettings settings, ThreadPool threadPool,
                             final ServiceLocator serviceLocator){
        super("duniter.blockchain", client, settings);
        this.gson = GsonUtils.newBuilder().create();
        this.threadPool = threadPool;
        threadPool.scheduleOnStarted(() -> {
            blockchainRemoteService = serviceLocator.getBlockchainRemoteService();
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

    @Inject
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    public void registerConnectionListener(WebsocketClientEndpoint.ConnectionListener listener) {
        synchronized (connectionListeners) {
            connectionListeners.add(listener);
        }
    }

    public BlockchainService listenAndIndexNewBlock(final Peer peer){
        WebsocketClientEndpoint wsEndPoint = blockchainRemoteService.addBlockListener(peer, message -> indexLastBlockFromJson(peer, message));
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

            // Create index blockchain if need
            if (!currencyService.isCurrencyExists(currencyName)) {
                currencyService.indexCurrencyFromPeer(peer);
            }

            // Check if index exists
            createIndexIfNotExists(currencyName, true/*wait cluster health*/);

            // Then index all blocks
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
                    startNumber = getMaxBlockNumber(currencyName) + 1;
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
                            ? indexBlocksUsingBulk(peer, currencyName, startNumber, peerCurrentBlockNumber, progressionModel)
                            : indexBlocksNoBulk(peer, currencyName, startNumber, peerCurrentBlockNumber, progressionModel);

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
                        logger.warn(String.format("[%s] [%s] Could not indexed all blocks. Missing %s blocks.", currencyName, peer, missingBlocks.size()));
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
            logger.error("Error during indexBlocksFromNode: " + e.getMessage(), e);
            progressionModel.setStatus(ProgressionModel.Status.FAILED);
        }

        return this;
    }

    public BlockchainService deleteIndex(String currencyName) {
        deleteIndexIfExists(currencyName);
        return this;
    }

    public boolean existsIndex(String currencyName) {
        return super.existsIndex(currencyName);
    }

    public BlockchainService createIndexIfNotExists(String currencyName, boolean waitClusterHealth) {
        if (!existsIndex(currencyName)) {
            createIndex(currencyName);

            // when wait cluster state
            if (waitClusterHealth) {
                threadPool.waitClusterHealthStatus(ClusterHealthStatus.GREEN, ClusterHealthStatus.YELLOW);
            }
        }
        return this;
    }

    public void createIndex(String currencyName) {
        logger.info(String.format("Creating index [%s]", currencyName));

        CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(currencyName);
        org.elasticsearch.common.settings.Settings indexSettings = org.elasticsearch.common.settings.Settings.settingsBuilder()
                .put("number_of_shards", 1)
                .put("number_of_replicas", 1)
                //.put("analyzer", createDefaultAnalyzer())
                .build();
        createIndexRequestBuilder.setSettings(indexSettings);
        createIndexRequestBuilder.addMapping(BLOCK_TYPE, createBlockType());
        createIndexRequestBuilder.execute().actionGet();
    }

    public void createBlock(BlockchainBlock block) throws org.duniter.elasticsearch.exception.DuplicateIndexIdException {
        ObjectUtils.checkNotNull(block, "block could not be null") ;
        ObjectUtils.checkNotNull(block.getCurrency(), "block attribute 'blockchain' could not be null");
        ObjectUtils.checkNotNull(block.getNumber(), "block attribute 'number' could not be null");

        BlockchainBlock existingBlock = getBlockById(block.getCurrency(), block.getNumber());
        if (existingBlock != null) {
            throw new DuplicateIndexIdException(String.format("Block with number [%s] already exists.", block.getNumber()));
        }

        indexBlock(block, false);
    }

    /**
     * Create or update a block, depending on its existence and hash
     * @param block
     * @param updateWhenSameHash if true, always update an existing block. If false, update only if hash has changed.
     * @param wait wait indexBlocksFromNode end
     * @throws DuplicateIndexIdException
     */
    public void saveBlock(BlockchainBlock block, boolean updateWhenSameHash, boolean wait) throws DuplicateIndexIdException {
        ObjectUtils.checkNotNull(block, "block could not be null") ;
        ObjectUtils.checkNotNull(block.getCurrency(), "block attribute 'blockchain' could not be null");
        ObjectUtils.checkNotNull(block.getNumber(), "block attribute 'number' could not be null");
        ObjectUtils.checkNotNull(block.getHash(), "block attribute 'hash' could not be null");

        BlockchainBlock existingBlock = getBlockById(block.getCurrency(), block.getNumber());

        // Currency not exists, or has changed, so create it
        if (existingBlock == null) {
            if (logger.isTraceEnabled()) {
                logger.trace(String.format("Insert new block [%s]", block.getNumber()));
            }

            // Create new block
            indexBlock(block, wait);
        }

        // Exists, so check the owner signature
        else {
            boolean doUpdate = false;
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
                indexBlock(block, wait);
            }
        }
    }

    public void indexBlock(BlockchainBlock block, boolean wait) {
        ObjectUtils.checkNotNull(block);
        ObjectUtils.checkArgument(StringUtils.isNotBlank(block.getCurrency()));
        ObjectUtils.checkNotNull(block.getHash());
        ObjectUtils.checkNotNull(block.getNumber());

        // Serialize into JSON
        // WARN: must use GSON, to have same JSON result (e.g identities and joiners field must be converted into String)
        String json = gson.toJson(block);

        // Preparing indexBlocksFromNode
        IndexRequestBuilder indexRequest = client.prepareIndex(block.getCurrency(), BLOCK_TYPE)
                .setId(block.getNumber().toString())
                .setSource(json);

        // Execute indexBlocksFromNode
        ActionFuture<IndexResponse> futureResponse = indexRequest
                .setRefresh(true)
                .execute();

        if (wait) {
            futureResponse.actionGet();
        }
    }

    /**
     *
     * @param currencyName
     * @param number the block number
     * @param json block as JSON
     */
    public BlockchainService indexBlockFromJson(String currencyName, int number, byte[] json, boolean refresh, boolean wait) {
        ObjectUtils.checkNotNull(json);
        ObjectUtils.checkArgument(json.length > 0);

        // Preparing indexBlocksFromNode
        IndexRequestBuilder indexRequest = client.prepareIndex(currencyName, BLOCK_TYPE)
                .setId(String.valueOf(number))
                .setRefresh(refresh)
                .setSource(json);

        // Execute indexBlocksFromNode
        if (!wait) {
            indexRequest.execute();
        }
        else {
            indexRequest.execute().actionGet();
        }

        return this;
    }

    /**
     * Index the given block, as the last (current) block. This will check is a fork has occur, and apply a rollback so.
     * @param peer a source peer
     * @param json block as json
     */
    public BlockchainService indexLastBlockFromJson(Peer peer, String json) {
        ObjectUtils.checkNotNull(json);
        ObjectUtils.checkArgument(json.length() > 0);

        indexBlockFromJson(peer, json, true /*refresh*/, true /*is current*/, true/*check fork*/, true/*wait*/);

        return this;
    }

    /**
     *
     * @param json block as json
     * @param refresh Could be an existing block ?
     * @param wait need to wait until processed ?
     */
    public BlockchainService indexBlockFromJson(Peer peer, String json, boolean refresh, boolean isCurrent, boolean detectFork, boolean wait) {
        ObjectUtils.checkNotNull(json);
        ObjectUtils.checkArgument(json.length() > 0);

        String currencyName = blockCurrencyParser.getValueAsString(json);
        int number = blockNumberParser.getValueAsInt(json);
        String hash = blockHashParser.getValueAsString(json);

        logger.info(I18n.t("duniter4j.blockIndexerService.indexBlock", currencyName, peer, number, hash));
        if (logger.isTraceEnabled()) {
            logger.trace(json);
        }

        // Detecting fork and rollback is necessary
        if (detectFork) {
            String previousHash = blockPreviousHashParser.getValueAsString(json);
            boolean resolved = detectAndResolveFork(peer, currencyName, previousHash, number - 1);
            if (!resolved) {
                // Bad blockchain ! Skipping block indexation
                logger.error(I18n.t("duniter4j.blockIndexerService.detectFork.invalidBlockchain", currencyName, peer, number, hash));
                return this;
            }
        }

        // Preparing indexBlocksFromNode
        IndexRequestBuilder indexRequest = client.prepareIndex(currencyName, BLOCK_TYPE)
                .setId(String.valueOf(number))
                .setRefresh(refresh)
                .setSource(json);

        // Execute indexBlocksFromNode
        if (!wait) {
            indexRequest.execute();
        }
        else {
            indexRequest.execute().actionGet();
        }

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
        ObjectUtils.checkNotNull(currentBlock);
        ObjectUtils.checkArgument(StringUtils.isNotBlank(currentBlock.getCurrency()));
        ObjectUtils.checkNotNull(currentBlock.getHash());
        ObjectUtils.checkNotNull(currentBlock.getNumber());

        // Serialize into JSON
        // WARN: must use GSON, to have same JSON result (e.g identities and joiners field must be converted into String)
        String json = gson.toJson(currentBlock);

        indexCurrentBlockFromJson(currentBlock.getCurrency(), json, wait);
    }

   /**
    *
    * @param currencyName
    * @param json block as JSON
    * @pram wait need to wait until block processed ?
    */
    public void indexCurrentBlockFromJson(String currencyName, String json, boolean wait) {
        ObjectUtils.checkNotNull(json);
        ObjectUtils.checkArgument(json.length() > 0);
        ObjectUtils.checkArgument(StringUtils.isNotBlank(currencyName));

        // Preparing indexBlocksFromNode
        IndexRequestBuilder indexRequest = client.prepareIndex(currencyName, BLOCK_TYPE)
                .setId(CURRENT_BLOCK_ID)
                .setRefresh(true)
                .setSource(json);

        // Execute in a pool
        if (!wait) {
            boolean acceptedInPool = false;
            while(!acceptedInPool)
                try {
                    indexRequest.execute();
                    acceptedInPool = true;
                }
                catch(EsRejectedExecutionException e) {
                    // not accepted, so wait
                    try {
                        Thread.sleep(1000); // 1s
                    }
                    catch(InterruptedException e2) {
                        // silent
                    }
                }

        } else {
            indexRequest.execute().actionGet();
        }
    }

    public List<BlockchainBlock> findBlocksByHash(String currencyName, String query) {
        String[] queryParts = query.split("[\\t ]+");

        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(BLOCK_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // If only one term, search as prefix
        if (queryParts.length == 1) {
            searchRequest.setQuery(QueryBuilders.prefixQuery("hash", query));
        }

        // If more than a word, search on terms match
        else {
            searchRequest.setQuery(QueryBuilders.matchQuery("hash", query));
        }

        // Sort as score/memberCount
        searchRequest.addSort("_score", SortOrder.DESC)
                .addSort("number", SortOrder.DESC);

        // Highlight matched words
        searchRequest.setHighlighterTagsSchema("styled")
                .addHighlightedField("hash")
                .addFields("hash")
                .addFields("*", "_source");

        // Execute query
        SearchResponse searchResponse = searchRequest.execute().actionGet();

        // Read query result
        return toBlocks(searchResponse, true);
    }

    public int getMaxBlockNumber(String currencyName) {
        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(BLOCK_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // Get max(number)
        searchRequest.addAggregation(AggregationBuilders.max("max_number").field("number"));

        // Execute query
        SearchResponse searchResponse = searchRequest.execute().actionGet();

        // Read query result
        Max result = searchResponse.getAggregations().get("max_number");
        if (result == null) {
            return -1;
        }

        return (result.getValue() == Double.NEGATIVE_INFINITY)
                ? -1
                : (int)result.getValue();
    }

    public BlockchainBlock getBlockById(String currencyName, int number) {
        return getBlockByIdStr(currencyName, String.valueOf(number));
    }

    public BlockchainBlock getCurrentBlock(String currencyName) {
        return getBlockByIdStr(currencyName, CURRENT_BLOCK_ID);
    }

    /* -- Internal methods -- */


    public XContentBuilder createBlockType() {
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(BLOCK_TYPE)
                    .startObject("properties")

                    // block number
                    .startObject("number")
                    .field("type", "integer")
                    .endObject()

                    // hash
                    .startObject("hash")
                    .field("type", "string")
                    .endObject()

                    // previous hash
                    .startObject("previousHash")
                    .field("type", "string")
                    .endObject()

                    // membercount
                    .startObject("memberCount")
                    .field("type", "integer")
                    .endObject()

                    // membersChanges
                    .startObject("membersChanges")
                    .field("type", "string")
                    .endObject()

                    // membersChanges
                    .startObject("monetaryMass")
                    .field("type", "string")
                    .endObject()

                    // identities:
                    //.startObject("identities")
                    //.endObject()

                    .endObject()
                    .endObject().endObject();

            return mapping;
        }
        catch(IOException ioe) {
            throw new TechnicalException("Error while getting mapping for block index: " + ioe.getMessage(), ioe);
        }
    }

    public BlockchainBlock getBlockByIdStr(String currencyName, String blockId) {

        // Prepare request
        SearchRequestBuilder searchRequest = client
                .prepareSearch(currencyName)
                .setTypes(BLOCK_TYPE)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);

        // If more than a word, search on terms match
        searchRequest.setQuery(QueryBuilders.matchQuery("_id", blockId));

        // Execute query
        try {
            SearchResponse searchResponse = searchRequest.execute().actionGet();
            List<BlockchainBlock> blocks = toBlocks(searchResponse, false);
            if (CollectionUtils.isEmpty(blocks)) {
                return null;
            }

            // Return the unique result
            return CollectionUtils.extractSingleton(blocks);
        }
        catch(JsonSyntaxException e) {
            throw new TechnicalException(String.format("Error while getting indexed block #%s for blockchain [%s]", blockId, currencyName), e);
        }

    }

    protected List<BlockchainBlock> toBlocks(SearchResponse response, boolean withHighlight) {
        // Read query result
        List<BlockchainBlock> result = Lists.newArrayList();
        response.getHits().forEach(searchHit -> {
            BlockchainBlock block;
            if (searchHit.source() != null) {
                String jsonString = new String(searchHit.source());
                try {
                    block = GsonUtils.newBuilder().create().fromJson(jsonString, BlockchainBlock.class);
                } catch(Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Error while parsing block from JSON:\n" + jsonString);
                    }
                    throw new JsonSyntaxException("Error while read block from JSON: " + e.getMessage(), e);
                }
            }
            else {
                block = new BlockchainBlock();
                SearchHitField field = searchHit.getFields().get("hash");
                block.setHash(field.getValue());
            }
            result.add(block);

            // If possible, use highlights
            if (withHighlight) {
                Map<String, HighlightField> fields = searchHit.getHighlightFields();
                for (HighlightField field : fields.values()) {
                    String blockNameHighLight = field.getFragments()[0].string();
                    block.setHash(blockNameHighLight);
                }
            }
        });

        return result;
    }


    public Collection<String> indexBlocksNoBulk(Peer peer, String currencyName, int firstNumber, int lastNumber, ProgressionModel progressionModel) {
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
                indexBlockFromJson(currencyName, curNumber, blockAsJson.getBytes(), false, true /*wait*/);

                // If last block
                if (curNumber == lastNumber - 1) {
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

    public Collection<String> indexBlocksUsingBulk(Peer peer, String currencyName, int firstNumber, int lastNumber, ProgressionModel progressionModel) {
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
                    int itemNumber = blockNumberParser.getValueAsInt(blockAsJson);

                    // update curNumber with max number;
                    if (itemNumber > batchFirstNumber) {
                        batchFirstNumber = itemNumber;
                    }

                    if (!processedBlockNumbers.contains(itemNumber)) {
                        // Add to bulk
                        bulkRequest.add(client.prepareIndex(currencyName, BLOCK_TYPE, String.valueOf(itemNumber))
                                .setRefresh(false) // recommended for heavy indexing
                                .setSource(blockAsJson)
                        );
                        processedBlockNumbers.add(itemNumber);
                    }

                    // If last block : also update the current block
                    if (itemNumber == lastNumber) {
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
            indexCurrentBlockFromJson(currencyName, currentBlockJson, false);
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
    protected Collection<String> indexMissingBlocksFromOtherPeers(Peer peer, BlockchainBlock currentBlock, Collection<String> sortedMissingBlocks, int tryCounter) {
        ObjectUtils.checkNotNull(peer);
        ObjectUtils.checkNotNull(currentBlock);
        ObjectUtils.checkNotNull(currentBlock.getHash());
        ObjectUtils.checkNotNull(currentBlock.getNumber());
        ObjectUtils.checkArgument(CollectionUtils.isNotEmpty(sortedMissingBlocks));
        ObjectUtils.checkArgument(tryCounter >= 1);

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
        //List<Peer> otherPeers = networkRemoteService.findPeers(peer, "UP", EndpointProtocol.BASIC_MERKLED_API,
        //        currentBlock.getNumber(), currentBlock.getHash());
        List<Peer> otherPeers = networkRemoteService.findPeers(peer, null, EndpointProtocol.BASIC_MERKLED_API,
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

                        Collection<String> bulkMissingBlocks = indexBlocksUsingBulk(childPeer, currencyName, firstNumber, lastNumber, new ProgressionModelImpl());

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
                            indexBlockFromJson(currencyName, blockNumber, blockAsJson.getBytes(), false/*refresh*/, true/*wait*/);

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

    protected void reportIndexBlocksProgress(ProgressionModel progressionModel, String currencyName, Peer peer, int firstNumber, int lastNumber, int curNumber) {
        int pct = (curNumber - firstNumber) * 100 / (lastNumber - firstNumber);
        progressionModel.setCurrent(pct);

        progressionModel.setMessage(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.progress", currencyName, peer, curNumber, lastNumber, pct));
        if (logger.isInfoEnabled()) {
            logger.info(I18n.t("duniter4j.blockIndexerService.indexLastBlocks.progress", currencyName, peer, curNumber, lastNumber, pct));
        }

    }

    protected boolean isBlockIndexed(String currencyName, int number, String hash) {
        // Check if previous block exists
        BlockchainBlock block = getBlockByIdStr(currencyName, String.valueOf(number));
        boolean blockExists = block != null;
        if (!blockExists) {
            return blockExists;
        }
        return block.getHash() != null && block.getHash().equals(hash);
    }

    protected boolean detectAndResolveFork(Peer peer, final String currencyName, final String hash, final int number){
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
                forkOriginHash = blockHashParser.getValueAsString(testBlock);

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
            deleteBlocksFromNumber(currencyName, forkOriginNumber/*from*/, number+forkResyncWindow/*to*/);

            // Re-indexing blocks
            indexBlocksUsingBulk(peer, currencyName, forkOriginNumber/*from*/, number, nullProgressionModel);
        }

        return true; // sync OK
    }

    /**
     * Delete blocks from a start number (using bulk)
     * @param currencyName
     * @param fromNumber
     */
    protected void deleteBlocksFromNumber(final String currencyName, final int fromNumber, final int toNumber) {

        int bulkSize = pluginSettings.getIndexBulkSize();

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (int number=fromNumber; number<=toNumber; number++) {

            bulkRequest.add(
                client.prepareDelete(currencyName, BLOCK_TYPE, String.valueOf(number))
            );

            // Flush the bulk if not empty
            if ((fromNumber - number % bulkSize) == 0) {
                flushDeleteBulk(currencyName, BLOCK_TYPE, bulkRequest);
                bulkRequest = client.prepareBulk();
            }
        }

        // last flush
        flushDeleteBulk(currencyName, BLOCK_TYPE, bulkRequest);
    }


}
