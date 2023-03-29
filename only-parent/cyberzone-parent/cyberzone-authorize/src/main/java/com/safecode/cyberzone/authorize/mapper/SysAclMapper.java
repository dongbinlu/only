package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.cyberzone.authorize.entity.SysAcl;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    int countByNameAndAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("name") String name,
                                  @Param("id") Integer id);

    Page<SysAcl> getPageByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> getAll();

    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);

    List<Integer> getAclModuleIdListByAclIdList(@Param("aclIdList") List<Integer> aclIdList);

    // 根据权限码codeList获取权限点aclIdList
    List<Integer> getAclIdListByCodeList(@Param("codeIdList") List<String> codeIdList);

    // 根据level查权限点
    List<SysAcl> getByLevel(@Param("level") String level);

    // 根据权限模块获取权限点
    List<SysAcl> getByAclModuleIdList(@Param("aclModuleIdList") List<Integer> aclModuleIdList);

}