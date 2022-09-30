package com.only.multids.core;

import com.only.multids.exception.FormatTableSuffixException;
import com.only.multids.exception.LoadRoutingStategyUnMatch;
import com.only.multids.exception.RoutingFiledArgsIsNull;

/**
 * 路由接口
 * 策略模式接口
 */
public interface IRouting {

    /**
     * 根据规则计算出到底是哪个数据源
     *
     * @param routingFieldValue 参数值  routingField      参数即路由字段
     * @return
     */
    String calDataSourceKey(String routingFieldValue, String routingField) throws LoadRoutingStategyUnMatch, RoutingFiledArgsIsNull;


    /**
     * 计算routingFiled字段的 hashcode值
     *
     * @param routingFiled
     * @return
     */
    Integer getRoutingFileHashCode(String routingFiled);

    /**
     * 计算一个库所在表的索引值
     *
     * @param routingFiled
     * @return
     */
    String calTableKey(String routingFiled) throws LoadRoutingStategyUnMatch, RoutingFiledArgsIsNull;

    /**
     * 计算出表的后缀
     *
     * @param tableIndex
     * @return
     * @throws FormatTableSuffixException
     */
    String getFormatTableSuffix(Integer tableIndex) throws FormatTableSuffixException;
}
