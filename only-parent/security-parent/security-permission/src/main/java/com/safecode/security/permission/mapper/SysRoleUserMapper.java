package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.security.permission.entity.SysRoleUser;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> getRoleIdListByUserId(@Param("userId") Integer userId);

    List<Integer> getUserIdListByRoleId(@Param("roleId") Integer roleId);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    void deleteByUserId(@Param("userId") Integer userId);

    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    int countByRoleId(@Param("roleId") Integer roleId);
}