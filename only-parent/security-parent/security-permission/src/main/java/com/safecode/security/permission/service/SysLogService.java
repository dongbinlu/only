package com.safecode.security.permission.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.entity.SysAclModule;
import com.safecode.security.permission.entity.SysDept;
import com.safecode.security.permission.entity.SysLogWithBLOBs;
import com.safecode.security.permission.entity.SysRole;
import com.safecode.security.permission.entity.SysUser;

public interface SysLogService {

    public void saveDeptLog(SysDept before, SysDept after);

    public void saveUserLog(SysUser before, SysUser after);

    public void saveRoleLog(SysRole before, SysRole after);

    public void saveAclLog(SysAcl before, SysAcl after);

    public void saveAclModuleLog(SysAclModule before, SysAclModule after);

    public void saveRoleAclLog(int roleId, List<String> before, List<String> after);

    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after);

    public void saveUserRoleLog(int userId, List<Integer> before, List<Integer> after);

    public PageInfo<SysLogWithBLOBs> getPage(Pageable pageable);

    public void recover(Integer id);

}
