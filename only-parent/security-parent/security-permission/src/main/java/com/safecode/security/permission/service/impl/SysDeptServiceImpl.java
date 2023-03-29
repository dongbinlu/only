package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysDept;
import com.safecode.security.permission.exception.ParamException;
import com.safecode.security.permission.exception.PermissionException;
import com.safecode.security.permission.mapper.SysDeptMapper;
import com.safecode.security.permission.mapper.SysUserMapper;
import com.safecode.security.permission.param.DeptParam;
import com.safecode.security.permission.service.SysDeptService;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.utils.BeanValidator;
import com.safecode.security.permission.utils.IpUtil;
import com.safecode.security.permission.utils.LevelUtil;

@Service("sysDeptService")
@Transactional
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(DeptParam param) {
        // 基本参数校验
        BeanValidator.check(param);

        // 判断同一级部门下是否存在名称相同的部门
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept sysDept = SysDept.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .remark(param.getRemark()).build();
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(sysDept);
        sysLogService.saveDeptLog(null, sysDept);
    }

    /**
     * 修改部门，以及子部门的修改
     */
    @Override
    public void update(DeptParam param) {
        BeanValidator.check(param);
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");

        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getAccount());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
        sysLogService.saveDeptLog(before, after);
    }

    public void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();// 0.36
        String oldLevelPrefix = before.getLevel();// 0.34.37
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            // [JAVAEE]
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before);// 0.34.37
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();// 0.34.37.39
                    if (level.indexOf(oldLevelPrefix) == 0) {
						/*
						 * 返回一个新字符串，它是此字符串的一个子字符串。该子字符串始于指定索引处的字符，一直到此字符串末尾。
							参数：beginIndex - 开始处的索引（包括），
							返回：指定的子字符串，
						 */
                        // 0.36 + .39
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                // 批量更新
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKeySelective(after);
    }

    /**
     * 删除部门
     */
    @Override
    public void delete(Integer deptId) {

        SysDept before = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(before, "待删除的部门不存在，无法删除");
        // 判断当前部门下是否有子部门
        if (sysDeptMapper.countByParentId(before.getId()) > 0) {
            throw new PermissionException("当前部门下有子部门，无法删除");
        }
        // 判断当前部门下是否有用户
        if (sysUserMapper.countByDeptId(before.getId()) > 0) {
            throw new PermissionException("当前部门下有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(before.getId());
        sysLogService.saveDeptLog(before, null);
    }

    /**
     * @param parentId
     * @param deptName
     * @param deptId   在修改时判断是否名称相同，要把自己排除掉
     * @return
     */
    public boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }

    /**
     * @param deptId 如果传入进来的是parentId,则查询到的是父节点的level
     * @return
     */
    public String getLevel(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (sysDept == null) {
            return null;
        }
        return sysDept.getLevel();
    }
}
