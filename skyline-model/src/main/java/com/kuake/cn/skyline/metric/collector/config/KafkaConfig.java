package com.kuake.cn.skyline.metric.collector.config;

import lombok.Data;

/**
 * 描述：Kafak消费者配置类
 *
 * @author: kuake.cn
 * @create: 2021-03-29 13:22
 **/
@Data
public class KafkaConfig {
    private String broker;
    private String topic;
    private String group;
}
