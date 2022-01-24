package com.kuake.cn.skyline.server.server.event;

import com.kuake.cn.skyline.transport.protocol.Request;
import io.netty.channel.ChannelHandlerContext;

/**
 * 描述：请求事件处理器
 * @author: kuake.cn
 * @create: 2021-03-28 17:09
 **/
public class RequestEventHandler implements EventHandler<Request> {
    private static final String HANDLER_NAME = Request.class.getName();

    @Override
    public void handle(ChannelHandlerContext ctx, Request event) {

    }
}
