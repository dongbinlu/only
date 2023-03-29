package com.safecode.cyberzone.base.log;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {

    private String remark; // 操作内容
    private String projectName;// 项目名称
    private String requestObject; // 请求参数
    private String requestUrl;// 请求url
    private String method; // 请求方法名
    private String logStatus;//

    private String id;
    private String userId;
    private String username;
    private String ip;
    private Date createTime;

}
