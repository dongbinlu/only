package com.safecode.cyberzone.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysRoleAclMapper {
    // 根据角色id获取权限码
    List<String> getCodeIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}