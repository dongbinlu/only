package com.only.multids.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.only.multids.dynamicdatasource.TulingMultiDataSource;
import com.only.multids.support.TulingDruidProperties;
import com.only.multids.support.TulingDsRoutingSetProperties;
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
@EnableConfigurationProperties({TulingDsRoutingSetProperties.class, TulingDruidProperties.class})  //  @EnableConfigurationProperties注解的作用是：使使用 @ConfigurationProperties 注解的类生效。
@MapperScan(basePackages = "com.only.multids.busi.dao")
public class DataSourceConfiguration {

    @Autowired
    private TulingDsRoutingSetProperties tulingDsRoutingSetProperties;

    @Autowired
    private TulingDruidProperties tulingDruidProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid00")
    public DataSource dataSource00() {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(tulingDruidProperties.getDruid00username());
        dataSource.setPassword(tulingDruidProperties.getDruid00passwrod());
        dataSource.setUrl(tulingDruidProperties.getDruid00jdbcUrl());
        dataSource.setDriverClassName(tulingDruidProperties.getDruid00driverClass());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid01")
    public DataSource dataSource01() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(tulingDruidProperties.getDruid01username());
        dataSource.setPassword(tulingDruidProperties.getDruid01passwrod());
        dataSource.setUrl(tulingDruidProperties.getDruid01jdbcUrl());
        dataSource.setDriverClassName(tulingDruidProperties.getDruid01driverClass());
        return dataSource;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid02")
    public DataSource dataSource02() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(tulingDruidProperties.getDruid02username());
        dataSource.setPassword(tulingDruidProperties.getDruid02passwrod());
        dataSource.setUrl(tulingDruidProperties.getDruid02jdbcUrl());
        dataSource.setDriverClassName(tulingDruidProperties.getDruid02driverClass());
        return dataSource;
    }

    @Bean("tulingMultiDataSource")
    public TulingMultiDataSource dataSource() {
        // 自己的多数据源类 需要 继承 AbstractRoutingDataSource
        TulingMultiDataSource tulingMultiDataSource = new TulingMultiDataSource();

        Map<Object,Object> targetDataSources = new HashMap<>();
        targetDataSources.put("dataSource00",dataSource00());
        targetDataSources.put("dataSource01",dataSource01());
        targetDataSources.put("dataSource02",dataSource02());

        //把多个数据 和 多数据源  进行关联
        tulingMultiDataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源
        tulingMultiDataSource.setDefaultTargetDataSource(dataSource00());

        //将索引字段和 数据源进行映射 ，方便 分库时候 根据取模的值 计算出是哪个库
        Map<Integer,String> setMappings = new HashMap<>();
        setMappings.put(0,"dataSource00");
        setMappings.put(1,"dataSource01");
        setMappings.put(2,"dataSource02");
        tulingDsRoutingSetProperties.setDataSourceKeysMapping(setMappings);

        return tulingMultiDataSource;

    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("tulingMultiDataSource") TulingMultiDataSource tulingMultiDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //设置数据源 为 上面的 自定义数据源
        sqlSessionFactoryBean.setDataSource(tulingMultiDataSource);
        //设置mybatis映射路径
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mybatis/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("tulingMultiDataSource") TulingMultiDataSource tulingMultiDataSource){
        return new DataSourceTransactionManager(tulingMultiDataSource);
    }

}
