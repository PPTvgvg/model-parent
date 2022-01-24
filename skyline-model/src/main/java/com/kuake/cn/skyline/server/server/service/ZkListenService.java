package com.kuake.cn.skyline.server.server.service;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：
 *
 * @author: kuake.cn
 * @create: 2021-03-29 21:42
 **/
@Slf4j
public class ZkListenService {
    private static final ZkListenService zkListenService = new ZkListenService();

    public static ZkListenService getInstance() {
        return zkListenService;
    }

    public void listen() {

    }
}
