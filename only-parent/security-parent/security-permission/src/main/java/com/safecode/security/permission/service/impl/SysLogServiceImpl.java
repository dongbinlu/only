package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.safecode.security.permission.common.LogType;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.entity.SysAclModule;
import com.safecode.security.permission.entity.SysDept;
import com.safecode.security.permission.entity.SysLogWithBLOBs;
import com.safecode.security.permission.entity.SysRole;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.exception.ParamException;
import com.safecode.security.permission.mapper.SysAclMapper;
import com.safecode.security.permission.mapper.SysAclModuleMapper;
import com.safecode.security.permission.mapper.SysDeptMapper;
import com.safecode.security.permission.mapper.SysLogMapper;
import com.safecode.security.permission.mapper.SysRoleMapper;
import com.safecode.security.permission.mapper.SysUserMapper;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.utils.IpUtil;
import com.safecode.security.permission.utils.JsonMapper;

@Service("sysLogService")
@Transactional
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    /**
     * <p>Title: saveDeptLog</p>
     * <p>Description: 部门日志</p>
     *
     * @param before
     * @param after
     */
    @Override
    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(before == null ? after.getId() : before.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * <p>Title: saveUserLog</p>
     * <p>Description: 用户日志</p>
     *
     * @param before
     * @param after
     */
    @Override
    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER);
        sysLog.setTargetId(before == null ? after.getId() : before.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * <p>Title: saveRoleLog</p>
     * <p>Description: 角色日志</p>
     *
     * @param before
     * @param after
     */
    @Override
    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(before == null ? after.getId() : before.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * <p>Title: saveAclLog</p>
     * <p>Description: 权限点日志</p>
     *
     * @param before
     * @param after
     */
    @Override
    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(before == null ? after.getId() : before.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);

    }

    /**
     * <p>Title: saveAclModuleLog</p>
     * <p>Description: 权限模块日志</p>
     *
     * @param before
     * @param after
     */
    @Override
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(before == null ? after.getId() : before.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * <p>Title: saveRoleAclLog</p>
     * <p>Description: 角色权限日志</p>
     *
     * @param roleId
     * @param before
     * @param after
     */
    @Override
    public void saveRoleAclLog(int roleId, List<String> before, List<String> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    /**
     * <p>Title: saveRoleUser</p>
     * <p>Description: 角色用户日志</p>
     *
     * @param roleId
     * @param before
     * @param after
     */
    @Override
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveUserRoleLog(int userId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_USER_ROLE);
        sysLog.setTargetId(userId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setStatus(1);
        sysLog.setOperator(RequestHolder.getCurrentUser().getAccount());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public PageInfo<SysLogWithBLOBs> getPage(Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().replace(":", ""));
        Page<SysLogWithBLOBs> page = sysLogMapper.getPage();
        return new PageInfo<SysLogWithBLOBs>(page);
    }

    /**
     * 恢复操作
     */
    @Override
    public void recover(Integer id) {
        SysLogWithBLOBs sysLog = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLog, "待还原的记录不存在");

        switch (sysLog.getType()) {
            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门不存在");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操作不做还原");
                }
                SysDept afterDept = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysDept>() {
                });
                afterDept.setOperator(RequestHolder.getCurrentUser().getAccount());
                afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterDept.setOperateTime(new Date());
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;
            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户不存在");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操做不做还原");
                }
                SysUser afterUser = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysUser>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getAccount());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(new Date());
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;
            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色不存在");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操做不做还原");
                }
                SysRole afterRole = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysRole>() {
                });
                afterRole.setOperator(RequestHolder.getCurrentUser().getAccount());
                afterRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterRole.setOperateTime(new Date());
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;
            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAclModule, "待还原的权限模块不存在");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操做不做还原");
                }
                SysAclModule afterAclModule = JsonMapper.string2Obj(sysLog.getOldValue(),
                        new TypeReference<SysAclModule>() {
                        });
                afterAclModule.setOperator(RequestHolder.getCurrentUser().getAccount());
                afterAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAclModule.setOperateTime(new Date());
                sysAclModuleMapper.updateByPrimaryKeySelective(afterAclModule);
                saveAclModuleLog(beforeAclModule, afterAclModule);
                break;
            case LogType.TYPE_ACL:
                SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限点不存在");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增和删除操做不做还原");
                }
                SysAcl afterAcl = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysAcl>() {
                });
                afterAcl.setOperator(RequestHolder.getCurrentUser().getAccount());
                afterAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAcl.setOperateTime(new Date());
                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;
            case LogType.TYPE_ROLE_ACL:
                SysRole roleAcl = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(roleAcl, "角色已经不存在");
                List<String> codeIdList = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<String>>() {
                });
                sysRoleAclService.changeRoleAcls(sysLog.getTargetId(), codeIdList);
                break;
            case LogType.TYPE_ROLE_USER:
                SysRole roleUser = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(roleUser, "角色已经不存在");
                List<Integer> userIdList = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                });
                sysRoleUserService.changeRoleUsers(sysLog.getTargetId(), userIdList);
                break;
            case LogType.TYPE_USER_ROLE:
                SysUser userRole = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(userRole, "用户已经不存在");
                List<Integer> roleIdList = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                });
                sysRoleUserService.changeUserRoles(sysLog.getTargetId(), roleIdList);
                break;
            default:
                break;
        }
    }

}
