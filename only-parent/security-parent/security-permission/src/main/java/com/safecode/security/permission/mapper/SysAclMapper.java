package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.security.permission.entity.SysAcl;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndAclModuleId(@Param("name") String name, @Param("aclModuleId") Integer aclModuleId,
                                  @Param("id") Integer id);

    Page<SysAcl> getPageByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> getAll();

    List<SysAcl> getByRoleCodeIdList(@Param("roleCodeIdList") List<String> roleCodeIdList);

    int countByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> getByUrl(@Param("url") String url);
}