package com.safecode.es.canal;

import java.net.InetSocketAddress;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.google.common.collect.Lists;

@Component
public class CanalClient implements DisposableBean {

    private CanalConnector canalConnector;

    // http的形式接入es
    @Bean("restHighLevelClient")
    public RestHighLevelClient restHighLevelClient() {
        HttpHost httpHost = new HttpHost("192.168.50.28", 19200);
        return new RestHighLevelClient(RestClient.builder(new HttpHost[]{httpHost}));
    }

    // 连接canal服务
    @Bean
    public CanalConnector canalConnector() {
        /*
         * 即使我们使用的是集群，其实同时也只有一个canal工作，只有当他宕机了，才会启用其他的，所以就是一个备份
         */
        canalConnector = CanalConnectors.newClusterConnector(
                Lists.newArrayList(new InetSocketAddress("192.168.50.28", 11111)), "book", "canal", "canal");
        canalConnector.connect();

        // 指定filter,格式{database}.{table}
        /*
         * 指定我们要监听的表 如果监听的表很多，最好使用多线程，每一个canal服务只处理几张表就行了
         */
        // canalConnector.subscribe("*");
        canalConnector.subscribe("test.read_book_pd");
        // 回滚寻找上次中断的为止
        canalConnector.rollback();
        return canalConnector;
    }

    @Override
    public void destroy() throws Exception {
        if (canalConnector != null) {
            canalConnector.disconnect();
        }

    }

}
