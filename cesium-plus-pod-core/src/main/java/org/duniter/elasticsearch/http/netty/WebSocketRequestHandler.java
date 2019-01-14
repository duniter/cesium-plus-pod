package org.duniter.elasticsearch.http.netty;

import org.duniter.elasticsearch.http.netty.websocket.NettyWebSocketSession;
import org.duniter.elasticsearch.http.netty.websocket.WebSocketEndpoint;
import org.elasticsearch.common.bytes.ChannelBufferBytesReference;
import org.elasticsearch.http.netty.NettyHttpRequest;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.websocketx.*;

import javax.websocket.CloseReason;

@ChannelHandler.Sharable
public class WebSocketRequestHandler extends SimpleChannelHandler {

    private final WebSocketEndpoint endpoint;
    private NettyWebSocketSession session;

    public WebSocketRequestHandler(WebSocketEndpoint endpoint) {
        super();
        this.endpoint = endpoint;
    }

    /* Do the handshaking for WebSocket request */
    public ChannelFuture handleHandshake(final NettyHttpRequest request) {
        WebSocketServerHandshakerFactory wsFactory =
                new WebSocketServerHandshakerFactory(getWebSocketURL(request), null, true);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request.request());
        if (handshaker == null) {
            return wsFactory.sendUnsupportedWebSocketVersionResponse(request.getChannel());
        }

        ChannelFuture future = handshaker.handshake(request.getChannel(), request.request());
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                // Session is open
                session = new NettyWebSocketSession(future.getChannel(), request.params());
                endpoint.onOpen(session);
            }
        });
        return future;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (endpoint == null) return; // not open

        Object msg = e.getMessage();
        if (msg instanceof NettyWebSocketSession) {
            endpoint.onOpen((NettyWebSocketSession)msg);
        }

        else if (msg instanceof WebSocketFrame) {

            // Received binary
            if (msg instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame frame = (BinaryWebSocketFrame)msg;
                endpoint.onMessage(new ChannelBufferBytesReference(frame.getBinaryData()));
            }

            // Received text
            else if (msg instanceof TextWebSocketFrame) {
                TextWebSocketFrame frame = (TextWebSocketFrame) msg;
                endpoint.onMessage(frame.getText());
            }

            // Ping event
            else if (msg instanceof PingWebSocketFrame) {
               // TODO
            }

            // Pong event
            else if (msg instanceof PongWebSocketFrame) {
                // TODO
            }

            // Close
            else if (msg instanceof CloseWebSocketFrame) {
                ctx.getChannel().close();
                CloseWebSocketFrame frame = (CloseWebSocketFrame)msg;
                endpoint.onClose(new CloseReason(getCloseCode(frame), frame.getReasonText()));
            }

            // Unknown event
            else {
                System.out.println("Unsupported WebSocketFrame");
            }
        }
    }

    protected String getWebSocketURL(NettyHttpRequest req) {
        return "ws://" + req.request().headers().get(HttpHeaders.Names.HOST) + req.rawPath() ;
    }

    protected CloseReason.CloseCode getCloseCode(CloseWebSocketFrame frame) {

        int statusCode = frame.getStatusCode();
        if (statusCode == -1) return CloseReason.CloseCodes.NO_STATUS_CODE;
        try {
            return CloseReason.CloseCodes.getCloseCode(statusCode);
        }
        catch(IllegalArgumentException e) {
            return CloseReason.CloseCodes.NO_STATUS_CODE;
        }
    }
}
