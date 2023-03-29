package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AclModuleParam {
    private Integer id;

    @NotBlank(message = "权限模块名称不可以为空")
    @Length(min = 2, max = 20, message = "权限模块名称长度需要在2~20个字符之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "权限模块展示顺序不可以为空")
    private Integer seq;

    @NotBlank(message = "路由request不可以为空")
    @Length(min = 3, max = 100, message = "路由request长度需要在3-100个字符之间")
    private String request;

    @NotNull(message = "权限模块状态不可以为空")
    @Min(value = 0, message = "权限模块状态不合法")
    @Max(value = 1, message = "权限模块状态不合法")
    private Integer status;

    @Length(max = 200, message = "权限模块图标长度需要在1~200个字符之间")
    private String icon;

    @Min(value = 0, message = "权限模块图标状态不合法")
    @Max(value = 1, message = "权限模块图标状态不合法")
    private Integer iconStatus;

    @Length(max = 200, message = "权限模块备注需要在200个字之间")
    private String remark;
}
