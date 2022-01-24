package com.kuake.cn.skyline.transport.protocol;

import com.kuake.cn.skyline.transport.common.WriteAndReadType;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * 描述：请求实体类
 *
 * @author: chenliqiang001
 * @create: 2021-03-24
 **/
public class Response extends NettyMessage.Body {
    /**
     * 消息类型ID
     **/
    public static final byte ID = 20;

    /**
     * 请求主机端口号
     **/
    private byte[] ipPort;

    /**
     * 请求入参
     **/
    private byte [] result;

    public Response(byte[] ipPort, byte[] result) {
        this.ipPort = ipPort;
        this.result = result;
    }

    public byte[] getIpPort() {
        return ipPort;
    }

    public void setIpPort(byte[] ipPort) {
        this.ipPort = ipPort;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Response{" +
                "ipPort=" + Arrays.toString(ipPort) +
                ", result=" + Arrays.toString(result) +
                '}';
    }

    public int write(ByteBuf byteBuf) {
        int writeLength = 0;

        writeLength += NettyMessage.writeParam(byteBuf, ipPort, WriteAndReadType.BYTE);
        writeLength += NettyMessage.writeParam(byteBuf, result, WriteAndReadType.NULL);

        return writeLength;
    }

    public static NettyMessage.Body read(ByteBuf byteBuf) {
        byte[] ipPort = NettyMessage.readParam(byteBuf, WriteAndReadType.BYTE);
        byte[] result = NettyMessage.readParam(byteBuf, WriteAndReadType.NULL);

        return Response.builder()
                .ipPort(ipPort)
                .result(result)
                .build();
    }

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public static class RequestBuilder {
        /**
         * 请求主机端口号
         **/
        byte[] ipPort;

        /**
         * 请求入参
         **/
        byte [] result;

        RequestBuilder ipPort(byte[] ipPort) {
            this.ipPort = ipPort;
            return this;
        }

        RequestBuilder result(byte[] result) {
            this.result = result;
            return this;
        }

        Response build() {
            return new Response(ipPort, result);
        }
    }
}
