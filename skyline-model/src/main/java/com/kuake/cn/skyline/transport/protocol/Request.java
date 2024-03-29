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
public class Request extends NettyMessage.Body {
    /**
     * 消息类型ID
     **/
    public static final byte ID = 10;

    /**
     * 请求主机端口号
     **/
    private byte[] ipPort;

    /**
     * 请求入参
     **/
    private byte [] parameters;

    public Request(byte[] ipPort, byte[] parameters) {
        this.ipPort = ipPort;
        this.parameters = parameters;
    }

    public byte[] getIpPort() {
        return ipPort;
    }

    public void setIpPort(byte[] ipPort) {
        this.ipPort = ipPort;
    }

    public byte[] getParameters() {
        return parameters;
    }

    public void setParameters(byte[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "Request{" +
                "ipPort=" + Arrays.toString(ipPort) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }

    public int write(ByteBuf byteBuf) {
        int writeLength = 0;

        writeLength += NettyMessage.writeParam(byteBuf, ipPort, WriteAndReadType.BYTE);
        writeLength += NettyMessage.writeParam(byteBuf, parameters, WriteAndReadType.NULL);

        return writeLength;
    }

    public static NettyMessage.Body read(ByteBuf byteBuf) {
        byte[] ipPort = NettyMessage.readParam(byteBuf, WriteAndReadType.BYTE);
        byte[] parameters = NettyMessage.readParam(byteBuf, WriteAndReadType.NULL);

        return Request.builder()
                .ipPort(ipPort)
                .parameters(parameters)
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
        byte [] parameters;

        RequestBuilder ipPort(byte[] ipPort) {
            this.ipPort = ipPort;
            return this;
        }

        RequestBuilder parameters(byte[] parameters) {
            this.parameters = parameters;
            return this;
        }

        Request build() {
            return new Request(ipPort, parameters);
        }
    }
}
