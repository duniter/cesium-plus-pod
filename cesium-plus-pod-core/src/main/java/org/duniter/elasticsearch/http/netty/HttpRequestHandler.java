package org.duniter.elasticsearch.http.netty;

import org.duniter.elasticsearch.http.netty.websocket.WebSocketEndpoint;
import org.elasticsearch.http.netty.NettyHttpChannel;
import org.elasticsearch.http.netty.NettyHttpRequest;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;

@ChannelHandler.Sharable
public class HttpRequestHandler extends org.elasticsearch.http.netty.HttpRequestHandler {

    private final NettyHttpServerTransport serverTransport;
    private final boolean detailedErrorsEnabled;

    public HttpRequestHandler(NettyHttpServerTransport transport, boolean detailedErrorsEnabled) {
        super(transport, detailedErrorsEnabled);
        this.serverTransport = transport;
        this.detailedErrorsEnabled = detailedErrorsEnabled;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (e.getMessage() instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) e.getMessage();
            HttpHeaders headers = httpRequest.headers();

            // If web socket path, and connection request
            if (httpRequest.getUri().startsWith(WebSocketEndpoint.WEBSOCKET_PATH + "/") &&
                    HttpHeaders.Names.UPGRADE.equalsIgnoreCase(headers.get(org.apache.http.HttpHeaders.CONNECTION)) &&
                    HttpHeaders.Values.WEBSOCKET.equalsIgnoreCase(headers.get(org.apache.http.HttpHeaders.UPGRADE))) {

                // Convert request and channel
                NettyHttpRequest request = new NettyHttpRequest(httpRequest, ctx.getChannel());
                NettyHttpChannel channel = new NettyHttpChannel(this.serverTransport, request, null, this.detailedErrorsEnabled);

                serverTransport.dispathWebsocketRequest(request, channel);
                ctx.sendUpstream(e);
                return;
            }
        }
        super.messageReceived(ctx, e);
    }


}
