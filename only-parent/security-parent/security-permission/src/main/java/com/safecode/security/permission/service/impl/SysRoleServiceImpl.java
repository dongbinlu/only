package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.entity.SysRole;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.exception.ParamException;
import com.safecode.security.permission.mapper.SysAclMapper;
import com.safecode.security.permission.mapper.SysRoleAclMapper;
import com.safecode.security.permission.mapper.SysRoleMapper;
import com.safecode.security.permission.mapper.SysRoleUserMapper;
import com.safecode.security.permission.mapper.SysUserMapper;
import com.safecode.security.permission.param.RoleParam;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.service.SysRoleService;
import com.safecode.security.permission.utils.BeanValidator;
import com.safecode.security.permission.utils.IpUtil;

@Service("sysRoleService")
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        SysRole role = SysRole.builder().name(param.getName()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).build();

        role.setOperator(RequestHolder.getCurrentUser().getAccount());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insertSelective(role);
        sysLogService.saveRoleLog(null, role);

    }

    @Override
    public void update(RoleParam param) {
        BeanValidator.check(param);
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }

        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).type(param.getType())
                .status(param.getStatus()).remark(param.getRemark()).build();

        after.setOperator(RequestHolder.getCurrentUser().getAccount());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);

    }

    @Override
    public PageInfo<SysRole> getPage(Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().replace(":", ""));
        Page<SysRole> page = sysRoleMapper.getPage();
        return new PageInfo<SysRole>(page);
    }

    private boolean checkExist(String roleName, Integer roleId) {
        return sysRoleMapper.countByName(roleName, roleId) > 0;
    }

    @Override
    public List<SysRole> getRoleListByUserId(Integer userId) {

        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysRole> getRoleListByAclId(Integer aclId) {
        SysAcl acl = sysAclMapper.selectByPrimaryKey(aclId);
        Preconditions.checkNotNull(acl, "指定权限不存在");
        // 1,通过权限点id获取角色idList
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByCodeId(acl.getCodeId());
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {

        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }

        List<Integer> roleIdList = roleList.stream().map(sysRole -> sysRole.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    @Override
    public void delete(Integer roleId) {
        SysRole before = sysRoleMapper.selectByPrimaryKey(roleId);
        Preconditions.checkNotNull(before, "待删除的角色不存在");

        if (sysRoleUserMapper.countByRoleId(roleId) > 0) {
            throw new ParamException("待删除的角色下有用户，无法删除");
        }
        if (sysRoleAclMapper.countByRoleId(roleId) > 0) {
            throw new ParamException("待删除的角色下有权限，无法删除");
        }
        sysRoleMapper.deleteByPrimaryKey(roleId);
        sysLogService.saveRoleLog(before, null);

    }

}
