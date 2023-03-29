package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.cyberzone.authorize.entity.SysRoleAcl;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    // 统计数量
    int countByCodeId(@Param("codeId") String codeId);

    // 根据权限码获取角色id
    List<Integer> getRoleIdListByCodeId(@Param("codeIdList") List<String> codeIdList);

    // 根据角色id获取权限码
    List<String> getCodeIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId") int roleId);

    void deleteByCodeId(@Param("codeId") String codeId);

    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);

    void batchDelete(@Param("roleIdList") List<Integer> roleIdList);

    //获取拥有执行端的角色ID
    List<Integer> getExecuteRoleIdList();

}