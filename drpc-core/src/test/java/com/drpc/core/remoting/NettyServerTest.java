package com.drpc.core.remoting;

import com.drpc.core.complier.Invoker;
import com.drpc.core.protocol.DefaultRequest;
import com.drpc.core.register.ServiceRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import javax.xml.ws.Response;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class NettyServerTest {


    public static void main(String[] args) throws Exception {

        registeService();

        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DecodeHandler())
                                    .addLast(new SimpleChannelInboundHandler<DefaultRequest>() {

                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, DefaultRequest msg) throws Exception {
                                            Invoker  invoker=ServiceRegister.getInvoker(msg.getServiceName());
                                            if(invoker!=null){
                                                invoker.invoke(msg.getMethodName(),msg.getArguments());
                                            }
                                        }
                                    });

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
// 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(8080).sync(); // (7)
// 等待服务器 socket 关闭 。
// 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static void registeService() throws Exception {
        ServiceRegister.Add(TService.class,new TServiceImpl());
    }
}




