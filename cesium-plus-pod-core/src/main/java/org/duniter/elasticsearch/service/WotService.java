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
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableInt;
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.model.bma.BlockchainParameters;
import org.duniter.core.client.model.bma.WotRequirements;
import org.duniter.core.client.model.local.Member;
import org.duniter.core.client.service.bma.WotRemoteService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.LockManager;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.dao.BlockDao;
import org.duniter.elasticsearch.dao.CurrencyExtendDao;
import org.duniter.elasticsearch.dao.MemberDao;
import org.duniter.elasticsearch.dao.PendingMembershipDao;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benoit on 30/03/2015.
 */
public class WotService extends AbstractService {

    private static final String LOCK_NAME_COMPUTE_MEMBERS = "Index WoT members";


    private BlockDao blockDao;
    private MemberDao memberDao;
    private CurrencyExtendDao currencyDao;
    private PendingMembershipDao pendingMembershipDao;
    private WotRemoteService wotRemoteService;
    private BlockchainService blockchainService;
    private ThreadPool threadPool;
    private final LockManager lockManager = new LockManager(1, 4);
    private final Map<String, ChangeService.ChangeListener> currentBlockListeners = Maps.newConcurrentMap();

    private final Map<String, Boolean> isBlockchainIndexationReady = Maps.newConcurrentMap();

    @Inject
    public WotService(Duniter4jClient client,
                      PluginSettings settings,
                      ThreadPool threadPool,
                      BlockDao blockDao,
                      MemberDao memberDao,
                      CurrencyDao currencyDao,
                      PendingMembershipDao pendingMembershipDao,
                      BlockchainService blockchainService,
                      final ServiceLocator serviceLocator){
        super("duniter.wot", client, settings);
        this.client = client;
        this.blockDao = blockDao;
        this.memberDao = memberDao;
        this.currencyDao = (CurrencyExtendDao) currencyDao;
        this.pendingMembershipDao = pendingMembershipDao;
        this.blockchainService = blockchainService;
        this.threadPool = threadPool;
        this.threadPool.scheduleOnStarted(() -> {
            wotRemoteService = serviceLocator.getWotRemoteService();
            setIsReady(true);
        });
    }

    public List<Member> getMembers(String currency) {

        final String currencyId = safeGetCurrency(currency);

        // Blockchain indexation is enable: use it!
        if (isBlockchainReady(currencyId)) {

            List<Member> members = memberDao.getMembers(currencyId);

            // No members, or not indexed yet ?
            if (CollectionUtils.isEmpty(members)) {
                logger.warn("No member found. Trying to index members...");
                return indexAndGetMembers(currencyId);
            }

            return members;
        }

        // Else, fallback to the Duniter node
        else {
            return wotRemoteService.getMembers(currencyId);
        }
    }

    public void save(String currencyId, final List<Member> members) {
        // skip if nothing to save
        if (CollectionUtils.isEmpty(members)) return;

        memberDao.save(currencyId, members);
    }

    public Member save(final Member member) {
        Preconditions.checkNotNull(member);
        Preconditions.checkNotNull(member.getCurrency());
        Preconditions.checkNotNull(member.getPubkey());
        Preconditions.checkNotNull(member.getUid());

        boolean exists = memberDao.isExists(member.getCurrency(), member.getPubkey());

        // Create
        if (!exists) {
            memberDao.create(member);
        }

        // or update
        else {
            memberDao.update(member);
        }
        return member;
    }

    public WotService indexMembers(final String currency) {
        indexAndGetMembers(currency);
        return this;
    }

    public WotService stopListenAndIndexMembers(final String currency) {
        ChangeService.ChangeListener listener = currentBlockListeners.remove(currency);
        if (listener != null) {
            ChangeService.unregisterListener(listener);
        }
        return this;
    }

    public Closeable listenAndIndexMembers(final String currency) {
        // Stop if previous listener was existing
        stopListenAndIndexMembers(currency);

        // Listen changes on block
        ChangeService.ChangeListener listener =  ChangeService.registerListener(new ChangeService.ChangeListener() {
            @Override
            public String getId() {
                return "duniter.wot";
            }
            @Override
            public Collection<ChangeSource> getChangeSources() {
                return ImmutableList.of(new ChangeSource(currency, BlockDao.TYPE, "current"));
            }
            @Override
            public void onChange(ChangeEvent change) {
                // If current block indexed
                switch (change.getOperation()) {
                    case CREATE:
                    case INDEX:
                        logger.debug(String.format("[%s] Scheduling indexation of WoT members", currency));
                        threadPool.schedule(() -> {
                            try {
                                // Acquire lock (once members indexation at a time)
                                if (lockManager.tryLock(LOCK_NAME_COMPUTE_MEMBERS, 10, TimeUnit.SECONDS)) {
                                    try {
                                        indexMembers(currency);
                                    }
                                    catch (Exception e) {
                                        logger.error("Error while indexing WoT members: " + e.getMessage(), e);
                                    }
                                    finally {
                                        // Release the lock
                                        lockManager.unlock(LOCK_NAME_COMPUTE_MEMBERS);
                                    }
                                }
                                else {
                                    logger.debug("Could not acquire lock for indexing members. Skipping.");
                                }
                            } catch (InterruptedException e) {
                                logger.warn("Stopping indexation of WoT members: " + e.getMessage());
                            }
                        }, 30, TimeUnit.SECONDS);
                        break;
                    default:
                        // Skip deletion
                        break;
                }

            }
        });

        this.currentBlockListeners.put(currency, listener);

        // Return the tear down logic
        return () -> this.stopListenAndIndexMembers(currency);
    }

