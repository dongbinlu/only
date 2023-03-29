package com.safecode.security.permission.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.exception.ParamException;
import com.safecode.security.permission.mapper.SysAclMapper;
import com.safecode.security.permission.mapper.SysRoleAclMapper;
import com.safecode.security.permission.param.AclParam;
import com.safecode.security.permission.service.SysAclService;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.utils.BeanValidator;
import com.safecode.security.permission.utils.IpUtil;

@Service("sysAclService")
@Transactional
public class SysAclServiceImpl implements SysAclService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getAclModuleId(), param.getId())) {
            throw new ParamException("当前权限模块下存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCodeId(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getAccount());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());
        sysAclMapper.insertSelective(acl);
        sysLogService.saveAclLog(null, acl);
    }

    @Override
    public void update(AclParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getAclModuleId(), param.getId())) {
            throw new ParamException("当前权限模块下存在相同名称的权限点");
        }

        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus()).seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getAccount());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before, after);
    }

    @Override
    public PageInfo<SysAcl> getPageByAclModuleId(Integer aclModuleId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().replace(":", ""));
        Page<SysAcl> page = sysAclMapper.getPageByAclModuleId(aclModuleId);
        return new PageInfo<SysAcl>(page);
    }

    public boolean checkExist(String aclName, Integer aclModuleId, Integer aclId) {
        return sysAclMapper.countByNameAndAclModuleId(aclName, aclModuleId, aclId) > 0;
    }

    private String generateCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()) + "_" + (int) (Math.random() * 100);
    }

    @Override
    public void delete(Integer id) {
        SysAcl before = sysAclMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(before, "该权限点不存在");

        if (sysRoleAclMapper.countByCodeId(before.getCodeId()) > 0) {
            throw new ParamException("该权限点下有用户，无法删除");
        }

        sysAclMapper.deleteByPrimaryKey(id);
        sysLogService.saveAclLog(before, null);
    }
}
