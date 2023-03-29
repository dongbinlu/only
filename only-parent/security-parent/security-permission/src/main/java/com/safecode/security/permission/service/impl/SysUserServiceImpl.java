package com.safecode.security.permission.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.exception.ParamException;
import com.safecode.security.permission.mapper.SysUserMapper;
import com.safecode.security.permission.param.UserParam;
import com.safecode.security.permission.service.SysLogService;
import com.safecode.security.permission.service.SysUserService;
import com.safecode.security.permission.utils.BeanValidator;
import com.safecode.security.permission.utils.IpUtil;
import com.safecode.security.permission.utils.MD5Util;
import com.safecode.security.permission.utils.PasswordUtil;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;

    @Override
    public void save(UserParam param) {
        BeanValidator.check(param);
        if (checkAccountExist(param.getAccount(), param.getId())) {
            throw new ParamException("账号已被占用");
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        if (checkTelePhoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }

        String password = PasswordUtil.randomPassword();
        // TODO
        password = "12345678";
        String encryptPassword = MD5Util.encrypt(password);

        SysUser user = SysUser.builder().account(param.getAccount()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).password(encryptPassword)
                .deptId(param.getDeptId()).gender(param.getGender()).company(param.getCompany()).job(param.getJob())
                .icon(param.getIcon()).status(param.getStatus()).signature(param.getSignature())
                .facePerm(param.getFacePerm()).remark(param.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getAccount());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        // TODO : sendEmail 邮件通知用户后保存数据
        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null, user);

    }

    @Override
    public void update(UserParam param) {
        BeanValidator.check(param);
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的部门不存在");

        if (checkAccountExist(param.getAccount(), param.getId())) {
            throw new ParamException("账号已被占用");
        }
        if (checkEmailExist(param.getMail(), param.getId())) {
            throw new ParamException("邮箱已被占用");
        }
        if (checkTelePhoneExist(param.getTelephone(), param.getId())) {
            throw new ParamException("电话已被占用");
        }

        SysUser after = SysUser.builder().id(param.getId()).account(param.getAccount()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).deptId(param.getDeptId())
                .gender(param.getGender()).company(param.getCompany()).job(param.getJob()).icon(param.getIcon())
                .status(param.getStatus()).signature(param.getSignature()).faceId(param.getFaceId())
                .facePerm(param.getFacePerm()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getAccount());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before, after);

    }

    @Override
    public void delete(Integer userId) {
        SysUser before = sysUserMapper.selectByPrimaryKey(userId);
        Preconditions.checkNotNull(before, "待删除的用户不存在");
        sysUserMapper.deleteByPrimaryKey(userId);
        sysLogService.saveUserLog(before, null);

    }

    @Override
    public SysUser findBykeyword(String keyword) {
        return sysUserMapper.findBykeyword(keyword);
    }

    @Override
    public PageInfo<SysUser> getPageByDeptId(Integer deptId, Pageable pageable) {
        PageHelper.startPage(pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort().toString().replace(":", ""));
        Page<SysUser> page = sysUserMapper.getPageByDeptId(deptId);

        return new PageInfo<SysUser>(page);
    }

    public boolean checkAccountExist(String account, Integer userId) {
        return sysUserMapper.countByAccount(account, userId) > 0;
    }

    public boolean checkEmailExist(String mail, Integer userId) {
        return sysUserMapper.countByMail(mail, userId) > 0;
    }

    public boolean checkTelePhoneExist(String telephone, Integer userId) {
        return sysUserMapper.countByTelephone(telephone, userId) > 0;
    }

    @Override
    public List<SysUser> getAll() {
        return sysUserMapper.getAll();
    }

}
