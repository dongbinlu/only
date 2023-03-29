package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.security.permission.entity.SysRoleAcl;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    List<String> getCodeIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);

    int countByCodeId(@Param("codeId") String codeId);

    List<Integer> getRoleIdListByCodeId(@Param("codeId") String codeId);

    int countByRoleId(@Param("roleId") Integer roleId);
}