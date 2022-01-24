package com.kuake.cn.skyline.server.server.service;

/**
 * 描述：zk高可用服务接口
 * @author: kuake.cn
 * @create: 2021-03-26 17:23
 **/
public class ZookeeperHighAvailableService implements HighAvailableService {
    @Override
    public HighAvailableService register() throws Exception {
        ZkRegisterService.getInstance().register();
        return this;
    }

    @Override
    public HighAvailableService listen() throws Exception {
        ZkListenService.getInstance().listen();
        return this;
    }

    @Override
    public void shown() throws Exception {
        ZkRegisterService.getInstance().shutdown();
    }
}
