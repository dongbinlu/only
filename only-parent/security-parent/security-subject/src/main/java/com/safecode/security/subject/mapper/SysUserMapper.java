package com.safecode.security.subject.mapper;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.security.subject.entity.SysUser;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    Page<SysUser> getPageByDeptId(@Param("deptId") Integer deptId);
}