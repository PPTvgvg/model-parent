package com.kuake.cn.skyline.metric.es.config;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 描述：ES配置类
 *
 * @author: kuake.cn
 * @create: 2021-03-30 10:17
 **/
@Data
@ToString
@Builder
public class EsConfig {
    private String esAddress;
}
