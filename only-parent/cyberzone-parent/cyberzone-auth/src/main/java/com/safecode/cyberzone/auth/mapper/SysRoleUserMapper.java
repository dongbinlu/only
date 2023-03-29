package com.safecode.cyberzone.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SysRoleUserMapper {
    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);
}