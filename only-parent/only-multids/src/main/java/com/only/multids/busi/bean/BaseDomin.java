package com.only.multids.busi.bean;

import com.only.multids.dynamicdatasource.DataSourceHolder;

public class BaseDomin {

    private String tableSuffix;

    public String getTableSuffix() {
        this.tableSuffix = DataSourceHolder.getTableIndex();
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }
}
