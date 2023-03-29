package com.safecode.security.permission.param;


import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestVo {
    //字符串
    @NotBlank
    private String msg;

    //Integer
    @NotNull
    private Integer id;

    //集合
    @NotEmpty
    private List<String> list;
}
