package com.kuake.cn.skyline.server.server.service;

/**
 * 描述：RPC Server 启动接口
 * @author: kuake.cn
 * @create: 2021-03-26 16:00
 **/
public interface EndpointService {
    /**
     * 启动一个rpc server
     **/
    boolean startRpcServer();

    /**
     * 关闭一个rpc server
     **/
    void shutdown();
}
