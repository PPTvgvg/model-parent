package com.kuake.cn.skyline.transport.server;

import com.kuake.cn.skyline.transport.handler.NettyMessageDecoder;
import com.kuake.cn.skyline.transport.handler.NettyMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：Netty服务器
 * @author: kuake.cn
 * @create: 2021-03-25 18:33
 **/
public class NettyServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    public boolean start(String ip, Integer port, final ChannelHandler channelHandler) {
        bootstrap = new ServerBootstrap();

        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("ACCEPTOR", false));
        workerGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("IO-WORKER", true));

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new NettyMessageDecoder())
                                .addLast(new NettyMessageEncoder())
                                .addLast(channelHandler);
                    }
                });

        boolean started;
        try {
            ChannelFuture channelFuture = bootstrap.bind(ip, port);
            channelFuture.syncUninterruptibly();
            channel = channelFuture.channel();
            if (channelFuture.isSuccess()) {
                started = true;
                LOGGER.info("NettyServer is started at ip:{}, port:{}.", ip, port);
            }else {
                started = false;
                LOGGER.error("Error while start netty server.", channelFuture.cause());
            }
        }catch (Throwable e) {
            LOGGER.error("Error while start netty server.", e);
            started = false;
        }

        return started;
    }

    public void close() {
        if (ObjectUtils.isNotEmpty(channel)) {
            channel.close();
        }
        if (ObjectUtils.isNotEmpty(bootstrap)) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
