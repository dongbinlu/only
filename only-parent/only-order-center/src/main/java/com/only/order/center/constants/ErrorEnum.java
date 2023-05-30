package com.only.order.center.constants;

import lombok.Getter;
import lombok.Setter;

public enum ErrorEnum {

    FLOW_RULE_ERR(-1, "触发了流控"),
    HOT_PARAM_FLOW_RULE_ERR(-2, "触发了参数流控"),
    AUTH_RULE_ERR(-3, "触发了授权规则"),
    SYS_RULE_ERR(-4, "触发了系统规则"),
    DEGRADE_RULE_ERR(-5, "触发了降级规则");

    @Setter
    @Getter
    private Integer code;

    @Setter
    @Getter
    private String msg;

    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
