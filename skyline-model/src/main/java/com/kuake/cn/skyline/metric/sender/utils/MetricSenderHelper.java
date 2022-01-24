package com.kuake.cn.skyline.metric.sender.utils;

import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.metric.model.SkylineMetric;
import com.kuake.cn.skyline.metric.sender.config.KafkaConfig;
import com.kuake.cn.skyline.metric.sender.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述：消息发送代理类
 *
 * @author: kuake.cn
 * @create: 2021-03-29 16:41
 **/
@Slf4j
public class MetricSenderHelper {
    private static final KafkaSender kafkaSender;

    static {
        kafkaSender = new KafkaSender();
        if (!kafkaSender.isInitialized()) {
            KafkaConfig kafkaConfig = new KafkaConfig();
            try {
                loadKafkaConfig(kafkaConfig);
                kafkaSender.init(kafkaConfig);
            }catch (Exception e) {
                log.error("Error, init metric sender failed", e);
            }
        }
    }

    private static void loadKafkaConfig(KafkaConfig kafkaConfig) {
        kafkaConfig.setBroker(Environment.getMetricKafkaAddress());
        kafkaConfig.setTopic(Environment.getMetricKafkaTopic());
    }

    /**
     * 发送Metric到kafka
     * @param: skylineMetric {@link SkylineMetric}
     * @return: void
     **/
    public static void sendToKafka(SkylineMetric skylineMetric) {
        kafkaSender.send(skylineMetric);
    }
}
