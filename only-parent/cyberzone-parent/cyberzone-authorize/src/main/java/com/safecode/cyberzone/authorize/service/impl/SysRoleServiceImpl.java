package com.safecode.cyberzone.authorize.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.dto.RoleDto;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.exception.ParamException;
import com.safecode.cyberzone.authorize.mapper.SysAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleUserMapper;
import com.safecode.cyberzone.authorize.mapper.SysUserMapper;
import com.safecode.cyberzone.authorize.param.RoleParam;
import com.safecode.cyberzone.authorize.service.SysRoleService;
import com.safecode.cyberzone.authorize.utils.BeanValidator;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.authorize.utils.StringUtil;

@Service("sysRoleService")
@Transactional
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysCoreService sysCoreService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public synchronized void save(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole role = SysRole.builder().name(param.getName()).status(param.getStatus()).type(param.getType())
                .remark(param.getRemark()).build();
        role.setCreater(AuthHolder.getCurrentUsername());
        role.setCreateTime(new Date());

        role.setOperateIp(IpUtil.getRemoteIp(request));

        sysRoleMapper.insertSelective(role);
    }

    @Override
    public void update(RoleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).status(param.getStatus())
                .type(param.getType()).remark(param.getRemark()).build();
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public PageInfo<RoleDto> newGetAll(Pageable pageable, RoleDto dto) {

        Integer userId = AuthHolder.getCurrentUserId();
        // 如果是超管
        if (sysCoreService.isSuperAdmin(userId)) {
            // 获取角色列表
            PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());

            Page<RoleDto> roleList = sysRoleMapper.new2GetAll(dto.getPlatformName(), dto.getName());

            for (RoleDto roleDto : roleList) {
                // 通过角色id查用户数量
                int count = sysRoleUserMapper.countUserByRoleId(roleDto.getId());
                roleDto.setUnum(count);
            }
            return new PageInfo<RoleDto>(roleList);

        }
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return new PageInfo<RoleDto>(Lists.newArrayList());
        }
        // 根据角色ID查角色
        List<SysRole> roleLists = sysRoleMapper.getByIdList(roleIdList);

        List<String> adminList = roleLists.stream().map(role -> role.getAdmin()).collect(Collectors.toList());
        String admin = String.join(",", adminList).replace("管理员", "");
        List<String> newAdminList = Arrays.asList(admin.split(","));
        // 运营管理中心管理员
        if (newAdminList.contains("靶场用户")) {
            // 获取角色列表
            PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());

            Page<RoleDto> roleList = sysRoleMapper.new2GetAll(dto.getPlatformName(), dto.getName());

            for (RoleDto roleDto : roleList) {
                // 通过角色id查用户数量
                int count = sysRoleUserMapper.countUserByRoleId(roleDto.getId());
                roleDto.setUnum(count);
            }
            return new PageInfo<RoleDto>(roleList);
        }

        // 非超管，各平台的管理员，自定义管理员
        ArrayList<Integer> roleIdLists = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(StringUtil.splitToListString(StringUtils.join(newAdminList, ",")))) {
            List<SysRole> roles = sysRoleMapper.getByAdminList(newAdminList);
            Set<Integer> set = roles.stream().map(role -> role.getId()).collect(Collectors.toSet());
            roleIdLists = Lists.newArrayList(set);
        }
        // 没有指定权限的角色列表
        ArrayList<Integer> roleIdList2 = Lists.newArrayList();

        List<SysRole> allRoleList = sysRoleMapper.getAll();
        List<Integer> allRoleIdList = allRoleList.stream().map(sysRole -> sysRole.getId()).collect(Collectors.toList());
        for (Integer roleId : allRoleIdList) {
            List<String> codeIdListByRoleIdList = sysRoleAclMapper
                    .getCodeIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
            if (CollectionUtils.isEmpty(codeIdListByRoleIdList)) {
                roleIdList2.add(roleId);
            }

        }
        roleIdLists.addAll(roleIdList2);
        roleIdLists.addAll(roleIdList);
        List<Integer> newRoleIdList = roleIdLists.stream().distinct().collect(Collectors.toList());

        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());

        Page<RoleDto> roleList = sysRoleMapper.newGetAll(newRoleIdList, dto.getPlatformName(), dto.getName());

        for (RoleDto roleDto : roleList) {
            // 通过角色id查用户数量
            int count = sysRoleUserMapper.countUserByRoleId(roleDto.getId());
            roleDto.setUnum(count);
        }
        return new PageInfo<RoleDto>(roleList);
    }

    @Override
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    @Override
    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    @Override
    public List<SysRole> getRoleListByAclId(int aclId) {

        // 通过权限id获取权限码，
        String code = sysAclMapper.selectByPrimaryKey(aclId).getCodeId();
        // 通过权限码获取角色id
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByCodeId(Lists.<String>newArrayList(code));

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
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    @Override
    public void delete(int roleId) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
        Preconditions.checkNotNull(sysRole, "待删除的角色不存在");
        if (sysRoleUserMapper.countByRoleId(roleId) > 0) {
            throw new ParamException("待删除的角色下有用户，无法删除");
        }
        sysRoleMapper.deleteByPrimaryKey(roleId);
        sysRoleUserMapper.deleteByRoleId(roleId);
        sysRoleAclMapper.deleteByRoleId(roleId);
    }

    @Override
    public void batchDelete(List<Integer> roleIdList) {
        for (Integer roleId : roleIdList) {
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleId);
            Preconditions.checkNotNull(sysRole, "待删除的角色不存在");
            if (sysRoleUserMapper.countByRoleId(roleId) > 0) {
                throw new ParamException(sysRole.getName() + "下有用户，无法删除");
            }
        }
        sysRoleMapper.batchDelete(roleIdList);
        sysRoleUserMapper.batchDelete(roleIdList);
        sysRoleAclMapper.batchDelete(roleIdList);
    }

    private boolean checkExist(String roleName, Integer roleId) {
        return sysRoleMapper.countByName(roleName, roleId) > 0;
    }

    @Override
    public PageInfo<SysUser> roleToUser(Pageable pageable, Integer roleId, String account, String username) {
        // 根据角色id获取用户idList
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return new PageInfo<SysUser>(Lists.newArrayList());
        }
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        // 根据用户idList获取用户
        Page<SysUser> page = sysUserMapper.roleToUser(userIdList, account, username);
        return new PageInfo<SysUser>(page);
    }

    @Override
    public SysRole id(Integer id) {
        SysRole role = sysRoleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(role, "该角色不存在");
        return role;
    }

    // 移除指定角色下的用户
    @Override
    public void remove(Integer roleId, Integer userId) {
        sysRoleUserMapper.remove(roleId, userId);
    }

    @Override
    public void batchRemove(Integer roleId, String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserMapper.batchRemove(roleId, userIdList);
    }

    @Override
    public void writeExecuteRoleIdToRedis() {
        // 获取执行端角色ID
        List<Integer> executeRoleIdList = sysRoleAclMapper.getExecuteRoleIdList();
        String jsonString = JSON.toJSONString(executeRoleIdList);
        redisTemplate.opsForValue().set("execute_role", jsonString);
    }
}
