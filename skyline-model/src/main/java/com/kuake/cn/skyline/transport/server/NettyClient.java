package com.kuake.cn.skyline.transport.server;

import com.kuake.cn.skyline.transport.handler.NettyMessageDecoder;
import com.kuake.cn.skyline.transport.handler.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 描述：Netty客户端
 * @author: kuake.cn
 * @create: 2021-03-25 19:49
 **/
public class NettyClient {
    private static final String SKY_LINE_CLIENT_IO = "skyline-client-io";
    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;
    private volatile boolean isInitialized;

    private ChannelFuture channelFuture;

    public synchronized void init(final ChannelHandler... channelHandlers) {
        if (isInitialized) {
            return;
        }
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup(2, new DefaultThreadFactory(SKY_LINE_CLIENT_IO, true));

        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new NettyMessageDecoder())
                                .addLast(new NettyMessageEncoder())
                                .addLast(channelHandlers);
                    }
                });
        isInitialized = true;
    }

    public ChannelFuture connect(String ip, Integer port) {
        channelFuture = bootstrap.connect(ip, port);
        return channelFuture;
    }

    public void close() {
        try {
            channelFuture.channel().close();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
