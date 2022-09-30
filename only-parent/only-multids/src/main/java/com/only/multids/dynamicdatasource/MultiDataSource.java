package com.only.multids.dynamicdatasource;


import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 多数据源类
 * AbstractRoutingDataSource, 该类充当了DataSource的路由中介,
 * 能有在运行时, 根据某种key值来动态切换到真正的DataSource上。
 */
@Slf4j
public class MultiDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSourceKey();
    }
}
