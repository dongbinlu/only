package com.only.multids.core;

import com.only.multids.constant.TulingConstant;
import com.only.multids.enumuration.MultiDsErrorEnum;
import com.only.multids.exception.FormatTableSuffixException;
import com.only.multids.exception.LoadRoutingStategyUnMatch;
import com.only.multids.support.TulingDsRoutingSetProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


/**
 * 路由规则抽象类
 * 策略模式
 * 并加入检查配置路由参数和 策略是否相匹配
 *
 * @EnableConfigurationProperties 这个注解可以提供一种方便的方式来将带有@ConfigurationProperties注解的类注
 * 入为Spring容器的Bean。，然后用 Spring的@Autowire来注入 即将 TulingDsRoutingSetProperties注入容器
 */
@Slf4j
@EnableConfigurationProperties(value = {TulingDsRoutingSetProperties.class})
@Data
public abstract class AbstractTulingRouting implements ITulingRouting, InitializingBean {

    @Autowired
    private TulingDsRoutingSetProperties tulingDsRoutingSetProperties;


    /**
     * 获取路由key的hash值   这里直接采用的hashcode
     *
     * @param routingFiled 路由key
     * @return
     */
    @Override
    public Integer getRoutingFileHashCode(String routingFiled) {

        return Math.abs(routingFiled.hashCode());
    }

    /**
     * 获取表的后缀：_0000,_0001,_0002
     *
     * @param tableIndex 表的索引值
     * @return
     */
    @Override
    public String getFormatTableSuffix(Integer tableIndex) {
        StringBuffer stringBuffer = new StringBuffer(tulingDsRoutingSetProperties.getTableSuffixConnect());

        try {
            stringBuffer.append(String.format(getTulingDsRoutingSetProperties().getTableSuffixStyle(), tableIndex));
        } catch (Exception e) {
            log.error("格式化表后缀异常:{}", getTulingDsRoutingSetProperties().getTableSuffixStyle());
            throw new FormatTableSuffixException(MultiDsErrorEnum.FORMAT_TABLE_SUFFIX_ERROR);
        }
        return stringBuffer.toString();
    }


    /**
     * 工程在启动的时候 检查配置路由参数和 策略是否相匹配   因为继承了 InitializingBean
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws LoadRoutingStategyUnMatch {

        switch (getTulingDsRoutingSetProperties().getRoutingStategy()) {
            case TulingConstant.ROUTING_DS_TABLE_STATEGY:
                checkRoutingDsTableStategyConfig();
                break;
            case TulingConstant.ROUTGING_DS_STATEGY:
                checkRoutingDsStategyConfig();
                break;
            case TulingConstant.ROUTGIN_TABLE_STATEGY:
                checkRoutingTableStategyConfig();
                break;
        }
    }

    /**
     * 检查多库多表配置
     */
    private void checkRoutingDsTableStategyConfig() {
        if (tulingDsRoutingSetProperties.getTableNum() <= 1 || tulingDsRoutingSetProperties.getDataSourceNum() <= 1) {
            log.error("你的配置项routingStategy:{}是多库多表配置,数据库个数>1," +
                            "每一个库中表的个数必须>1,您的配置:数据库个数:{},表的个数:{}", tulingDsRoutingSetProperties.getRoutingStategy(),
                    tulingDsRoutingSetProperties.getDataSourceNum(), tulingDsRoutingSetProperties.getTableNum());
            throw new LoadRoutingStategyUnMatch(MultiDsErrorEnum.LOADING_STATEGY_UN_MATCH);
        }
    }

    /**
     * 检查多库一表的路由配置项
     */
    private void checkRoutingDsStategyConfig() {
        if (tulingDsRoutingSetProperties.getTableNum() != 1 || tulingDsRoutingSetProperties.getDataSourceNum() <= 1) {
            log.error("你的配置项routingStategy:{}是多库一表配置,数据库个数>1," +
                            "每一个库中表的个数必须=1,您的配置:数据库个数:{},表的个数:{}", tulingDsRoutingSetProperties.getRoutingStategy(),
                    tulingDsRoutingSetProperties.getDataSourceNum(), tulingDsRoutingSetProperties.getTableNum());
            throw new LoadRoutingStategyUnMatch(MultiDsErrorEnum.LOADING_STATEGY_UN_MATCH);
        }
    }

    /**
     * 检查一库多表的路由配置项
     */
    private void checkRoutingTableStategyConfig() {
        if (tulingDsRoutingSetProperties.getTableNum() <= 1 || tulingDsRoutingSetProperties.getDataSourceNum() != 1) {
            log.error("你的配置项routingStategy:{}是一库多表配置,数据库个数=1," +
                            "每一个库中表的个数必须>1,您的配置:数据库个数:{},表的个数:{}", tulingDsRoutingSetProperties.getRoutingStategy(),
                    tulingDsRoutingSetProperties.getDataSourceNum(), tulingDsRoutingSetProperties.getTableNum());
            throw new LoadRoutingStategyUnMatch(MultiDsErrorEnum.LOADING_STATEGY_UN_MATCH);
        }
    }

}
