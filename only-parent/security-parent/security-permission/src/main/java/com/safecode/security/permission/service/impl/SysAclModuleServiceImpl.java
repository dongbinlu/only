package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysAclModule;
import com.safecode.security.permission.exception.ParamException;
import com.safecode.security.permission.mapper.SysAclMapper;
import com.safecode.security.permission.mapper.SysAclModuleMapper;
import com.safecode.security.permission.param.AclModuleParam;
import com.safecode.security.permission.service.SysAclModuleService;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.utils.BeanValidator;
import com.safecode.security.permission.utils.IpUtil;
import com.safecode.security.permission.utils.LevelUtil;

@Service("sysAclModuleService")
@Transactional
public class SysAclModuleServiceImpl implements SysAclModuleService {

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(AclModuleParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).status(param.getStatus()).remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getAccount());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);
        sysLogService.saveAclModuleLog(null, aclModule);
    }

    @Override
    public void update(AclModuleParam param) {
        BeanValidator.check(param);
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");

        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName())
                .parentId(param.getParentId()).seq(param.getSeq()).status(param.getStatus()).remark(param.getRemark())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getAccount());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        // 更新子级
        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);

    }

    private void updateWithChild(SysAclModule before, SysAclModule after) {
        String oldLevelPrefix = before.getLevel();
        String newLevelPrefix = after.getLevel();
        if (!oldLevelPrefix.equals(newLevelPrefix)) {
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before);
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                // 批量更新
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }

    private boolean checkExist(Integer parentId, String aclModuleName, Integer aclModuleId) {
        return sysAclModuleMapper.countByParentIdAndName(parentId, aclModuleName, aclModuleId) > 0;
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();

    }

    @Override
    public void delete(Integer id) {

        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "待删除的权限模块不存在");

        // 1,判断权限模块下是否存在子模块
        if (sysAclModuleMapper.countByParentId(id) > 0) {
            throw new ParamException("当前模块下存在子模块，无法删除");
        }

        // 2，判断权限模块下是否存在权限点
        if (sysAclMapper.countByAclModuleId(id) > 0) {
            throw new ParamException("当前模块下存在权限点，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(id);
        sysLogService.saveAclModuleLog(before, null);
    }
}
