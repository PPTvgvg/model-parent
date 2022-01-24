package com.kuake.cn.skyline.server.server.service;

import com.kuake.cn.skyline.common.environment.Environment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 描述：zk客户端
 *
 * @author: kuake.cn
 * @create: 2021-03-29 21:36
 **/
@Slf4j
public class CuratorClientHolder {
    private static final CuratorFramework client;
    private static volatile boolean isStarted = false;

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(Environment.getRegisterZookeeperSleep(), Environment.getRegisterZookeeperRetry());
        client = CuratorFrameworkFactory.builder()
                .connectString(Environment.getRegisterZookeeperAddress())
                .sessionTimeoutMs(Environment.getRegisterZookeeperTimeout())
                .connectionTimeoutMs(50_000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        if (!ObjectUtils.notEqual(CuratorFrameworkState.STARTED, client.getState())) {
            isStarted = true;
        }
    }

    public static CuratorFramework getCuratorFramework() {return client;}

    public static void ensureStart() {
        int time = 0;
        while (!isStarted) {
            try {
                if (time > 0) {
                    break;
                }
                Thread.sleep(30);
            }catch (Exception e) {
                //ignore
            }finally {
                time ++;
            }
        }

        if (!isStarted) {
            log.warn("zookeeper is not start.");
            System.exit(-1);
        }
    }
}