    public boolean isOrWasMember(String pubkey) {

        Set<String> currencyIds =  currencyDao.getAllIds();
        if (CollectionUtils.isEmpty(currencyIds)) return false;

        SearchResponse response = client.prepareSearch()
                .setIndices(currencyIds.toArray(new String[currencyIds.size()]))
                .setSize(0) // only need the total
                .setTypes(MemberDao.TYPE)
                .setQuery(QueryBuilders.idsQuery().ids(pubkey))
                .setRequestCache(true)
                .execute().actionGet();

        return response.getHits() != null && response.getHits().getTotalHits() > 0;
    }

    public boolean isMember(String pubkey) {

        Set<String> currencyIds =  currencyDao.getAllIds();
        if (CollectionUtils.isEmpty(currencyIds)) return false;

        QueryBuilder query = QueryBuilders.constantScoreQuery(QueryBuilders.boolQuery()
                .filter(QueryBuilders.idsQuery().addIds(pubkey))
                .filter(QueryBuilders.termQuery(Member.PROPERTY_IS_MEMBER, true))
        );

        SearchResponse response = client.prepareSearch()
                .setIndices(currencyIds.toArray(new String[currencyIds.size()]))
                .setSize(0) // only need the total
                .setTypes(MemberDao.TYPE)
                .setQuery(query)
                .setRequestCache(true)
                .execute().actionGet();

        return response.getHits() != null && response.getHits().getTotalHits() > 0;
    }

    public List<WotRequirements> getRequirements(String currency, String pubkey) {
        waitReady();

        final String currencyId = safeGetCurrency(currency);

        return this.wotRemoteService.getRequirements(currencyId, pubkey);
    }

    /* -- protected methods -- */

    protected List<Member> indexAndGetMembers(final String currencyId) {

        logger.info(String.format("[%s] Indexing WoT members...", currencyId));

        final BlockchainParameters parameters = blockchainService.getParameters(currencyId);

        // Retrieve previous members pubkeys. This list will be reduce later, to keep only excluded members
        final Set<String> pubkeysToExclude = memberDao.getMemberPubkeys(currencyId);
        final long previousMembersCount = CollectionUtils.size(pubkeysToExclude);

        final List<Member> members = blockDao.getMembers(parameters);

        // Save members into index
        final MutableInt becomesCount = new MutableInt(0);
        if (CollectionUtils.isNotEmpty(members)) {
            // Set currency
            members.forEach(m -> {
                // Remove from the list
                boolean becomeMember = !pubkeysToExclude.remove(m.getPubkey());
                // If not found in the previous list = new member
                if(becomeMember) becomesCount.increment();
                m.setCurrency(currencyId);
            });
        }

        int excludedCount = CollectionUtils.size(pubkeysToExclude);
        long deltaCount = CollectionUtils.size(members) - previousMembersCount;
        boolean hasBecomes = becomesCount.getValue() > 0;
        boolean hasExcluded = excludedCount > 0;
        boolean hasChanges = deltaCount != 0 || hasBecomes || hasExcluded;

        // Has changes
        if (hasChanges) {

            // Save members
            if (hasBecomes) {
                memberDao.save(currencyId, members);
            }

            // Update old members as "was member"
            if (hasExcluded) {
                memberDao.updateAsWasMember(currencyId, pubkeysToExclude);
            }

            // Update currency member count
            if (deltaCount != 0) {
                currencyDao.updateMemberCount(currencyId, members.size());
            }

            logger.info(String.format("[%s] Indexing WoT members [OK] - %s members (%s%s), %s becomes, %s excluded", currencyId,
                    CollectionUtils.size(members),
                    (deltaCount > 0) ? "\u21D1" : "\u21D3",
                    Math.abs(deltaCount),
                    becomesCount.getValue(),
                    excludedCount));
        }

        // No changes: just log
        else {
            logger.info(String.format("[%s] Indexing WoT members [OK] - %s members (unchanged)", currencyId, CollectionUtils.size(members)));
        }

        return members;
    }

    /**
     * Return the given currency, or the default currency
     * @param currency
     * @return
     */
    protected String safeGetCurrency(String currency) {
        if (StringUtils.isNotBlank(currency)) return currency;
        return currencyDao.getDefaultId();
    }

    protected boolean isBlockchainReady(String currency) {
        if (!this.isReady()) return false;

        Boolean isReady = isBlockchainIndexationReady.get(currency);
        if (isReady != null) return isReady.booleanValue();

        // Blockchain indexation was disable in settings
        if (!pluginSettings.enableBlockchainIndexation()) {
            isBlockchainIndexationReady.put(currency, Boolean.FALSE);
            return false;
        }

        // Check if there is a current block
        else {
            try {
                boolean hasCurrentBlock = blockchainService.getCurrentBlock(currency) != null;
                if (hasCurrentBlock) {

                    // OK. Remember that indexation is ready
                    isBlockchainIndexationReady.put(currency, Boolean.TRUE);
                    return true;
                }
            }
            catch(Throwable t) {
            }

            // No current block => indexation is still processing
            // do NOT set the map, to force new check later
            return false;
        }
    }
}
