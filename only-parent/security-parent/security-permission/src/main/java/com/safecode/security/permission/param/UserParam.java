package com.safecode.security.permission.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserParam {

    private Integer id;

    @NotBlank(message = "账号不可以为空")
    @Length(min = 1, max = 20, message = "账号需要在20字以内")
    private String account;

    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名需要在20字以内")
    private String username;

    @NotBlank(message = "电话不可以为空")
    @Length(min = 1, max = 13, message = "电话长度需要在13字以内")
    private String telephone;

    @NotBlank(message = "邮箱不可以为空")
    @Email(message = "邮箱不合法")
    private String mail;

    @NotNull(message = "必须提供用户所在部门")
    private Integer deptId;

    @Min(value = 0, message = "性别不合法")
    @Max(value = 1, message = "性别不合法")
    private Integer gender;

    @Length(min = 0, max = 20, message = "单位需要在20字以内")
    private String company;

    @Length(min = 0, max = 20, message = "工作需要在20字以内")
    private String job;

    @Length(min = 0, max = 200, message = "头像需要在200字以内")
    private String icon;

    @NotNull(message = "必须指定用户状态")
    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 1, message = "用户状态不合法")
    private Integer status;

    @Length(min = 0, max = 200, message = "个性签名需要在200字以内")
    private String signature;

    @Length(min = 0, max = 200, message = "人脸需要在200字以内")
    private String faceId;

    @Min(value = 0, message = "人脸权限不合法")
    @Max(value = 1, message = "人脸权限不合法")
    private Integer facePerm;

    @Length(min = 0, max = 200, message = "备注需要在200字以内")
    private String remark;

}
