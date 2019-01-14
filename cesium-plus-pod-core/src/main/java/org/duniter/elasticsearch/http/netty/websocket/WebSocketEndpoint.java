package org.duniter.elasticsearch.http.netty.websocket;

import org.elasticsearch.common.bytes.BytesReference;

import javax.websocket.CloseReason;

public interface WebSocketEndpoint {

    String WEBSOCKET_PATH = "/ws";

    void onOpen(NettyWebSocketSession session);

    void onMessage(String message);

    void onMessage(BytesReference bytes);

    void onClose(CloseReason reason);
}
