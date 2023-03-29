package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysRoleAcl;
import com.safecode.security.permission.mapper.SysRoleAclMapper;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.utils.IpUtil;

@Service("sysRoleAclService")
@Transactional
public class SysRoleAclService {

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysLogService sysLogService;

    public void changeRoleAcls(Integer roleId, List<String> codeIdList) {

        // 1,获取角色对应的权限
        List<String> originAclCodeIdList = sysRoleAclMapper.getCodeIdListByRoleIdList(Lists.newArrayList(roleId));
        // 2,判断传递的codeIdList和 角色对应的权限List是否相同
        if (originAclCodeIdList.size() == codeIdList.size()) {
            Set<String> originAclCodeIdSet = Sets.newHashSet(originAclCodeIdList);
            Set<String> aclCodeIdSet = Sets.newHashSet(codeIdList);
            originAclCodeIdSet.removeAll(aclCodeIdSet);
            // 3，相同、直接return
            if (CollectionUtils.isEmpty(originAclCodeIdSet)) {
                return;
            }
        }
        // 4，不同 修改角色对应的权限
        updateRoleAcls(roleId, codeIdList);
        sysLogService.saveRoleAclLog(roleId, originAclCodeIdList, codeIdList);
    }

    public void updateRoleAcls(Integer roleId, List<String> codeIdList) {

        // 5,先删除原来角色对应的权限
        sysRoleAclMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(codeIdList)) {
            return;
        }

        // 6,批量修改
        List<SysRoleAcl> roleAclList = Lists.newArrayList();

        for (String codeId : codeIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).codeId(codeId)
                    .operator(RequestHolder.getCurrentUser().getAccount()).operateTime(new Date())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).build();
            roleAclList.add(roleAcl);
        }
        // 7,写入数据库
        sysRoleAclMapper.batchInsert(roleAclList);
    }

}
