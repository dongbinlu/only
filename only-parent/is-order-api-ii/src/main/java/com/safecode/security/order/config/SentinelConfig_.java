package com.safecode.security.order.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

//在Spring项目启动完，所有的Bean都组装好
@Component
public class SentinelConfig_ implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //用sentinel-dashboard配置，zookeeper为配置中心

/*		
		//流控规则
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();
		rule.setResource("createOrder");
		//流控策略
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule.setCount(10);
		rules.add(rule);
		FlowRuleManager.loadRules(rules);
		
		//降级规则
		List<DegradeRule> degradeRules = new ArrayList<>();
		DegradeRule degradeRule = new DegradeRule();
		degradeRule.setResource("createOrder");
		//以响应时间做策略
		degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
		degradeRule.setCount(10);
		degradeRule.setTimeWindow(10);
		degradeRules.add(degradeRule);
		DegradeRuleManager.loadRules(degradeRules);
		*/

    }

}
