package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * 管理员修改用户信息
 */
@Getter
@Setter
@ToString
public class UserAdminParam {

    @NotNull(message = "用户id不可以为空")
    private Integer id;

    @NotBlank(message = "账号不可以为空")
    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{2,50}", message = "账号须由数字、英文字母、汉字组成,长度为2-50位")
    private String account;

    @NotBlank(message = "姓名不可以为空")
    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{1,10}", message = "姓名须由数字、英文字母、汉字组成,长度为1-10位")
    private String username;

    private String telephone;

    private String mail;

    private Integer deptId;

    private String roleIds;

    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{0,25}", message = "部门须由数字、英文字母、汉字组成,长度为0-25位")
    private String deptName;

    @Min(value = 0, message = "用户性别不合法")
    @Max(value = 1, message = "用户性别不合法")
    private Integer gender;

    @Length(min = 0, max = 25, message = "单位长度需要在25字以内")
    private String company;

    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{0,25}", message = "职务须由数字、英文字母、汉字组成,长度为0-25位")
    private String job;

    @Length(min = 0, max = 200, message = "头像路径需要在200个字以内")
    private String icon;

    @Min(value = 0, message = "用户状态不合法")
    @Max(value = 1, message = "用户状态不合法")
    private Integer status = 1;

    @Length(min = 0, max = 200, message = "个性签名需要在200个字以内")
    private String signature;

    @Length(min = 0, max = 200, message = "人脸路径需要在200个字以内")
    private String faceId;

    @Min(value = 0, message = "人脸权限状态不合法")
    @Max(value = 1, message = "人脸权限状态不合法")
    private Integer facePerm = 0;

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark;

}
