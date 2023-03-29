package com.safecode.security.permission.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.param.UserParam;

public interface SysUserService {

    public void save(UserParam param);

    public void update(UserParam param);

    public void delete(Integer userId);

    public PageInfo<SysUser> getPageByDeptId(Integer deptId, Pageable pageable);

    public SysUser findBykeyword(String keyword);

    public List<SysUser> getAll();
}
