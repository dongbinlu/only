package com.only.order.center.handler;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.only.order.center.api.CommonResult;
import com.only.order.center.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 流控降级错误优化
 */
@Slf4j
@Component
public class FlowUrlBlockHandler implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws IOException {

        if (ex instanceof FlowException) {
            log.warn("触发了流控");
            warrperResponse(response, ErrorEnum.FLOW_RULE_ERR);
        } else if (ex instanceof ParamFlowException) {
            log.warn("触发了参数流控");
            warrperResponse(response, ErrorEnum.HOT_PARAM_FLOW_RULE_ERR);
        } else if (ex instanceof AuthorityException) {
            log.warn("触发了授权规则");
            warrperResponse(response, ErrorEnum.AUTH_RULE_ERR);
        } else if (ex instanceof SystemBlockException) {
            log.warn("触发了系统规则");
            warrperResponse(response, ErrorEnum.SYS_RULE_ERR);
        } else {
            log.warn("触发了降级规则");
            warrperResponse(response, ErrorEnum.DEGRADE_RULE_ERR);
        }
    }

    private void warrperResponse(HttpServletResponse httpServletResponse, ErrorEnum errorEnum) throws IOException {

        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content‐Type", "application/json;charset=utf‐8");
        httpServletResponse.setContentType("application/json;charset=utf‐8");

        ObjectMapper objectMapper = new ObjectMapper();
        String errMsg = objectMapper.writeValueAsString(CommonResult.failed(errorEnum.getCode(), errorEnum.getMsg()));
        httpServletResponse.getWriter().write(errMsg);
    }

}
