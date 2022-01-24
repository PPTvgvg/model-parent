package com.kuake.cn.skyline.server.server.handler;

import com.kuake.cn.skyline.common.exception.SkyLineException;
import com.kuake.cn.skyline.server.server.event.factory.EventHandlerFactory;
import com.kuake.cn.skyline.transport.protocol.NettyMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述：server端请求处理器
 * @author: kuake.cn
 * @create: 2021-03-28 16:16
 **/
@Slf4j
@ChannelHandler.Sharable
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //TODO: 添加 server -> client 的路由信息，请求返回是用
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //TODO 通道关闭时，即请求结束时，将路由信息移除
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (!(msg instanceof NettyMessage.Body)) {
                throw new SkyLineException("不支持的msg格式");
            }

            NettyMessage.Body message = (NettyMessage.Body) msg;
            EventHandlerFactory.getEventHandler(message.getClass().getName()).handle(ctx,message);

        }finally {
            super.channelRead(ctx, msg);
        }
    }
}
