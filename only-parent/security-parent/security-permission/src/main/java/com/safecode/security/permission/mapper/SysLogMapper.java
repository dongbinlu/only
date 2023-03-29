package com.safecode.security.permission.mapper;

import com.github.pagehelper.Page;
import com.safecode.security.permission.entity.SysLog;
import com.safecode.security.permission.entity.SysLogWithBLOBs;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    Page<SysLogWithBLOBs> getPage();
}