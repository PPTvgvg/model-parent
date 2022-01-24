package com.kuake.cn.skyline.metric.collector;

import com.kuake.cn.skyline.common.environment.EnvEnum;
import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.metric.collector.config.KafkaConfig;
import com.kuake.cn.skyline.metric.collector.kafka.KafkaWorker;
import com.kuake.cn.skyline.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 描述：
 *
 * @author: kuake.cn
 * @create: 2021-03-30 16:10
 **/
@Slf4j
public class MetricCollectorRunner {
    private static final KafkaConfig config = new KafkaConfig();
    private static KafkaWorker kafkaWorker;
    public static void main(String[] args) {
        loadEnv();
        kafkaWorker = new KafkaWorker(config);
        kafkaWorker.start();
        addHook();
    }

    private static void addHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook begin execute");
            kafkaWorker.shutdown();
            log.info("Shutdown hook execute finished");
        }));
    }

    private static void loadEnv() {
        String projectType = System.getProperty(Environment.PROJECT_TYPE);
        String env = System.getProperty(Environment.ENV);

        try {
            EnvUtils.loadEnvironment(projectType, EnvEnum.getInstanceByName(env).getPath());
        } catch (IOException e) {
            log.error("Error, while load environment", e);
        }

        config.setBroker(Environment.getMetricKafkaAddress());
        config.setGroup(Environment.getMetricKafkaGroup());
        config.setTopic(Environment.getMetricKafkaTopic());
    }
}
