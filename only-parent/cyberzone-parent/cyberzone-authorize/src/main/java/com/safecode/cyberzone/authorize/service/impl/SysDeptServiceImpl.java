package com.safecode.cyberzone.authorize.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.entity.SysDept;
import com.safecode.cyberzone.authorize.exception.ParamException;
import com.safecode.cyberzone.authorize.mapper.SysDeptMapper;
import com.safecode.cyberzone.authorize.mapper.SysUserMapper;
import com.safecode.cyberzone.authorize.param.DeptParam;
import com.safecode.cyberzone.authorize.service.SysDeptService;
import com.safecode.cyberzone.authorize.utils.BeanValidator;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.authorize.utils.LevelUtil;

@Service
@Transactional
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void save(DeptParam param) {
        BeanValidator.check(param);
        // 判断同一层下存在相同部门名称
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .icon(param.getIcon()).remark(param.getRemark()).build();

        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator(AuthHolder.getCurrentUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(request));
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);
    }

    @Override
    public void update(DeptParam param) {
        BeanValidator.check(param);
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }

        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).icon(param.getIcon()).remark(param.getRemark()).build();

        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperateTime(new Date());
        updateWithChild(before, after);
    }

    private void updateWithChild(SysDept before, SysDept after) {
        String oldLevelPrefix = before.getLevel();
        String newLevelPrefix = after.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before);
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在 ,无法删除");
        // 判断当前部门下是否有子级部门
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门,无法删除");
        }
        // 判断当前部门下是否有用户
        if (sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下有用户,无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }

    // 判断同一层下是否存在相同部门名称 同一部门parentId 一致 update的时候需要用deptId
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    // 获取部门层级
    private String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }

}
