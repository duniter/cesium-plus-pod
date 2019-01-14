package org.duniter.elasticsearch.http.netty.websocket;

import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.http.netty.NettyHttpRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import javax.websocket.CloseReason;
import java.util.Map;

public class NettyWebSocketSession {

    private Channel channel;
    private Map<String, String> pathParameters;

    public NettyWebSocketSession(Channel channel, Map<String, String> pathParameters) {
        this.channel = channel;
        this.pathParameters = pathParameters;
    }

    public void close(CloseReason closeReason) {

        CloseWebSocketFrame frame = new CloseWebSocketFrame(closeReason.getCloseCode().getCode(), closeReason.getReasonPhrase());
        ChannelFuture future = channel.write(frame);

        future.addListener(ChannelFutureListener.CLOSE);
    }

    public void sendText(String text) {
        channel.write(new TextWebSocketFrame(text));
    }

    public void sendBinary(ChannelBuffer buffer) {
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame();
        frame.setBinaryData(buffer);
        channel.write(frame);
    }

    public void sendBinary(BytesReference bytes) {
        sendBinary(bytes.toChannelBuffer());
    }

    public Map<String, String> getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters( Map<String, String> pathParameters) {
        this.pathParameters = pathParameters;
    }

    public String getId() {
        return String.valueOf(this.hashCode());
    }

}
