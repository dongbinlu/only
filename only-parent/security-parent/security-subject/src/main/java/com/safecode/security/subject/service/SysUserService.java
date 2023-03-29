package com.safecode.security.subject.service;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.security.subject.entity.SysUser;

public interface SysUserService {
    SysUser selectByPrimaryKey(Integer id);

    PageInfo<SysUser> getPageByDeptId(Integer deptId, Pageable pageable);
}
