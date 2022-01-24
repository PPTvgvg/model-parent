package com.kuake.cn.skyline.client.listen;

import com.kuake.cn.skyline.transport.endpoint.NodeWithWeight;

import java.util.List;

/**
 * 描述：
 *
 * @author: kuake.cn
 * @create: 2021-04-02 15:37
 **/
public interface ZkListener {
    public void onServerRegister(List<NodeWithWeight> registerServers);
    public void onServerUnRegister(List<NodeWithWeight> removedServers);
}
