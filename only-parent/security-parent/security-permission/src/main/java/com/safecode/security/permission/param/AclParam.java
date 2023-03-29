package com.safecode.security.permission.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AclParam {
    private Integer id;

    @NotBlank(message = "权限点名称不可以为空")
    @Length(min = 2, max = 20, message = "权限点名称需要在2-20个字以内")
    private String name;

    @NotNull(message = "必须指定权限模块id")
    private Integer aclModuleId;

    @Length(min = 2, max = 100, message = "权限点url需要在2-200个字符以内")
    private String url;

    @NotNull(message = "必须指定权限点类型")
    @Min(value = 1, message = "权限点类型不合法")
    @Max(value = 3, message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "必须指定权限点状态")
    @Min(value = 0, message = "权限点状态不合法")
    @Max(value = 1, message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq;

    @Length(max = 200, message = "权限点备注长度需要在200字以内")
    private String remark;

}
