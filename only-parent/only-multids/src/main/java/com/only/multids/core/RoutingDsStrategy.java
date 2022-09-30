package com.only.multids.core;

import com.only.multids.dynamicdatasource.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;

/**
 * 多库 一表策略
 */
@Slf4j
public class RoutingDsStrategy extends AbstractRouting {

    private static final String ROUTING_DS_STATEGY_TBALESUFFIX = "_0000";

    @Override
    public String calDataSourceKey(String routingFiled, String routingField) {

        String dataSourceKey = null;

        Integer routingFiledHashCode = getRoutingFileHashCode(routingFiled);
        //定位库的索引值
        Integer dsIndex = routingFiledHashCode % getDsRoutingSetProperties().getDataSourceNum();

        //根据库的索引值定位 数据源的key
        dataSourceKey = getDsRoutingSetProperties().getDataSourceKeysMapping().get(dsIndex);

        //放入线程变量
        DataSourceHolder.setdataSourceKey(dataSourceKey);

        log.info("根据路由字段:{},值:{},计算出数据库索引值:{},数据源key的值:{}", routingField, routingFiled, dsIndex, dataSourceKey);
        return dataSourceKey;
    }

    @Override
    public String calTableKey(String routingFiled) {
        DataSourceHolder.setTableIndex(ROUTING_DS_STATEGY_TBALESUFFIX);
        return ROUTING_DS_STATEGY_TBALESUFFIX;
    }
}
