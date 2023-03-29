package com.safecode.cyberzone.authorize.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class SysLog {
    private Long id;

    private Long userId;

    private String userName;

    private Date createTime;

    private String ip;

    private String logStatus;

    private String remark;

    private String projectName;

    private String requestUrl;

    private String requestObject;


}