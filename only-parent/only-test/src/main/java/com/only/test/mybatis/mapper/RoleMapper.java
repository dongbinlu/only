package com.only.test.mybatis.mapper;

import com.only.test.mybatis.entity.User;

public interface RoleMapper {


    /**
     * 测试二级缓存
     *
     * @param userId
     * @return
     */
    void updateByUserId(String username, Integer userId);

    User getByRoleId(Integer roleId);

    void updateByRoleId(String roleName, Integer roleId);

    User getByRoleName(String roleName);


}
