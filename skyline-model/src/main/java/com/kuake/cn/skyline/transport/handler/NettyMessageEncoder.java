package com.kuake.cn.skyline.transport.handler;

import com.kuake.cn.skyline.common.exception.SkyLineException;
import com.kuake.cn.skyline.transport.protocol.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.commons.lang3.ObjectUtils;
import sun.nio.ch.Net;

/**
 * 描述：编码器
 * @author: kuake.cn
 * @create: 2021-03-25 16:36
 **/
public class NettyMessageEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof NettyMessage) {
            ByteBuf serialized = null;
            try {
                serialized = ((NettyMessage) msg).write(ctx.alloc());
            }catch (Throwable e) {
                throw new SkyLineException("Error while serializing message: " + msg, e);
            }finally {
                ctx.write(serialized, promise);
            }
        }else {
            throw new SkyLineException("Not a NettyMessage: " + msg);
        }
    }
}
