package com.only.multids.core;


import com.only.multids.dynamicdatasource.DataSourceHolder;
import com.only.multids.exception.LoadRoutingStategyUnMatch;
import com.only.multids.exception.RoutingFiledArgsIsNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 多表多库策略
 */
@Slf4j
public class RoutingDsAndTbStrategy extends AbstractRouting {

    /**
     * 功能描述:   计算 库的 key值
     */
    @Override
    public String calDataSourceKey(String routingFieldValue, String routingField) throws LoadRoutingStategyUnMatch, RoutingFiledArgsIsNull {

        String dataSourceKey = null;
        //调用父类 AbstractRouting 计算 路由 的key值
        Integer routingFiledHashCode = getRoutingFileHashCode(routingFieldValue);

        //定位库的索引值
        Integer dsIndex = routingFiledHashCode % getDsRoutingSetProperties().getDataSourceNum();

        //根据库的索引值定位 数据源的key
        dataSourceKey = getDsRoutingSetProperties().getDataSourceKeysMapping().get(dsIndex);

        //放入线程变量
        DataSourceHolder.setdataSourceKey(dataSourceKey);

        log.info("根据路由字段:{},值:{},计算出数据库索引值:{},数据源key的值:{}", routingField, routingFieldValue, dsIndex, dataSourceKey);

        return dataSourceKey;
    }

    /**
     * 计算表的索引值
     *
     * @param routingFiled
     * @return
     */
    @Override
    public String calTableKey(String routingFiled) {

        Integer routingFiledHashCode = getRoutingFileHashCode(routingFiled);

        Integer tbIndex = routingFiledHashCode % getDsRoutingSetProperties().getTableNum();

        String tableSuffix = getFormatTableSuffix(tbIndex);

        DataSourceHolder.setTableIndex(tableSuffix);

        return tableSuffix;
    }


}
