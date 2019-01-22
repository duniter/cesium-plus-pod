package org.duniter.elasticsearch.http.netty;


import org.duniter.elasticsearch.http.netty.websocket.WebSocketEndpoint;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.network.NetworkService;
import org.elasticsearch.common.path.PathTrie;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.http.netty.NettyHttpChannel;
import org.elasticsearch.http.netty.NettyHttpRequest;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.support.RestUtils;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

public class NettyHttpServerTransport extends  org.elasticsearch.http.netty.NettyHttpServerTransport {

    private final PathTrie<Class<? extends WebSocketEndpoint>> websocketEndpoints;

    @Inject
    public NettyHttpServerTransport(Settings settings,
                                    NetworkService networkService,
                                    BigArrays bigArrays) {
        super(settings, networkService, bigArrays);
        this.websocketEndpoints = new PathTrie(RestUtils.REST_DECODER);
    }

    @Override
    public ChannelPipelineFactory configureServerChannelPipelineFactory() {
        return new HttpChannelPipelineFactory(this, this.detailedErrorsEnabled);
    }

    protected static class HttpChannelPipelineFactory extends org.elasticsearch.http.netty.NettyHttpServerTransport.HttpChannelPipelineFactory {

        protected final HttpRequestHandler handler;

        public HttpChannelPipelineFactory(NettyHttpServerTransport transport, boolean detailedErrorsEnabled) {
            super(transport, detailedErrorsEnabled);
            this.handler = new HttpRequestHandler(transport, detailedErrorsEnabled);
        }

        public ChannelPipeline getPipeline() throws Exception {
            ChannelPipeline pipeline = super.getPipeline();

            // Replace default HttpRequestHandler by a custom handler with WebSocket support
            pipeline.replace("handler", "handler", handler);

            return pipeline;
        }
    }


    public <T extends WebSocketEndpoint> void addEndpoint(String path, Class<T> handler) {
        websocketEndpoints.insert(path, handler);
    }

    @Override
    protected void dispatchRequest(RestRequest request, RestChannel channel) {
        super.dispatchRequest(request, channel);
    }

    public void dispathWebsocketRequest(NettyHttpRequest request, NettyHttpChannel channel) {

        WebSocketEndpoint wsEndpoint = createWebsocketEndpoint(request);
        if (wsEndpoint != null) {

            WebSocketRequestHandler channelHandler = new WebSocketRequestHandler(wsEndpoint);

            // Replacing the new handler to the existing pipeline to handle
            request.getChannel().getPipeline().replace("handler", "websocketHandler", channelHandler);

            // Execute the handshake
            channelHandler.handleHandshake(request);

        } else if (request.method() == RestRequest.Method.OPTIONS) {
            channel.sendResponse(new BytesRestResponse(RestStatus.OK));
        } else {
            if (logger.isTraceEnabled()) logger.trace(String.format("No matching rules for %s request [%s]: reject", request.method(), request.rawPath()));
            channel.sendResponse(new BytesRestResponse(RestStatus.BAD_REQUEST, "Websocket to URI [" + request.uri() + "] not authorized"));
        }
    }


    /* -- protected method -- */

    protected <T extends WebSocketEndpoint> T createWebsocketEndpoint(RestRequest request) {
        String path = request.rawPath();
        Class<? extends WebSocketEndpoint> clazz = websocketEndpoints != null ? websocketEndpoints.retrieve(path, request.params()) : null;
        if (clazz == null ){
            return null;
        }
        try {
            return (T)clazz.newInstance();
        }
        catch(Exception e) {
            logger.error(String.format("Could not create websocket endpoint instance, from class %s: %s", clazz.getName(), e.getMessage()), e);
            return null;
        }
    }

}
