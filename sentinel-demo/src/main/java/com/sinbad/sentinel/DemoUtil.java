package com.sinbad.sentinel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import sun.misc.PerfCounter;

/**
 * @author sinbad on 2020/07/02.
 */
public class DemoUtil {


	public static void main(String[] args) {
		String sentinelJsonConfig = "";
		if (StringUtils.isEmpty(sentinelJsonConfig)) {


			initLivePostFlowQpsRule(sentinelJson, resourceName);

		}
	}


	private void initLivePostFlowQpsRule(String configSentinel,
										 String resourceName) {
		if (CommonUtil.isNullOrBlank(configSentinel) || CommonUtil.isNullOrBlank(resourceName)) {
			logger.error("config is null");
			return;
		}
		LogHelper.debugEx(vipContext, logger,
				"initLivePostFlowQpsRule  resourceName={} markConfigVersion={} configSentinel={}",
				resourceName, markConfigVersion, configSentinel);
		final SentinelConfig sentinelConfig = GsonUtil.fromJson(configSentinel,
				SentinelConfig.class);
		if (sentinelConfig != null) {
			if (markConfigVersion != null
					&& markConfigVersion.equals(sentinelConfig.getConfigVersion())) {
				return;
			}
			if (resourceName.equals(sentinelConfig.getResourceName())) {
				if (sentinelConfig.getQpsNum() != null) {
					markConfigVersion = sentinelConfig.getConfigVersion();
					List<FlowRule> rules = new ArrayList<>();
					FlowRule rule = new FlowRule(resourceName);
					// set limit qps to 20
					rule.setCount(sentinelConfig.getQpsNum());
					rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
					rule.setLimitApp("default");
					rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER);
					rules.add(rule);
					logger.info("Do update rules is ={}", GsonUtil.toJson(rules));
					FlowRuleManager.loadRules(rules);
				}
			}
		}
	}


}
