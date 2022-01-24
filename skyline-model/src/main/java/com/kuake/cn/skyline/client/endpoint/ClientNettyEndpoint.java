package com.kuake.cn.skyline.client.endpoint;

import com.kuake.cn.skyline.client.listen.ZkListener;
import com.kuake.cn.skyline.transport.endpoint.NodeWithWeight;

import java.util.List;

/**
 * 描述：
 *
 * @author: kuake.cn
 * @create: 2021-04-02 16:04
 **/
public class ClientNettyEndpoint implements ZkListener {
    private static ClientNettyEndpoint clientNettyEndpoint = new ClientNettyEndpoint();

    private volatile boolean initialized = false;

    public static ClientNettyEndpoint getInstance() {
        return clientNettyEndpoint;
    }

    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void onServerRegister(List<NodeWithWeight> registerServers) {

    }

    @Override
    public void onServerUnRegister(List<NodeWithWeight> removedServers) {

    }

    public void init() {
    }

    public void monitor() {
    }
}
