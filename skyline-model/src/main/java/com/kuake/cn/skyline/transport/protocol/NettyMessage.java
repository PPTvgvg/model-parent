package com.kuake.cn.skyline.transport.protocol;

import io.netty.util.CharsetUtil;

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
    private static final Charset CHARSET = CharsetUtil.UTF_8;

    /**
     * 消息的魔术字段 4个字节
     **/
    private static final Integer MAGIC_NUMBER = 0xDDDDDDDD;

}
