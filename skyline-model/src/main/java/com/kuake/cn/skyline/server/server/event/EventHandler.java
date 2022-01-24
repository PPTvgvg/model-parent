package com.kuake.cn.skyline.server.server.event;

import io.netty.channel.ChannelHandlerContext;

/**
 * 描述：事件处理接口
 * @author: kuake.cn
 * @create: 2021-03-28 16:38
 **/
public interface EventHandler<T> {

    void handle(ChannelHandlerContext ctx, T event);
}
