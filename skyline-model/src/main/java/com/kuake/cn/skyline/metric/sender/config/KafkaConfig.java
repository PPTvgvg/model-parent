package com.kuake.cn.skyline.metric.sender.config;

import lombok.Data;

/**
 * 描述：Kafka生产者配置类
 * @author: kuake.cn
 * @create: 2021-03-29 13:21
 **/
@Data
public class KafkaConfig {
    private String broker;
    private String topic;
}
