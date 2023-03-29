package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImportParam {

    private Integer id;

    @NotBlank(message = "账号不可以为空")
    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{2,50}", message = "账号须由数字、英文字母、汉字组成,长度为2-50位")
    private String account;

    @NotBlank(message = "姓名不可以为空")
    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{1,10}", message = "姓名须由数字、英文字母、汉字组成,长度为1-10位")
    private String username;

    private String telephone;

    private String mail;

    private String icon;

    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{0,25}", message = "部门须由数字、英文字母、汉字组成,长度为0-25位")
    private String deptName;

    private String gender;

    @Length(min = 0, max = 25, message = "单位长度需要在25字以内")
    private String company;

    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{0,25}", message = "职务须由数字、英文字母、汉字组成,长度为0-25位")
    private String job;

    private String facePerm;

    @Length(min = 0, max = 200, message = "备注长度需要在200个字以内")
    private String remark;

    private String result;
}
