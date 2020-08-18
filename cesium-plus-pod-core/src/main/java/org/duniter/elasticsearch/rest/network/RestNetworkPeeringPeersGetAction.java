package org.duniter.elasticsearch.rest.network;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.duniter.core.client.model.bma.NetworkPeers;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.util.merkle.MerkleTree;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.rest.RestXContentBuilder;
import org.duniter.elasticsearch.rest.XContentRestResponse;
import org.duniter.elasticsearch.rest.security.RestSecurityController;
import org.duniter.elasticsearch.service.NetworkService;
import org.duniter.elasticsearch.util.bytes.JsonNodeBytesReference;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * A rest to post a request to process a new currency/peer.
 *
 */
public class RestNetworkPeeringPeersGetAction extends BaseRestHandler {


    private PluginSettings pluginSettings;
    private NetworkService networkService;

    @Inject
    public RestNetworkPeeringPeersGetAction(Settings settings, PluginSettings pluginSettings, RestController controller, Client client, RestSecurityController securityController,
                                            NetworkService networkService) {
        super(settings, controller, client);
        this.pluginSettings = pluginSettings;

        if (StringUtils.isBlank(pluginSettings.getClusterRemoteHost())) {
            logger.warn(String.format("The cluster address can not be published on the network. /\\!\\\\ Fill in the options [cluster.remote.xxx] in the configuration (recommended)."));
        }
        else {
            securityController.allow(RestRequest.Method.GET, "(/[^/]+)?/network/peering/peers");

            controller.registerHandler(RestRequest.Method.GET, "/network/peering/peers", this);
            controller.registerHandler(RestRequest.Method.GET, "/{currency}/network/peering/peers", this);
        }

        this.networkService = networkService;
    }

    @Override
    protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
        String currency = request.param("currency");
        boolean showLeaves = request.paramAsBoolean("leaves", false);
        String leaf = request.param("leaf");

        try {
            Map<String, NetworkPeers.Peer> peerByHash = getBmaPeersByHash(currency);

            // Get all peer's hash (will become the merkle tree's leaves)
            List<String> leaves =  peerByHash.keySet().stream()
                    .sorted()
                    .collect(Collectors.toList());

            MerkleTree tree = new MerkleTree("sha156", leaves, true);

            if (StringUtils.isBlank(leaf)) {
                XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                        .field("depth", tree.depth())
                        .field("nodesCount", tree.nodes())
                        .field("leavesCount", leaves.size())
                        .field("root", tree.root());
                if (showLeaves) {
                    builder.field("leaves", leaves);
                }
                builder.endObject();
                channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
            }
            // Get a leaf
            else {
                NetworkPeers.Peer peer = peerByHash.get(leaf);

                // Peer not found: error
                if (peer == null) {
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                            .field("ucode", 2012)
                            .field("message", "Peer not found")
                            .endObject();
                    channel.sendResponse(new XContentRestResponse(request, RestStatus.BAD_REQUEST, builder));
                }
                // Serialize the leaf
                else {
                    ObjectMapper mapper = getObjectMapper();
                    XContentBuilder builder = RestXContentBuilder.restContentBuilder(request).startObject()
                            .field("depth", tree.depth())
                            .field("nodesCount", tree.nodes())
                            .field("leavesCount", leaves.size())
                            .field("root", tree.root())
                            .array("leaves", new String[]{})
                            .startObject("leaf")
                            .field("hash", leaf)
                            // TODO: need to serialize 'raw' field also, but it excluded using a @JsonIgnore ...
                            .rawField("value", new JsonNodeBytesReference(peer, mapper))
                            .endObject().endObject();
                    channel.sendResponse(new XContentRestResponse(request, RestStatus.OK, builder));
                }
            }
        } catch (IOException ioe) {
            throw new TechnicalException(String.format("Error while generating JSON for [/network/peering]: %s", ioe.getMessage()), ioe);
        }

    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }

    protected Map<String, NetworkPeers.Peer> getBmaPeersByHash(final String currency) throws ExecutionException {
        Collection<NetworkPeers.Peer> peers = networkService.getPeersAsBmaFormatWithCache(currency);
        return peers.stream()
                .map(p -> {
                    // Compute the peer's hash, is notalready done
                    if (p.getHash() == null && p.getRaw() != null) {
                        // Compute hash, as expected by Duniter
                        // WARN: do NOT use the Cesium+ pod hash here
                        String hash = DigestUtils.sha256Hex(p.getRaw()).toUpperCase();
                        p.setHash(hash);
                    }
                    return p;
                })
                .collect(Collectors.toMap(
                        NetworkPeers.Peer::getHash,
                        p -> p));
    }

}