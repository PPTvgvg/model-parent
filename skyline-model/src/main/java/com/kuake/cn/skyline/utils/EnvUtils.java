package com.kuake.cn.skyline.utils;

import com.kuake.cn.skyline.common.environment.Environment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 描述：运行环境配置工具类
 * @author: kuake.cn
 * @create: 2021-03-28 15:13
 **/
@Slf4j
public class EnvUtils {
    public static void loadEnvironment(String projectType, String path) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(projectType + "/" + path);
        Properties properties = new Properties();
        properties.load(inputStream);
        properties.forEach((K, V)-> Environment.setConfig((String) K, V));

        String rpcPort = System.getProperty(Environment.SERVER_PORT);
        if (ObjectUtils.isNotEmpty(rpcPort)) {
            log.info("server.port:{}", rpcPort);
            Environment.setConfig(Environment.RPC_PORT, rpcPort);
        }
    }
}
