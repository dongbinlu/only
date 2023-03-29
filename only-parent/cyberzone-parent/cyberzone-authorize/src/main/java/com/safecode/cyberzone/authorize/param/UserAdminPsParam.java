package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * 管理员重置用户密码
 */

@Getter
@Setter
@ToString
public class UserAdminPsParam {

    @NotNull(message = "用户id不可以为空")
    private Integer id;

    @NotBlank(message = "您的账号密码不能为空")
    private String mePassword;

    @NotBlank(message = "密码不允许为空")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "密码须由数字、英文字母、特殊符号组成,长度为8-16位")
    private String password;

    @NotBlank(message = "确认密码不允许为空")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "确认密码须由数字、英文字母、特殊符号组成,长度为8-16位")
    private String confirmPassword;

}
