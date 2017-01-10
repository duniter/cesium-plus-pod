package org.duniter.core.client.service.bma;

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

import java.util.ArrayList;
import java.util.List;

import org.duniter.core.client.model.bma.EndpointProtocol;
import org.duniter.core.client.model.bma.NetworkPeering;
import org.duniter.core.client.model.bma.NetworkPeers;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;

/**
 * Created by eis on 05/02/15.
 */
public class NetworkRemoteServiceImpl extends BaseRemoteServiceImpl implements NetworkRemoteService{


    public static final String URL_BASE = "/network";

    public static final String URL_PEERING = URL_BASE + "/peering";

    public static final String URL_PEERS = URL_BASE + "/peers";

    public static final String URL_PEERING_PEERS = URL_PEERING + "/peers";

    public static final String URL_PEERING_PEERS_LEAF = URL_PEERING + "/peers?leaf=";

    public NetworkRemoteServiceImpl() {
        super();
    }

    public NetworkPeering getPeering(Peer peer) {
        NetworkPeering result = httpService.executeRequest(peer, URL_PEERING, NetworkPeering.class);
        return result;
    }

    @Override
    public List<Peer> getPeers(Peer peer) {
        return findPeers(peer, null, null, null, null);
    }

    @Override
    public List<Peer> findPeers(Peer peer, String status, EndpointProtocol endpointProtocol, Integer currentBlockNumber, String currentBlockHash) {
        Preconditions.checkNotNull(peer);

        List<Peer> result = new ArrayList<Peer>();

        NetworkPeers remoteResult = httpService.executeRequest(peer, URL_PEERS, NetworkPeers.class);

        for (NetworkPeers.Peer remotePeer: remoteResult.peers) {
            boolean match = (status == null || status.equalsIgnoreCase(remotePeer.status))
                    && (currentBlockNumber == null || currentBlockNumber.equals(parseBlockNumber(remotePeer)))
                    && (currentBlockHash == null || currentBlockHash.equals(parseBlockHash(remotePeer)));

            if (match) {

                for (NetworkPeering.Endpoint endpoint : remotePeer.endpoints) {

                    match = endpointProtocol == null || endpointProtocol == endpoint.protocol;

                    if (match) {
                        Peer childPeer = toPeer(endpoint);
                        if (childPeer != null) {
                            result.add(childPeer);
                        }
                    }

                }
            }
        }

        return result;
    }

    /* -- Internal methods -- */

    protected Peer toPeer(NetworkPeering.Endpoint source) {
        Peer target = new Peer();
        if (StringUtils.isNotBlank(source.ipv4)) {
            target.setHost(source.ipv4);
        } else if (StringUtils.isNotBlank(source.ipv6)) {
            target.setHost(source.ipv6);
        } else if (StringUtils.isNotBlank(source.url)) {
            target.setHost(source.url);
        } else {
            target = null;
        }
        if (target != null && source.port != null) {
            target.setPort(source.port);
        }
        return target;
    }

    protected Integer parseBlockNumber(NetworkPeers.Peer remotePeer) {
        Preconditions.checkNotNull(remotePeer);

        if (remotePeer.block == null) {
            return null;
        }
        int index = remotePeer.block.indexOf("-");
        if (index == -1) {
            return null;
        }

        String str = remotePeer.block.substring(0, index);
        try {
            return Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return null;
        }
    }

    protected String parseBlockHash(NetworkPeers.Peer remotePeer) {
        Preconditions.checkNotNull(remotePeer);

        if (remotePeer.block == null) {
            return null;
        }
        int index = remotePeer.block.indexOf("-");
        if (index == -1) {
            return null;
        }

        String hash = remotePeer.block.substring(index+1);
        return hash;
    }
}
