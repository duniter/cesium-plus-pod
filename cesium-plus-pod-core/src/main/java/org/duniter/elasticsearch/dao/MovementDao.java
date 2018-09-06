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
import org.duniter.elasticsearch.model.Movement;
import org.elasticsearch.action.bulk.BulkRequestBuilder;

import java.util.List;

/**
 * Created by blavenie on 03/04/17.
 */
public interface MovementDao extends Bean, TypeDao<MovementDao> {

    String TYPE = "movement";

    void create(Movement block, boolean wait);

    boolean isExists(String currencyName, String id);

    void update(Movement operation, boolean wait);

    void delete(String currency, String id, boolean wait);

    BulkRequestBuilder bulkDeleteByBlock(String currency,
                                             String number,
                                             String hash,
                                             BulkRequestBuilder bulkRequest,
                                             int bulkSize,
                                             boolean flushAll);
}
