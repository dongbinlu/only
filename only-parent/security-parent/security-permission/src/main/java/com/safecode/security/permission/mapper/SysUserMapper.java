package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.security.permission.entity.SysUser;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int countByDeptId(@Param("deptId") Integer deptId);

    int countByAccount(@Param("account") String account, @Param("id") Integer id);

    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    Page<SysUser> getPageByDeptId(@Param("deptId") Integer deptId);

    SysUser findBykeyword(@Param("keyword") String keyword);

    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    List<SysUser> getAll();

}