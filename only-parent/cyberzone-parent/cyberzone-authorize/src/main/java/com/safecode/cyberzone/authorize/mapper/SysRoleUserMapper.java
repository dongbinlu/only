package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.cyberzone.authorize.entity.SysRoleUser;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);

    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    void deleteByRoleId(@Param("roleId") int roleId);

    void deleteByUserId(@Param("userId") int userId);

    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void batchDelete(@Param("roleIdList") List<Integer> roleIdList);

    int countUserByRoleId(@Param("roleId") int roleId);

    void remove(@Param("roleId") Integer roleId, @Param("userId") Integer userId);

    void batchRemove(@Param("roleId") Integer roleId, @Param("userIdList") List<Integer> userIdList);

    int countByRoleId(@Param("roleId") Integer roleId);

}