package com.kuake.cn.skyline.transport.protocol;

import com.kuake.cn.skyline.transport.common.WriteAndReadType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.ObjectUtils;

import java.nio.charset.Charset;

/**
 * 描述：Netty消息体
 *
 * @author: chenliqiang001
 * @create: 2021-03-24
 **/
public class NettyMessage {
    /**
     * 消息编码格式
     **/
    public static final Charset CHARSET = CharsetUtil.UTF_8;

    /**
     * 消息的魔术字段 4个字节
     **/
    public static final Integer MAGIC_NUMBER = 0xDDDDDDDD;


    /**
     * 消息头长度 长度字段4 + 魔术字段4 + 消息类型1
     **/
    public static final Integer HEAD_LENGTH = 4 + 4 +1;

    /**
     * 消息类型 1个字节
     **/
    private byte type;

    /**
     * 消息体
     **/
    private Body body;

    public NettyMessage() {
    }

    public NettyMessage(byte type, Body body) {
        this.type = type;
        this.body = body;
    }

    public static Integer getMagicNumber() {
        return MAGIC_NUMBER;
    }

    public static Integer getHeadLength() {
        return HEAD_LENGTH;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public static NettyMessage createRequestMessage(String ipPort, String parameters) {
        byte type = Request.ID;
        Body body = Request.builder()
                .ipPort((ObjectUtils.isEmpty(ipPort) ? null : ipPort.getBytes(CHARSET)))
                .parameters((ObjectUtils.isEmpty(parameters) ? null : parameters.getBytes(CHARSET)))
                .build();
        return new NettyMessage(type, body);
    }

    public static NettyMessage createResponseMessage(String ipPort, String result) {
        byte type = Response.ID;
        Body body = Response.builder()
                .ipPort((ObjectUtils.isEmpty(ipPort) ? null : ipPort.getBytes(CHARSET)))
                .result((ObjectUtils.isEmpty(result) ? null : result.getBytes(CHARSET)))
                .build();
        return new NettyMessage(type, body);
    }

    public ByteBuf write(ByteBufAllocator allocator) {
        CompositeByteBuf compositeByteBuf = null;
        ByteBuf headBuf = null;
        ByteBuf bodyBuf = allocator.directBuffer();

        try {
            int bodyLength = body.write(bodyBuf);

            headBuf = allocator.directBuffer(HEAD_LENGTH);
            headBuf.writeInt(HEAD_LENGTH + bodyLength);
            headBuf.writeInt(MAGIC_NUMBER);
            headBuf.writeByte(type);

            compositeByteBuf = allocator.compositeDirectBuffer();
            compositeByteBuf.addComponents(headBuf, bodyBuf);
            compositeByteBuf.writerIndex(headBuf.writerIndex() + bodyBuf.writerIndex());
        }catch (Throwable e) {
            if (ObjectUtils.isNotEmpty(headBuf)) {
                headBuf.release();
            }
            bodyBuf.release();
        }

        return compositeByteBuf;
    }

    static int writeParam(ByteBuf byteBuf, byte[] param, WriteAndReadType writeAndReadType) {
        int writeLength = 0;

        if (ObjectUtils.isNotEmpty(param)) {
            switch (writeAndReadType) {
                case NULL:
                    break;
                case BYTE:
                    byteBuf.writeByte(param.length);
                    break;
                case INT:
                    byteBuf.writeInt(param.length);
                    break;
            }
            byteBuf.writeBytes(param);
            writeLength = writeAndReadType.getCode() + param.length;
        }else {
            switch (writeAndReadType) {
                case NULL:
                    break;
                case BYTE:
                    byteBuf.writeByte(0);
                    break;
                case INT:
                    byteBuf.writeInt(0);
                    break;
            }
            writeLength = writeAndReadType.getCode();
        }

        return writeLength;
    }

    static byte[] readParam(ByteBuf byteBuf, WriteAndReadType writeAndReadType) {
        int readLength = 0;
        byte[] param = null;

        switch (writeAndReadType) {
            case NULL:
                readLength = byteBuf.readableBytes();
                break;
            case BYTE:
                readLength = byteBuf.readByte();
                break;
            case INT:
                readLength = byteBuf.readInt();
                break;
        }

        if (readLength > 0) {
            param = new byte[readLength];
            byteBuf.readBytes(param);
        }

        return param;
    }

    public static abstract class Body {
        /**
         * 写入数据到指定的{@link ByteBuf}中
         * @param: byteBuf
         * @return: 写入的字节长度
         **/
        public abstract int write(ByteBuf byteBuf);
    }

}
