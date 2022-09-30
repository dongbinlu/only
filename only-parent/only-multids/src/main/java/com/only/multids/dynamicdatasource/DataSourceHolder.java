package com.only.multids.dynamicdatasource;

import lombok.extern.slf4j.Slf4j;

/**
 * 多数据源key 缓存类  根据当前线程来选择具体的数据源
 */
@Slf4j
public class DataSourceHolder {

    /**
     *  设置动态选择的Datasource，这里的Set方法可以留给AOP调用，或者留给我们的具体的Dao层或者Service层中手动调用，在执行SQL语句之前。
     */

    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    private static final ThreadLocal<String> tableIndexHolder = new ThreadLocal<>();

    /**
     * 保存数据源的key
     * @param dsKey
     */
    public static void setdataSourceKey(String dsKey) {
        dataSourceHolder.set(dsKey);
    }

    /**
     * 从threadLocal中取出key
     * @return
     */
    public static String getDataSourceKey() {
        return dataSourceHolder.get();
    }

    /**
     * 清除key
     */
    public static void clearDataSourceKey() {
        dataSourceHolder.remove();
    }

    public static String getTableIndex(){
        return tableIndexHolder.get();
    }

    public static void setTableIndex(String tableIndex){
         tableIndexHolder.set(tableIndex);
    }

    public static void clearTableIndex(){
        tableIndexHolder.remove();
    }
}
