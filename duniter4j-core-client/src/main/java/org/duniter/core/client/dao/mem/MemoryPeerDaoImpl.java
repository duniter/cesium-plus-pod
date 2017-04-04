package org.duniter.core.client.dao.mem;

/*
 * #%L
 * UCoin Java :: Core Client API
 * %%
 * Copyright (C) 2014 - 2016 EIS
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

import org.duniter.core.client.dao.PeerDao;
import org.duniter.core.client.model.local.Peer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by blavenie on 29/12/15.
 */
public class MemoryPeerDaoImpl implements PeerDao {

    private Map<String, Peer> peersByCurrencyId = new HashMap<>();

    public MemoryPeerDaoImpl() {
        super();
    }

    @Override
    public Peer create(Peer entity) {
        entity.setId(entity.computeKey());

        peersByCurrencyId.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public Peer update(Peer entity) {
        peersByCurrencyId.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Peer getById(String id) {
        return peersByCurrencyId.get(id);
    }

    @Override
    public void remove(Peer entity) {
        peersByCurrencyId.remove(entity.getId());
    }

    @Override
    public List<Peer> getPeersByCurrencyId(final String currencyId) {
        return peersByCurrencyId.values().stream()
            .filter(peer -> currencyId.equals(peer.getCurrency()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExists(final String currencyId, final  String peerId) {
        return peersByCurrencyId.values().stream()
                .anyMatch(peer -> currencyId.equals(peer.getCurrency()) && peerId.equals(peer.getId()));
    }
}
