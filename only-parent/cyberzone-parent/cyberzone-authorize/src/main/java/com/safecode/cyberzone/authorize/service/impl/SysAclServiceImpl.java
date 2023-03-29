package com.safecode.cyberzone.authorize.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.exception.ParamException;
import com.safecode.cyberzone.authorize.mapper.SysAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleAclMapper;
import com.safecode.cyberzone.authorize.param.AclParam;
import com.safecode.cyberzone.authorize.service.SysAclService;
import com.safecode.cyberzone.authorize.utils.BeanValidator;
import com.safecode.cyberzone.authorize.utils.IpUtil;

@Service("sysAclService")
@Transactional
public class SysAclServiceImpl implements SysAclService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void save(AclParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCodeId(generateCode());
        acl.setOperator(AuthHolder.getCurrentUsername());
        acl.setOperateIp(IpUtil.getRemoteIp(request));
        acl.setOperateTime(new Date());
        sysAclMapper.insertSelective(acl);
    }

    @Override
    public void update(AclParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus()).seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setOperator(AuthHolder.getCurrentUsername());
        after.setOperateIp(IpUtil.getRemoteIp(request));
        after.setOperateTime(new Date());
        sysAclMapper.updateByPrimaryKeySelective(after);
    }

    @Override
    public PageInfo<SysAcl> getPageByAclModuleId(Integer aclModuleId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize());
        Page<SysAcl> page = sysAclMapper.getPageByAclModuleId(aclModuleId);
        return new PageInfo<SysAcl>(page);
    }

    private boolean checkExist(Integer aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    private String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int) (Math.random() * 100);
    }

    @Override
    public SysAcl selectByPrimaryKey(Integer id) {
        SysAcl sysAcl = sysAclMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysAcl, "该权限点不存在");
        return sysAcl;
    }

    @Override
    public void delete(Integer aclId) {
        SysAcl sysAcl = sysAclMapper.selectByPrimaryKey(aclId);
        Preconditions.checkNotNull(sysAcl, "该权限点不存在");

        // 同时删除角色权限表
        sysRoleAclMapper.deleteByCodeId(sysAcl.getCodeId());
//		if (sysRoleAclMapper.countByCodeId(sysAcl.getCodeId()) > 0) {
//			throw new ParamException("该权限点下有用户，无法删除");
//		}
        sysAclMapper.deleteByPrimaryKey(aclId);
    }

}
