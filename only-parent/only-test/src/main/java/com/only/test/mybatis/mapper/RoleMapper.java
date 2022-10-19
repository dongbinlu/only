package com.only.test.mybatis.mapper;

import com.only.test.mybatis.User;
import org.apache.ibatis.annotations.Update;

public interface RoleMapper {

    User getByRoleId(Integer roleId);

    @Update("update role set role_name = #{roleName} where role_id = #{roleId}")
    void updateByRoleId(String roleName, Integer roleId);

    User getByRoleName(String roleName);


}
