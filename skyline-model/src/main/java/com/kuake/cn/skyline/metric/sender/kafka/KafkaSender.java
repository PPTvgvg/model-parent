package com.kuake.cn.skyline.metric.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.kuake.cn.skyline.metric.model.SkylineMetric;
import com.kuake.cn.skyline.metric.sender.config.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 描述：Kafka生产者
 *
 * @author: kuake.cn
 * @create: 2021-03-29 14:04
 **/
@Slf4j
public class KafkaSender {
    private static final String KEY_SERIALIZER = StringSerializer.class.getName();
    private static final String VALUE_SERIALIZER = JsonSerializer.class.getName();

    private KafkaProducer<String, SkylineMetric> producer;

    private volatile boolean initialized = false;

    public void init(KafkaConfig config) {
        try {
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBroker());
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);
            properties.put(ProducerConfig.RETRIES_CONFIG, 1);
            properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024);
            properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
            properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
            properties.put(ProducerConfig.ACKS_CONFIG, "all");
            properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 20000);

            producer = new KafkaProducer<>(properties);
            initialized = true;

        }catch (Exception e) {
            log.error("Error Occurred while init kafka, kafka config:{}", config, e);
            if (ObjectUtils.isNotEmpty(producer)) {
                producer.close();
            }
        }
    }

    public boolean isInitialized() {return initialized;}

    public void send(SkylineMetric metric) {
        ProducerRecord<String, SkylineMetric> producerRecord = new ProducerRecord<>(metric.getTopic(), metric);
        producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (ObjectUtils.isNotEmpty(e)) {
                    log.error("send metrics error. data:{}", JSON.toJSONString(metric));
                }
            }
        });
    }

    public void close() {
        if (ObjectUtils.isNotEmpty(producer)) {
            producer.close();
        }
    }
}
