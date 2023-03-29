package com.safecode.cyberzone.authorize.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;
import com.safecode.cyberzone.base.dto.ResponseData.DetailView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    public interface SysUserSimpleView extends DetailView {
    }

    ;

    public interface SysUserDetailView extends SysUserSimpleView {
    }

    ;

    private Integer id;

    private String account;

    private String username;

    private String telephone;

    private String mail;

    private String password;

    private Integer deptId;

    private String deptName;

    private Integer gender;

    private String company;

    private String job;

    private String icon;

    private Integer status;

    private Integer accountNonExpired;

    private Integer credentialsNonExpired;

    private Integer accountNonLocked;

    private String oldPassword;

    private Date credentialsExpired;

    private String signature;

    private String faceId;

    private Integer facePerm;

    private String remark;

    private String operator;

    private Date operateTime;

    private String creater;

    private Date createTime;

    private String operateIp;

    @JsonView(SysUserSimpleView.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(SysUserSimpleView.class)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @JsonView(SysUserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @JsonView(SysUserSimpleView.class)
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    @JsonView(SysUserSimpleView.class)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail == null ? null : mail.trim();
    }

    @JsonView(SysUserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @JsonView(SysUserSimpleView.class)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @JsonView(SysUserSimpleView.class)
    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @JsonView(SysUserSimpleView.class)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonView(SysUserSimpleView.class)
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonView(SysUserDetailView.class)
    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getFacePerm() {
        return facePerm;
    }

    public void setFacePerm(Integer facePerm) {
        this.facePerm = facePerm;
    }

    @JsonView(SysUserSimpleView.class)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @JsonView(SysUserSimpleView.class)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    @JsonView(SysUserSimpleView.class)
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @JsonView(SysUserSimpleView.class)
    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Integer accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Integer credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @JsonView(SysUserSimpleView.class)
    public Integer getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Integer accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @JsonView(SysUserDetailView.class)
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @JsonView(SysUserSimpleView.class)
    public Date getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Date credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @JsonView(SysUserSimpleView.class)
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @JsonView(SysUserSimpleView.class)
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    @JsonView(SysUserSimpleView.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}