package com.drpc.core.channel;

import com.drpc.core.future.DefaultFuture;
import com.drpc.core.protocol.DefaultRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicLong;

public class NettyChannel implements Channel {
    private io.netty.channel.Channel channel;
    private static AtomicLong REQUESTID = new AtomicLong(0);

    public NettyChannel(io.netty.channel.Channel channel) {
        this.channel = channel;
    }

    @Override
    public DefaultFuture invoke(String serviceName, String methodName, Object[] args) {
        DefaultRequest request = new DefaultRequest();
        request.setRequestId(DefaultFuture.newRequestId());
        request.setServiceName(serviceName);
        request.setMethodName(methodName);
        request.setArguments(args);
        channel.write(request);
        channel.flush();
        return DefaultFuture.Add(request.getRequestId());

    }
}
