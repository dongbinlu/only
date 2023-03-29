package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysRoleUser;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.mapper.SysRoleUserMapper;
import com.safecode.security.permission.mapper.SysUserMapper;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.utils.IpUtil;

@Service("sysRoleUserService")
public class SysRoleUserService {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    public List<SysUser> getListByRoleId(Integer roleId) {
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

    /**
     * 修改角色用户
     *
     * @param roleId
     * @param userIdList
     */
    public void changeRoleUsers(Integer roleId, List<Integer> userIdList) {

        // 1,根据角色获取原来的用户
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        // 2,判断原来用户和传递的用户是都相同
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {
                // 3,相同，直接return
                return;
            }
        }
        // 4,不同 则修改
        updateRoleUsers(roleId, userIdList);
        sysLogService.saveRoleUserLog(roleId, originUserIdList, userIdList);
    }

    public void updateRoleUsers(Integer roleId, List<Integer> userIdList) {
        // 5，先删除
        sysRoleUserMapper.deleteByRoleId(roleId);
        // 6,判断是否为空
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        // 7,组装数据
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(RequestHolder.getCurrentUser().getAccount()).operateTime(new Date())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            roleUserList.add(roleUser);
        }
        // 8，批量插入
        sysRoleUserMapper.batchInsert(roleUserList);
    }

    /**
     * 修改用户角色
     *
     * @param userId
     * @param roleIdList
     */
    public void changeUserRoles(Integer userId, List<Integer> roleIdList) {

        // 1,获取用户原有角色
        List<Integer> originRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        // 2，判断原有角色和传递的角色是否相同
        if (originRoleIdList.size() == roleIdList.size()) {
            Set<Integer> originRoleIdSet = Sets.newHashSet(originRoleIdList);
            Set<Integer> roleIdSet = Sets.newHashSet(roleIdList);
            originRoleIdSet.removeAll(roleIdSet);
            if (CollectionUtils.isEmpty(originRoleIdSet)) {
                // 3，形同，直接return
                return;
            }
        }
        // 4,不同，则修改
        updateUserRoles(userId, roleIdList);
        sysLogService.saveUserRoleLog(userId, originRoleIdList, roleIdList);
    }

    public void updateUserRoles(Integer userId, List<Integer> roleIdList) {
        // 5,先删除
        sysRoleUserMapper.deleteByUserId(userId);
        // 6,判断传递的角色是否为空
        if (CollectionUtils.isEmpty(roleIdList)) {
            return;
        }
        // 7，组装数据
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer roleId : roleIdList) {
            SysRoleUser sysRoleUser = SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(RequestHolder.getCurrentUser().getAccount()).operateTime(new Date())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            roleUserList.add(sysRoleUser);
        }

        // 8，批量插入
        sysRoleUserMapper.batchInsert(roleUserList);
    }

}
