package com.kuake.cn.skyline.server.server.service;

/**
 * 描述：高可用服务接口
 * @author: kuake.cn
 * @create: 2021-03-26 16:11
 **/
public interface HighAvailableService {
    HighAvailableService register() throws Exception;

    HighAvailableService listen() throws Exception;

    void shown() throws Exception;
}
