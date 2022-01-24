package com.kuake.cn.skyline.server.server.service;

import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.server.server.handler.ServerRequestHandler;
import com.kuake.cn.skyline.transport.server.NettyServer;

/**
 * 描述：Netty服务启动接口
 * @author: kuake.cn
 * @create: 2021-03-26 16:14
 **/
public class NettyEndpointService implements EndpointService {
    private NettyServer nettyServer;

    public NettyEndpointService() {
        nettyServer = new NettyServer();
    }

    @Override
    public boolean startRpcServer() {
        return nettyServer.start(Environment.getServerIp(), Environment.getRpcPort(), new ServerRequestHandler());
    }

    @Override
    public void shutdown() {
        nettyServer.close();
    }
}
