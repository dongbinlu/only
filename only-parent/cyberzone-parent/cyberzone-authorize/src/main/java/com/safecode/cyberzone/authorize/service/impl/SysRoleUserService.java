package com.safecode.cyberzone.authorize.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysRoleUser;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.mapper.SysRoleMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleUserMapper;
import com.safecode.cyberzone.authorize.mapper.SysUserMapper;

@Service("sysRoleUserService")
@Transactional
public class SysRoleUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    public List<SysUser> getListByRoleId(int roleId) {
        // 根据角色id获取用户idList
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        // 根据用户idList获取用户
        return sysUserMapper.getByIdList(userIdList);
    }

    // 根据用户id获取角色
    public List<SysRole> getListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        // 根据角色idList获取角色
        return sysRoleMapper.getByIdList(roleIdList);
    }

    // 根据角色ID更新用户
    /*
     * 如果一直 不做更新 否则：先删除再做更新操作
     */
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        // 根据角色id获取用户ID list
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        // 长度相同
        if (originUserIdList.size() == userIdList.size()) {
            // 数据库
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            // 用户传过来的
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);

            originUserIdSet.removeAll(userIdSet);
            // 值相同
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
    }

    @Transactional
    private void updateRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        userIdList.addAll(originUserIdList);
        HashSet<Integer> set = Sets.newHashSet(userIdList);
        userIdList.clear();
        userIdList.addAll(set);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        sysRoleUserMapper.deleteByRoleId(roleId);
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(AuthHolder.getCurrentUsername()).operateIp(AuthHolder.getRemoteIp())
                    .operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }

    public void changeUserRoles(Integer userId, List<Integer> roleIdList) {
        // 1 根据用户Id 获取角色IdList
        List<Integer> originRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);

        // 2如果一直 不做操作
        if (originRoleIdList.size() == roleIdList.size()) {
            Set<Integer> originRoleIdSet = Sets.newHashSet(originRoleIdList);
            Set<Integer> roleIdSet = Sets.newHashSet(roleIdList);
            originRoleIdList.remove(roleIdSet);
            if (CollectionUtils.isEmpty(originRoleIdSet)) {
                return;
            }
        }

        // 不一致 先删除，后操作
        updateUserRoles(userId, roleIdList);

    }

    private void updateUserRoles(Integer userId, List<Integer> roleIdList) {
        // 删除用户Id
        sysRoleUserMapper.deleteByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return;
        }
        // 批量更新
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer roleId : roleIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(AuthHolder.getCurrentUsername()).operateIp(AuthHolder.getRemoteIp())
                    .operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }

}
