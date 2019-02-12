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
import org.duniter.core.client.dao.CurrencyDao;
import org.duniter.core.client.model.bma.BlockchainParameters;
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
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeService;
import org.duniter.elasticsearch.service.changes.ChangeSource;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.common.inject.Inject;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Benoit on 30/03/2015.
 */
public class WotService extends AbstractService {

    private static final String LOCK_NAME_COMPUTE_MEMBERS = "Index WoT members";


    private BlockDao blockDao;
    private MemberDao memberDao;
    private CurrencyExtendDao currencyDao;
    private WotRemoteService wotRemoteService;
    private BlockchainService blockchainService;
    private ThreadPool threadPool;
    private final LockManager lockManager = new LockManager(4, 10);

    @Inject
    public WotService(Duniter4jClient client,
                      PluginSettings settings,
                      ThreadPool threadPool,
                      BlockDao blockDao,
                      MemberDao memberDao,
                      CurrencyDao currencyDao,
                      BlockchainService blockchainService,
                      final ServiceLocator serviceLocator){
        super("duniter.wot", client, settings);
        this.client = client;
        this.blockDao = blockDao;
        this.memberDao = memberDao;
        this.currencyDao = (CurrencyExtendDao) currencyDao;
        this.blockchainService = blockchainService;
        this.threadPool = threadPool;
        this.threadPool.scheduleOnStarted(() -> {
            wotRemoteService = serviceLocator.getWotRemoteService();
            setIsReady(true);
        });
    }

    public List<Member> getMembers(String currency) {

        final String currencyId = safeGetCurrency(currency);

        // Index is enable: use dao
        if (pluginSettings.enableBlockchainIndexation()) {

            List<Member> members = memberDao.getMembers(currencyId);

            // No members, or not indexed yet ?
            if (CollectionUtils.isEmpty(members)) {
                logger.warn("No member found. Trying to index members...");
                return indexAndGetMembers(currencyId);
            }

            return members;
        }
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

    public WotService listenAndIndexMembers(final String currency) {

        // Listen changes on block
        ChangeService.registerListener(new ChangeService.ChangeListener() {
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

        return this;
    }

    /* -- protected methods -- */

    protected List<Member> indexAndGetMembers(final String currency) {

        logger.info(String.format("[%s] Indexing WoT members...", currency));

        Set<String> wasMemberPubkeys = memberDao.getMemberPubkeys(currency);

        BlockchainParameters p = blockchainService.getParameters(currency);
        List<Member> members = blockDao.getMembers(p);

        // Save members into index
        if (CollectionUtils.isNotEmpty(members)) {
            // Set currency
            members.forEach(m -> {
                wasMemberPubkeys.remove(m.getPubkey());
                m.setCurrency(currency);
            });

            // Save members
            memberDao.save(currency, members);
        }

        // Update old members as "was member"
        if (CollectionUtils.isNotEmpty(wasMemberPubkeys)) {
            memberDao.updateAsWasMember(currency, wasMemberPubkeys);
        }

        // Update currency member count
        currencyDao.updateMemberCount(currency, members.size());

        logger.info(String.format("[%s] Indexing WoT members [OK]", currency));

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
}
