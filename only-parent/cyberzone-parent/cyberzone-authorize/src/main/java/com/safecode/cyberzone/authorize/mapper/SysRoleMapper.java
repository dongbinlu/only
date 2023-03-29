package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.cyberzone.authorize.dto.RoleDto;
import com.safecode.cyberzone.authorize.entity.SysRole;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    int countByName(@Param("name") String name, @Param("id") Integer id);

    List<SysRole> getAll();

    Page<RoleDto> newGetAll(@Param("roleIdList") List<Integer> roleIdList, @Param("platformName") String platformName,
                            @Param("name") String name);

    Page<RoleDto> new2GetAll(@Param("platformName") String platformName, @Param("name") String name);

    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);

    Page<SysRole> getByIdList2(@Param("idList") List<Integer> idList, @Param("name") String name,
                               @Param("codeId") String codeId);

    void batchDelete(@Param("roleIdList") List<Integer> roleIdList);

    List<SysRole> getByAdminList(@Param("adminList") List<String> adminList);

    List<SysRole> getNoPlatform();

}