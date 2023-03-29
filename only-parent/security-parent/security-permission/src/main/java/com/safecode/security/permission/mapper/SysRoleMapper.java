package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.security.permission.entity.SysRole;

public interface SysRoleMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    int countByName(@Param("name") String name, @Param("id") Integer id);

    Page<SysRole> getPage();

    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);

}