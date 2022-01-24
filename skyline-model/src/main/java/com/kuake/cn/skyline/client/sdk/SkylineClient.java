package com.kuake.cn.skyline.client.sdk;

import com.kuake.cn.skyline.client.endpoint.ClientNettyEndpoint;
import com.kuake.cn.skyline.common.environment.EnvEnum;
import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

/**
 * 描述：客户端
 *
 * @author: kuake.cn
 * @create: 2021-04-02 13:25
 **/
@Slf4j
public class SkylineClient {
    private ClientNettyEndpoint endpoint = null;

    //最多多少个缓存
    private Integer maxCacheFuture;

    //zookeeper相关（最好不要修改，用默认配置）
    private String zkAddress;

    private int retry = 0;

    private long timeout = 3000L;

    public SkylineClient(Integer maxCacheFuture, String zkAddress, int retry, long timeout) {
        this.maxCacheFuture = maxCacheFuture;
        this.zkAddress = zkAddress;
        this.retry = retry;
        this.timeout = timeout;

        this.initialize();
    }

    private void initialize() {
        initEnv();
        endpoint = ClientNettyEndpoint.getInstance();
        if (!endpoint.isInitialized()) {
            if (ObjectUtils.isNotEmpty(maxCacheFuture) && maxCacheFuture >= 100) {
                Environment.setConfig(Environment.MAX_CACHE_FUTURE, maxCacheFuture);
            }
            if (ObjectUtils.isNotEmpty(zkAddress)) {
                Environment.setConfig(Environment.REGISTER_ZOOKEEPER_ADDRESS, zkAddress);
            }

            endpoint.init();
            endpoint.monitor();
        }
    }

    private void initEnv() {
        String env = System.getProperty(Environment.ENV);
        String projectType = System.getProperty(Environment.PROJECT_TYPE);

        EnvEnum envEnum = EnvEnum.getInstanceByName(env);
        try {
            EnvUtils.loadEnvironment(projectType, envEnum.getPath());
        } catch (IOException e) {
            log.error("Environment load failed.", e);
        }
    }
}
