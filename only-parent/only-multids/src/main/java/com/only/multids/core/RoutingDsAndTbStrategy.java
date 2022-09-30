package com.only.multids.core;


import com.only.multids.dynamicdatasource.MultiDataSourceHolder;
import com.only.multids.exception.LoadRoutingStategyUnMatch;
import com.only.multids.exception.RoutingFiledArgsIsNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 多表多库策略
 */
@Slf4j
public class RoutingDsAndTbStrategy extends AbstractTulingRouting {

    /**
     * 功能描述:   计算 库的 key值
     */
    @Override
    public String calDataSourceKey(String routingFieldValue, String routingField) throws LoadRoutingStategyUnMatch, RoutingFiledArgsIsNull {

        String dataSourceKey = null;
        //调用父类 AbstractTulingRouting 计算 路由 的key值
        Integer routingFiledHashCode = getRoutingFileHashCode(routingFieldValue);

        //定位库的索引值
        Integer dsIndex = routingFiledHashCode % getTulingDsRoutingSetProperties().getDataSourceNum();

        //根据库的索引值定位 数据源的key
        dataSourceKey = getTulingDsRoutingSetProperties().getDataSourceKeysMapping().get(dsIndex);

        //放入线程变量
        MultiDataSourceHolder.setdataSourceKey(dataSourceKey);

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

        Integer tbIndex = routingFiledHashCode % getTulingDsRoutingSetProperties().getTableNum();

        String tableSuffix = getFormatTableSuffix(tbIndex);

        MultiDataSourceHolder.setTableIndex(tableSuffix);

        return tableSuffix;
    }


}
