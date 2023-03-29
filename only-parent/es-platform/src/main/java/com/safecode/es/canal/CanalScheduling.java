package com.safecode.es.canal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.Message;
import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;
import com.safecode.es.mapper.ReadBookPdMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CanalScheduling implements Runnable {

    @Autowired
    private CanalConnector canalConnector;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ReadBookPdMapper readBookPdMapper;

    // 每隔10毫秒 从canal中获取一次数据，
    @Override
    @Scheduled(fixedDelay = 100)
    public void run() {
        long batchId = -1;

        try {
            // 一次取1000条数据
            int batchSize = 1000;
            Message message = canalConnector.getWithoutAck(batchSize);
            batchId = message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            if (batchId != -1 && entries.size() > 0) {
                entries.forEach(entry -> {
                    if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                        // 解析处理
                        publishCanalEvent(entry);
                    }
                });
            }
            // 提交确认消费完毕
            canalConnector.ack(batchId);
        } catch (Exception e) {
            e.printStackTrace();
            // 失败的话进行回滚
            canalConnector.rollback(batchId);
        }

    }

    private void publishCanalEvent(Entry entry) {
        log.info("收到canal消息:{}", entry.toString());

        if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
            return;
        }
        String database = entry.getHeader().getSchemaName();
        String table = entry.getHeader().getTableName();
        log.info("监听的数据库:{},变更的数据表:{}", database, table);

        CanalEntry.RowChange change = null;
        try {
            change = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return;
        }

        EventType eventType = change.getEventType();

        change.getRowDatasList().forEach(rowData -> {
            List<CanalEntry.Column> columns = null;
            // 对于es来说只要关注delete,update,insert
            if (eventType == EventType.DELETE) {
                columns = rowData.getBeforeColumnsList();
            } else {
                columns = rowData.getAfterColumnsList();
            }
            // 解析成map格式
            Map<String, Object> dataMap = parseColumsToMap(columns);
            // 真正的去改ES
            try {
                indexEs(dataMap, database, table, eventType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private Map<String, Object> parseColumsToMap(List<CanalEntry.Column> columns) {
        HashMap<String, Object> map = Maps.newHashMap();
        columns.forEach(column -> {
            if (column == null) {
                return;
            }
            map.put(column.getName(), column.getValue());
        });
        return map;
    }

    private void indexEs(Map<String, Object> dataMap, String database, String table, EventType eventType) {
        try {
            if (eventType == EventType.DELETE) {
                log.info("删除索引Id={},type={},value={}", dataMap.get("id"), eventType.toString(), dataMap.toString());
                DeleteRequest deleteRequest = new DeleteRequest("book", "_doc", String.valueOf(dataMap.get("id")));
                restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            } else {
                // 这里有两种i方式，一种是直接拿canal过来的数据，还有一种是拿主键id去查询
                List<Map<String, Object>> result = readBookPdMapper
                        .buildESQuery(new Integer((String) dataMap.get("id")));
                for (Map<String, Object> map : result) {
                    log.info("更新索引ID={},type={},value={}", map.get("id"), eventType.toString(), map.toString());
                    IndexRequest indexRequest = new IndexRequest("book", "_doc");
                    indexRequest.id(String.valueOf(map.get("id")));
                    indexRequest.source(map);
                    restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
                    // 处理多业务的思路
                    // 1.监听的是商品表 变更时我会拿到商品id
                    // 2.根据goodsId 去营销中心 订单中心 查询数据 会调用他们的接口
                    // 3.组装数据 进入es
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
