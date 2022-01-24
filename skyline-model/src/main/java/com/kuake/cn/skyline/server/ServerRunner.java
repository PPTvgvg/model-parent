package com.kuake.cn.skyline.server;

import com.kuake.cn.skyline.common.environment.EnvEnum;
import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.metric.model.SkylineServerMetric;
import com.kuake.cn.skyline.metric.sender.utils.MetricSenderHelper;
import com.kuake.cn.skyline.server.server.service.*;
import com.kuake.cn.skyline.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 描述：Server启动类
 * @author: kuake.cn
 * @create: 2021-03-26 15:32
 **/
@Slf4j
public class ServerRunner implements Runnable {
    private EndpointService endpointService;
    private HighAvailableService highAvailableService;

    public static void main(String[] args) {
        ServerRunner serverRunner = new ServerRunner();
        serverRunner.initEnvironments();
        CuratorClientHolder.ensureStart();
        serverRunner.start();
        serverRunner.addHook();
    }

    private void start() {
        Thread thread = new Thread(this, "thread-skyline-server-app");
        thread.setDaemon(false);
        thread.start();
    }

    private void startService() {
        endpointService = new NettyEndpointService();
        boolean isStarted = endpointService.startRpcServer();
        if (!isStarted) {
            //server 启动失败，程序退出
            System.exit(-1);
        }

        //收集启动信息
        registerMetadata();

        try {
            highAvailableService = new ZookeeperHighAvailableService().register().listen();
        } catch (Exception e) {
            log.error("Error Occurred while register to zk, will exit program.", e);
            System.exit(-1);
        }
        log.info("JVM running for Skyline server");
        log.info("Start ");

    }

    private void registerMetadata() {
        MetricSenderHelper.sendToKafka(SkylineServerMetric.builder()
                .ip(Environment.getServerIp())
                .port(Environment.getRpcPort())
                .build());
    }

    private void initEnvironments() {
        String env = System.getProperty(Environment.ENV);
        String projectType = System.getProperty(Environment.PROJECT_TYPE);

        EnvEnum envEnum = EnvEnum.getInstanceByName(env);
        try {
            EnvUtils.loadEnvironment(projectType, envEnum.getPath());
        } catch (IOException e) {
            log.error("Error while load Environment:{}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void addHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            log.info("shutdown hook begin execute");
            endpointService.shutdown();
            try {
                highAvailableService.shown();
            } catch (Exception e) {
//                e.printStackTrace();
            }
            log.info("shutdown hook execute finished");
        }, "thread-skyline-server-shutdown-hook"));
    }

    @Override
    public void run() {
        startService();
    }
}
