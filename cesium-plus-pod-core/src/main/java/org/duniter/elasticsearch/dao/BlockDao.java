package org.duniter.elasticsearch.dao;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Core plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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

import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.BlockchainBlock;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by blavenie on 03/04/17.
 */
public interface BlockDao extends Bean, TypeDao<BlockDao> {

    String TYPE = "block";


    void create(BlockchainBlock block, boolean wait);

    /**
     *
     * @param currencyName
     * @param number the block number
     * @param json block as JSON
     */
    void create(String currencyName, String id, byte[] json, boolean wait);

    boolean isExists(String currencyName, String id);

    void update(BlockchainBlock block, boolean wait);

    /**
     *
     * @param currencyName
     * @param number the block number, or -1 for current
     * @param json block as JSON
     */
    void update(String currencyName, String id, byte[] json, boolean wait);

    List<BlockchainBlock> findBlocksByHash(String currencyName, String query);

    int getMaxBlockNumber(String currencyName);

    BlockchainBlock getBlockById(String currencyName, String id);

    void deleteRange(final String currencyName, final int fromNumber, final int toNumber);

    List<BlockchainBlock> getBlocksByIds(String currencyName, Collection<String> ids);

    void deleteById(final String currencyName, String id);
}
