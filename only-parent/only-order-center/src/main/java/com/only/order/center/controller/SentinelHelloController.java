package com.only.order.center.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sentinel")
public class SentinelHelloController {

    @PostConstruct
    public void init() {
        List<FlowRule> flowRules = new ArrayList<>();

        // 创建流控规则对象
        FlowRule flowRule = new FlowRule();
        //设置流控规则QPS
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // 设置受保护的资源
        flowRule.setResource("HelloSentinel");
        // 设置受保护资源的阈值
        flowRule.setCount(1);

        flowRules.add(flowRule);

        // 加载配置好的规则
        FlowRuleManager.loadRules(flowRules);
    }


    @GetMapping("/hello")
    @SentinelResource(value = "HelloSentinel", blockHandler = "blockMethod")
    public String helloSentinel() {
        return "hello";
    }

    public String blockMethod(BlockException e){
        return "被流控了";
    }


}
