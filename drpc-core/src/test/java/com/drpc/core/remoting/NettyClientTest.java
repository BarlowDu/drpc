package com.drpc.core.remoting;

import com.drpc.core.channel.Channel;
import com.drpc.core.channel.NettyChannel;
import com.drpc.core.complier.JavassistProxy;
import com.drpc.core.future.DefaultFuture;
import com.drpc.core.model.ComplexModel;
import com.drpc.core.model.SimpleModel;
import com.drpc.core.protocol.DefaultRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Constructor;

public class NettyClientTest {
    public static void main(String[] args)throws Exception{
        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DecodeHandler());
                }
            });
// 启动客户端
            ChannelFuture f = b.connect(host, port).sync(); // (5)
// 等待连接关闭
            int n=0;
            Object o=(Object)n;

            Class<?> clazz= JavassistProxy.getProxy(TService.class,new NettyChannel(f.channel()));
            Constructor constructor= clazz.getConstructor();
            TService service=(TService)constructor.newInstance();
            //f.channel().closeFuture().sync();
            for(int i=0;i<10;i++){
                service.add(i,i);
            }
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}
