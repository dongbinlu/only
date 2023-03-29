package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.security.permission.entity.SysAclModule;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByParentIdAndName(@Param("parentId") Integer parentId, @Param("name") String name,
                               @Param("id") Integer id);

    List<SysAclModule> getChildAclModuleListByLevel(SysAclModule syAclModule);

    void batchUpdateLevel(@Param("aclModuleList") List<SysAclModule> aclModuleList);

    List<SysAclModule> getAll();

    int countByParentId(@Param("aclModuleId") Integer aclModuleId);
}