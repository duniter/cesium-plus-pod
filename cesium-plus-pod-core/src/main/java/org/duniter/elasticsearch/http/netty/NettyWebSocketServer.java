package org.duniter.elasticsearch.http.netty;

import org.duniter.elasticsearch.http.netty.websocket.WebSocketEndpoint;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.http.HttpServerTransport;

public class NettyWebSocketServer {

    private final ESLogger logger;
    private HttpServerTransport serverTransport;

    @Inject
    public  NettyWebSocketServer(HttpServerTransport serverTransport) {
        logger = Loggers.getLogger("duniter.ws");
        this.serverTransport = serverTransport;
    }

    public <T extends WebSocketEndpoint> void addEndpoint(String path, Class<T> handler) {
        if (serverTransport instanceof NettyHttpServerTransport) {
            NettyHttpServerTransport transport = (NettyHttpServerTransport)serverTransport;
            transport.addEndpoint(path, handler);
        }
        else {
            logger.warn("Ignoring websocket endpoint {" + handler.getName()+ "}: server transport is not compatible");
        }
    }

}
