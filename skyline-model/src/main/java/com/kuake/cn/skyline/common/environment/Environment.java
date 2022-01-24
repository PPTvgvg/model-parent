package com.kuake.cn.skyline.common.environment;

import com.kuake.cn.skyline.utils.IPUtils;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：运行环境配置类
 * @author: kuake.cn
 * @create: 2021-03-26 21:32
 **/
public class Environment {
    public static final String PROJECT_TYPE = "project.type";
    public static final String RPC_PORT = "rpc.port";
    public static final String SERVER_PORT = "server.port";
    public static final String DUBBO_ADDRESS = "dubbo.address";

    public static final String REGISTER_ZOOKEEPER_ADDRESS = "register.zookeeper.address";
    public static final String REGISTER_ZOOKEEPER_RETRY = "register.zookeeper.retry";
    public static final String REGISTER_ZOOKEEPER_TIMEOUT = "register.zookeeper.timeout";
    public static final String REGISTER_ZOOKEEPER_SLEEP = "register.zookeeper.sleep";
    public static final String MAX_CACHE_FUTURE = "future.cache.max";

    public static final String ENV = "env";

    public static final String METRIC_KAFKA_ADDRESS = "metric.kafka.address";
    public static final String METRIC_KAFKA_TOPIC = "metric.kafka.topic";
    public static final String METRIC_ES_ADDRESS = "metric.es.address";
    public static final String METRIC_KAFKA_GROUP = "metric.kafka.group";

    private static final String DEFAULT_IP = "localhost";
    private static final String DEFAULT_DUBBO_ADDRESS = "1.15.140.133:2181";
    private static final Integer DEFAULT_RPC_PORT = 10086;
    private static final String DEFAULT_METRIC_KAFKA_ADDRESS = "1.15.140.133:9092";
    private static final String DEFAULT_METRIC_KAFKA_TOPIC = "skyline_metric";
    private static final String DEFAULT_METRIC_KAFKA_GROUP = "kafka_to_es";
    private static final String DEFAULT_METRIC_ES_ADDRESS = "1.15.140.133:9200";
    private static final String DEFAULT_REGISTER_ZOOKEEPER_ADDRESS = "1.15.140.133:2181";
    private static final Integer DEFAULT_REGISTER_ZOOKEEPER_RETRY = 5;
    private static final Integer DEFAULT_REGISTER_ZOOKEEPER_TIMEOUT = 30000;
    private static final Integer DEFAULT_REGISTER_ZOOKEEPER_SLEEP = 1000;
    private static final Integer DEFAULT_MAX_CACHE_FUTURE = 100;

    private static final Map<String, Object> configs = new HashMap<>();

    public static void setConfig(String key, Object value) {
        configs.put(key, value);
    }

    public static String getDubboAddress() {
        return (String) (configs.get(DUBBO_ADDRESS) == null ?
                DEFAULT_DUBBO_ADDRESS : configs.get(DUBBO_ADDRESS));
    }

    public static Integer getRpcPort() {
        return (Integer) (configs.get(RPC_PORT) == null ?
                DEFAULT_RPC_PORT : configs.get(RPC_PORT));
    }

    public static String getRegisterZookeeperAddress() {
        return (String) (configs.get(REGISTER_ZOOKEEPER_ADDRESS) == null ?
                DEFAULT_REGISTER_ZOOKEEPER_ADDRESS : configs.get(REGISTER_ZOOKEEPER_ADDRESS));
    }

    public static Integer getRegisterZookeeperRetry() {
        return (Integer) (configs.get(REGISTER_ZOOKEEPER_RETRY) == null ?
                DEFAULT_REGISTER_ZOOKEEPER_RETRY : configs.get(REGISTER_ZOOKEEPER_RETRY));
    }

    public static Integer getRegisterZookeeperTimeout() {
        return (Integer) (configs.get(REGISTER_ZOOKEEPER_TIMEOUT) == null ?
                DEFAULT_REGISTER_ZOOKEEPER_TIMEOUT : configs.get(REGISTER_ZOOKEEPER_TIMEOUT));
    }

    public static Integer getRegisterZookeeperSleep() {
        return (Integer) (configs.get(REGISTER_ZOOKEEPER_SLEEP) == null ?
                DEFAULT_REGISTER_ZOOKEEPER_SLEEP : configs.get(REGISTER_ZOOKEEPER_SLEEP));
    }

    public static String getMetricKafkaAddress() {
        return (String) (configs.get(METRIC_KAFKA_ADDRESS) == null ?
                DEFAULT_METRIC_KAFKA_ADDRESS : configs.get(METRIC_KAFKA_ADDRESS));
    }

    public static String getMetricKafkaTopic() {
        return (String) (configs.get(METRIC_KAFKA_TOPIC) == null ?
                DEFAULT_METRIC_KAFKA_TOPIC : configs.get(METRIC_KAFKA_TOPIC));
    }

    public static String getMetricEsAddress() {
        return (String) (configs.get(METRIC_ES_ADDRESS) == null ?
                DEFAULT_METRIC_ES_ADDRESS : configs.get(METRIC_ES_ADDRESS));
    }

    public static String getMetricKafkaGroup() {
        return (String) (configs.get(METRIC_KAFKA_GROUP) == null ?
                DEFAULT_METRIC_KAFKA_GROUP : configs.get(METRIC_KAFKA_GROUP));
    }

    public static Integer getMaxCacheFuture() {
        return (Integer) (configs.get(MAX_CACHE_FUTURE) == null ?
                DEFAULT_MAX_CACHE_FUTURE : configs.get(MAX_CACHE_FUTURE));
    }

    public static String getServerIp() {
        String host = null;
        try {
            host = IPUtils.getHost();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return host == null ? DEFAULT_IP : host;
    }

}
