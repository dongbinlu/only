package com.only.multids.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.only.multids.dynamicdatasource.MultiDataSource;
import com.only.multids.properties.DruidProperties;
import com.only.multids.properties.DsRoutingSetProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: 配置多个数据源
 */
@Slf4j
@Configuration
//  @EnableConfigurationProperties注解的作用是：使使用 @ConfigurationProperties 注解的类生效。
@EnableConfigurationProperties({DsRoutingSetProperties.class, DruidProperties.class})
@MapperScan(basePackages = "com.only.multids.busi.mapper")
public class DataSourceConfiguration {

    @Autowired
    private DsRoutingSetProperties dsRoutingSetProperties;

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid00")
    public DataSource dataSource00() {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(druidProperties.getDruid00username());
        dataSource.setPassword(druidProperties.getDruid00passwrod());
        dataSource.setUrl(druidProperties.getDruid00jdbcUrl());
        dataSource.setDriverClassName(druidProperties.getDruid00driverClass());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid01")
    public DataSource dataSource01() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(druidProperties.getDruid01username());
        dataSource.setPassword(druidProperties.getDruid01passwrod());
        dataSource.setUrl(druidProperties.getDruid01jdbcUrl());
        dataSource.setDriverClassName(druidProperties.getDruid01driverClass());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid02")
    public DataSource dataSource02() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(druidProperties.getDruid02username());
        dataSource.setPassword(druidProperties.getDruid02passwrod());
        dataSource.setUrl(druidProperties.getDruid02jdbcUrl());
        dataSource.setDriverClassName(druidProperties.getDruid02driverClass());
        return dataSource;
    }

    @Bean("multiDataSource")
    public MultiDataSource dataSource() {
        // 自己的多数据源类 需要 继承 AbstractRoutingDataSource
        MultiDataSource multiDataSource = new MultiDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("dataSource00", dataSource00());
        targetDataSources.put("dataSource01", dataSource01());
        targetDataSources.put("dataSource02", dataSource02());

        //把多个数据 和 多数据源  进行关联
        multiDataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源
        multiDataSource.setDefaultTargetDataSource(dataSource00());

        //将索引字段和 数据源进行映射 ，方便 分库时候 根据取模的值 计算出是哪个库
        Map<Integer, String> setMappings = new HashMap<>();
        setMappings.put(0, "dataSource00");
        setMappings.put(1, "dataSource01");
        setMappings.put(2, "dataSource02");
        dsRoutingSetProperties.setDataSourceKeysMapping(setMappings);

        return multiDataSource;

    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("multiDataSource") MultiDataSource multiDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //设置数据源 为 上面的 自定义数据源
        sqlSessionFactoryBean.setDataSource(multiDataSource);
        //设置mybatis映射路径
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mappers/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("multiDataSource") MultiDataSource multiDataSource) {
        return new DataSourceTransactionManager(multiDataSource);
    }

}
