package com.safecode.cyberzone.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.cyberzone.auth.entity.SysAcl;

public interface SysAclMapper {

    List<SysAcl> getAll();

    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);

    // 根据权限码codeList获取权限点aclIdList
    List<Integer> getAclIdListByCodeList(@Param("codeIdList") List<String> codeIdList);
}