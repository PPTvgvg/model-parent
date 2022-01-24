package com.kuake.cn.skyline.metric.collector.kafka;

import com.alibaba.fastjson.JSONObject;
import com.kuake.cn.skyline.metric.model.SkylineMetric;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * 描述：消息解码器
 *
 * @author: kuake.cn
 * @create: 2021-03-30 15:05
 **/
public class JsonDeserializer implements Deserializer<SkylineMetric> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public SkylineMetric deserialize(String s, byte[] bytes) {
        return JSONObject.parseObject(new String(bytes), SkylineMetric.class);
    }

    @Override
    public void close() {

    }
}
