package com.kuake.cn.skyline.metric.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述：
 *
 * @author: kuake.cn
 * @create: 2021-03-28 22:43
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@ToString
public class SkylineServerMetric extends SkylineMetric {
    private final static String topic = "skyline-metric";

    private String from = "server";

    private String ip;

    private Integer port;

    private String version = "v1.0.0";

    public SkylineServerMetric() {}

    public SkylineServerMetric(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getTopic() {
        return topic;
    }
}
