package com.safecode.es.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safecode.es.config.ElasticSearchManager;
import com.safecode.es.service.ElasticService;

import lombok.extern.slf4j.Slf4j;

@Service("elasticService")
@Slf4j
public class ElasticServiceImpl implements ElasticService {

    @Autowired
    private ElasticSearchManager elasticSearchManager;

    @Override
    public Boolean add(Map<String, Object> doc, String index, String type) throws Exception {
        if (doc != null) {
            try {
                XContentBuilder json = XContentFactory.jsonBuilder().startObject();
                Iterator<String> iterator = doc.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    Object object = doc.get(key);
                    if (object instanceof Integer)
                        json.field(key, Integer.valueOf(object.toString()));
                    else if (object instanceof Long)
                        json.field(key, Long.valueOf(object.toString()));
                    else if (object instanceof String)
                        json.field(key, object.toString());
                    else
                        json.field(key, object);
                }
                json.endObject();
                elasticSearchManager.client.prepareIndex(index, type).setSource(json).get();
                return true;
            } catch (Exception e) {
                log.error("数据插入es失败,index={},type={}", index, type, e);
            }
        }
        return false;
    }

    @Override
    public Boolean adds(List<Map<String, Object>> docs) {
        // TODO
        return null;
    }

    @Override
    public Boolean addBulkIn(List<Map<String, Object>> datas, String index, String type) {
        try {
            BulkRequestBuilder bulkRequest = elasticSearchManager.client.prepareBulk();
            for (Map<String, Object> data : datas) {
                bulkRequest.add(elasticSearchManager.client.prepareIndex(index, type, data.get("bookId").toString())
                        .setSource(data));
            }
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            System.out.println(bulkResponse.buildFailureMessage());
            return true;
        } catch (Exception e) {
            log.error("数据插入es失败,index={},type={}", index, type, e);
        }
        return false;
    }

    @Override
    public Boolean delete(String index) {
        DeleteRequest request = new DeleteRequest(index);
        elasticSearchManager.client.delete(request);
        return null;
    }

}
