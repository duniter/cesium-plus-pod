package org.duniter.elasticsearch.http.netty.websocket;

import org.elasticsearch.common.bytes.BytesReference;

import javax.websocket.CloseReason;

public class NettyBaseWebSocketEndpoint implements WebSocketEndpoint {

    @Override
    public void onOpen(NettyWebSocketSession session) {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onMessage(BytesReference bytes) {

    }

    public void onClose(CloseReason reason) {
    }

}
