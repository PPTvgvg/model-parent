package com.kuake.cn.skyline.metric.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.kuake.cn.skyline.metric.model.SkylineMetric;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * 描述：消息编码器
 * @author: kuake.cn
 * @create: 2021-03-29 13:53
 **/
public class JsonSerializer implements Serializer<SkylineMetric> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, SkylineMetric skylineMetric) {
        return JSON.toJSONBytes(skylineMetric);
    }

    @Override
    public void close() {

    }
}
