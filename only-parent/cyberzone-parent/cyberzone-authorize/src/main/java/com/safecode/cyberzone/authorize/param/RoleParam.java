package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoleParam {
    private Integer id;

    @NotBlank(message = "角色名称不可以为空")
    @Pattern(regexp = "[\u4e00-\u9fa5_a-zA-Z0-9_]{2,50}", message = "角色须由数字、英文字母、汉字组成,长度为2-20位")
    private String name;

    @Min(value = 1, message = "角色类型不合法")
    @Max(value = 2, message = "角色类型不合法")
    private Integer type = 1;

    @Min(value = 0, message = "角色状态不合法")
    @Max(value = 1, message = "角色状态不合法")
    private Integer status = 1;

    private String admin;

    @Length(min = 0, max = 200, message = "角色备注长度需要在200个字符以内")
    private String remark;
}
