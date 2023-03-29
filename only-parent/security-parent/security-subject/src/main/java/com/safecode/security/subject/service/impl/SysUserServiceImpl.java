package com.safecode.security.subject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.safecode.security.subject.entity.SysUser;
import com.safecode.security.subject.mapper.SysUserMapper;
import com.safecode.security.subject.service.SysUserService;

@Transactional
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser selectByPrimaryKey(Integer id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<SysUser> getPageByDeptId(Integer deptId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        Page<SysUser> page = sysUserMapper.getPageByDeptId(deptId);
        return new PageInfo<SysUser>(page);
    }


}
