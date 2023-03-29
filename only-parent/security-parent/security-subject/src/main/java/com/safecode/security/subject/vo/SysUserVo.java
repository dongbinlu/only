package com.safecode.security.subject.vo;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;
import com.safecode.security.subject.validator.MyConstraint;

public class SysUserVo {

    public interface SimpleView {
    }

    public interface DetailView extends SimpleView {
    }

    private Integer id;

    private String username;

    @MyConstraint(message = "这是一个测试")
    private String account;

    private String mail;

    private Integer deptId;

    @NotBlank(message = "密码不能为空")
    @Length(min = 1, max = 6, message = "密码长度需要在1-6个字符之间")
    private String password;

    @Past(message = "生日不符合")
    private Date birthday;

    @JsonView(SimpleView.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(SimpleView.class)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @JsonView(SimpleView.class)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @JsonView(SimpleView.class)
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @JsonView(DetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(SimpleView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "SysUserVo [id=" + id + ", account=" + account + ", mail=" + mail + ", deptId=" + deptId + ", password="
                + password + ", birthday=" + birthday + "]";
    }


}
