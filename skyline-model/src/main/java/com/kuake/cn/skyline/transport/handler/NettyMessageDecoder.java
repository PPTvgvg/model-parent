package com.kuake.cn.skyline.transport.handler;

import com.kuake.cn.skyline.common.exception.SkyLineException;
import com.kuake.cn.skyline.transport.protocol.NettyMessage;
import com.kuake.cn.skyline.transport.protocol.Request;
import com.kuake.cn.skyline.transport.protocol.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.lang3.ObjectUtils;

import java.nio.ByteOrder;

/**
 * 描述：消息解码器
 * @author: kuake.cn
 * @create: 2021-03-25 15:25
 **/
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public NettyMessageDecoder() {
        super(Integer.MAX_VALUE, 0, 4, -4, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf msg = (ByteBuf) super.decode(ctx, in);

        if (ObjectUtils.isEmpty(msg)) {
            return null;
        }

        try {
            int magicNumber = msg.readInt();
            if (ObjectUtils.notEqual(magicNumber, NettyMessage.MAGIC_NUMBER)) {
                throw new  SkyLineException("Incorrect magic number.");
            }
            byte type = msg.readByte();
            NettyMessage.Body body;
            switch (type){
                case Request.ID:
                    body = Request.read(msg);
                    break;
                case Response.ID:
                    body = Response.read(msg);
                    break;
                default:
                    throw new RuntimeException("None Supported Message Type: " + type);
            }
            return body;
        }finally {
            msg.release();
        }
    }
}
