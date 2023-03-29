package com.safecode.cyberzone.auth.mapper;

import org.apache.ibatis.annotations.Param;

import com.safecode.cyberzone.auth.entity.SysUser;

public interface SysUserMapper {

    SysUser findByKeyword(@Param("keyword") String keyword);

    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}
