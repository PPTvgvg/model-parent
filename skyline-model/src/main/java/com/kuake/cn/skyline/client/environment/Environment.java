package com.kuake.cn.skyline.client.environment;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：
 *
 * @author: chenliqiang001
 * @create: 2021-03-24
 **/
public class Environment {
    public static final String ZK_REGISTER_ADDRESS = "register.zookeeper.address";
    public static final String ZK_REGISTER_RETRY = "register.zookeeper.retry";
    public static final String ZK_REGISTER_TIMEOUT = "register.zookeeper.timeout";
    public static final String ZK_REGISTER_SLEEP = "register.zookeeper.sleep";

    public static final String DEFAULT_ZK_REGISTER_ADDRESS = "127.0.0.1:2181";
    public static final Integer DEFAULT_ZK_REGISTER_RETRY = 5;
    public static final Integer DEFAULT_ZK_REGISTER_TIMEOUT = 30000;
    public static final Integer DEFAULT_ZK_REGISTER_SLEEP = 1000;
    public static final Integer DEFAULT_MAX_CACHE_FUTURE_COUNT = 10 * 1000 * 1000;

    public static final Map<String, Object> CONFIGS = new HashMap<>();
}
