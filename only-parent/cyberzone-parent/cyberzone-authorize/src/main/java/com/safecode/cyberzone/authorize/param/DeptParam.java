package com.safecode.cyberzone.authorize.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不可以为空")
    @Length(min = 2, max = 15, message = "部门名称长度需要在2—15个字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;

    @Length(max = 200, message = "logo的长度需要在200字以内")
    private String icon;

    @Length(max = 150, message = "备注的长度需要在150字以内")
    private String remark;

}
