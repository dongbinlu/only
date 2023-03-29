package com.safecode.cyberzone.authorize.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class SysRole {
    private Integer id;

    private String name;

    private String platformName;

    private Integer type;

    private Integer status;

    private String admin;

    private String remark;

    private String operator;

    private Date operateTime;

    private String creater;

    private Date createTime;

    private String operateIp;

}