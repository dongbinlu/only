package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.cyberzone.authorize.entity.SysAclModule;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name,
                               @Param("id") Integer id);

    List<SysAclModule> getChildAclModuleListByLevel(SysAclModule syAclModule);

    void batchUpdateLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModuleList);

    int countByParentId(@Param("aclModuleId") int aclModuleId);

    List<SysAclModule> getAllAclModule();

    List<SysAclModule> getByIdList(@Param("idList") List<Integer> idList);
}