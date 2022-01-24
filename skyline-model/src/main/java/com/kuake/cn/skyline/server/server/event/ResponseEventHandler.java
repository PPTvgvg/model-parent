package com.kuake.cn.skyline.server.server.event;

import com.kuake.cn.skyline.transport.protocol.Response;
import io.netty.channel.ChannelHandlerContext;

/**
 * 描述：请求响应事件处理器
 * @author: kuake.cn
 * @create: 2021-03-28 17:13
 **/
public class ResponseEventHandler implements EventHandler<Response> {
    private static final String HANDLER_NAME = Response.class.getName();

    @Override
    public void handle(ChannelHandlerContext ctx, Response event) {

    }
}
