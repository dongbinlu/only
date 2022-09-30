package com.only.multids.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Title: 多个数据源映射
 */
@ConfigurationProperties(prefix = "spring.datasource")     //配置文件的信息，读取并自动封装成实体类
@Data
public class DruidProperties {

    private String druid00username;

    private String druid00passwrod;

    private String druid00jdbcUrl;

    private String druid00driverClass;

    private String druid01username;

    private String druid01passwrod;

    private String druid01jdbcUrl;

    private String druid01driverClass;

    private String druid02username;

    private String druid02passwrod;

    private String druid02jdbcUrl;

    private String druid02driverClass;
}
