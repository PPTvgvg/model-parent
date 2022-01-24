package com.kuake.cn.skyline.metric.es.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.metric.es.config.EsConfig;
import com.kuake.cn.skyline.metric.model.SkylineMetric;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 描述：ES代理器
 *
 * @author: kuake.cn
 * @create: 2021-03-30 10:41
 **/
@Slf4j
public class ElasticSearchHelper {
    private static final RestHighLevelClient restHighLevelClient;

    private static final String indexTemplete = "skyline-metric-%s";
    private static final DateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

    static {
        EsConfig config = EsConfig.builder().build();
        try {
            config.setEsAddress(Environment.getMetricEsAddress());
        }catch (Exception e) {
            log.error("Error, init Metric ES failed.", e);
        }

        HttpHost[] httpHosts = Arrays.stream(config.getEsAddress().split(",")).map(k -> {
            String[] hostAndPort = k.split(":");
            return new HttpHost(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
        }).toArray(HttpHost[]::new);

        restHighLevelClient = new RestHighLevelClient(RestClient.builder(httpHosts)
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(10_000)
                            .setSocketTimeout(30_000)
                            .setAuthenticationEnabled(true);
                    return requestConfigBuilder;
                }));
    }

    public static void sendBulk(List<SkylineMetric> skylineMetrics) {
        if (skylineMetrics.size() == 0) {
            return;
        }

        //ES 建议一次发送数据小于20M
        if (skylineMetrics.size() > 5000) {
            List<List<SkylineMetric>> partition = Lists.partition(skylineMetrics, 5000);
            partition.forEach(ElasticSearchHelper::sendBulk);
        }else {
            sendBulkInternal(skylineMetrics);
        }
    }

    private static void sendBulkInternal(List<SkylineMetric> skylineMetrics) {
        String dateString = sdf.format(new Date());

        BulkRequest bulkRequest = new BulkRequest();

        skylineMetrics.forEach(metric -> {
            IndexRequest indexRequest = new IndexRequest(String.format(indexTemplete, dateString))
                    .source(JSON.toJSONBytes(metric), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });

        //同步请求，防止poll kafka消息过快
        try {
            BulkResponse responses = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (responses.hasFailures()) {
                log.error("Error Occurred while bulk to ES:{}.", responses.buildFailureMessage());
            }
        } catch (IOException e) {
            log.error("Error while flush metric record to elastic search.", e);
        }
    }
}
