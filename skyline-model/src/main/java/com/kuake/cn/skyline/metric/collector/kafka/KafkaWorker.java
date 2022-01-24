package com.kuake.cn.skyline.metric.collector.kafka;

import com.google.common.collect.Lists;
import com.kuake.cn.skyline.metric.collector.config.KafkaConfig;
import com.kuake.cn.skyline.metric.es.utils.ElasticSearchHelper;
import com.kuake.cn.skyline.metric.model.SkylineMetric;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 描述：kafka消费者
 *
 * @author: kuake.cn
 * @create: 2021-03-30 15:19
 **/
@Slf4j
public class KafkaWorker {
    private KafkaConsumer<String, SkylineMetric> kafkaConsumer;

    private KafkaConfig config;

    private final AtomicBoolean isClosed = new AtomicBoolean(false);

    private ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("skyline-metric-kafka-worker");
        return thread;
    });

    public KafkaWorker(KafkaConfig config) {
        this.config = config;
        init(config);
    }

    private void init(KafkaConfig config) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBroker());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDecoder.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3000);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, config.getGroup());

        kafkaConsumer = new KafkaConsumer<>(properties);
        log.info("kafka consumer initialized success at group:{}", config.getGroup());
    }

    public void start() {
        executorService.execute(() -> {
            while (!isClosed.get()) {
                try {
                    kafkaConsumer.subscribe(Collections.singleton(config.getTopic()));
                    ConsumerRecords<String, SkylineMetric> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(30));
                    List<SkylineMetric> metrics = Lists.newArrayList();
                    consumerRecords.forEach(record -> {
                        metrics.add(record.value());
                    });

                    ElasticSearchHelper.sendBulk(metrics);
                }catch (Exception e) {
                    if (!isClosed.get()) {
                        log.error("kafka fetch record:{} error.", e);
                    }
                }
            }
        });
    }

    public void shutdown() {
        log.info("Kafka fetch shutdown, kafka config:{}", config);
        isClosed.set(true);
        if (ObjectUtils.isNotEmpty(kafkaConsumer)) {
            kafkaConsumer.wakeup();
            kafkaConsumer.close();
        }
    }
}
