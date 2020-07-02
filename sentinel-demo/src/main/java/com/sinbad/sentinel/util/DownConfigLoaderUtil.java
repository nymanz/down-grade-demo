package com.sinbad.sentinel.util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.google.gson.reflect.TypeToken;
import com.sinbad.sentinel.pojo.DownStrategyConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sinbad on 2020/07/02.
 */
@Slf4j
public class DownConfigLoaderUtil {

	private static List<DownStrategyConfig> localConfigList = null;


	public static void loadDownStrategyConfig() {
		String sentinelJsonConfig = "";
		if (!StringUtils.isEmpty(sentinelJsonConfig)) {
			{
				try {
					localConfigList = GsonUtil.fromJson(sentinelJsonConfig, new TypeToken<List<DownStrategyConfig>>() {
					}.getType());
				} catch (Exception e) {
					log.error("Turn config to entry failed, sentinelJsonConfig={}", sentinelJsonConfig);
				}
			}
			if (!CollectionUtils.isEmpty(localConfigList)) {
				initDownStrategyConfig(localConfigList);
			}
		}
	}


	private static boolean checkConfigInfo(DownStrategyConfig downStrategyConfig) {
		if (downStrategyConfig == null) {
			return false;
		}

		return true;
	}


	private static void initDownStrategyConfig(List<DownStrategyConfig> configList) {

		if (CollectionUtils.isEmpty(configList)) {
			return;
		}

		List<DownStrategyConfig> configListTemp = configList.stream()
				.filter(DownConfigLoaderUtil::checkConfigInfo)
				.collect(Collectors.toList());


		for (DownStrategyConfig config : configListTemp) {
			Integer configType = config.getConfigType();
			String resourceName = config.getResourceName();
			if (configType == DownStrategyConfig.ConfigTypeEnum.FlowRule.getKey()) {
				FlowRule flowRule = new FlowRule(resourceName);

				//Rate limiter control behavior.
				//0. default(reject directly), 1. warm up, 2. rate limiter, 3. warm up + rate limiter
				flowRule.setControlBehavior(config.getControlBehavior());

				flowRule.setCount(config.getQpsOrThreadNum());

				flowRule.setGrade(config.getGradeType());
				// 调用关心限流策略：直接，链路，关联 根据资源本身（直接）
				flowRule.setStrategy(0);
				flowRule.setWarmUpPeriodSec(1);

				flowRule.setMaxQueueingTimeMs(RuleConstant.FLOW_GRADE_QPS);

				FlowRuleManager.loadRules(Collections.singletonList(flowRule));
			} else if (configType == DownStrategyConfig.ConfigTypeEnum.DegradeRule.getKey()) {
				DegradeRule degradeRule = new DegradeRule(resourceName);
				//RT threshold or exception ratio threshold count.
				degradeRule.setCount(config.getQpsOrThreadNum());
				degradeRule.setGrade(config.getGradeType());

				degradeRule.setMinRequestAmount(1);

				degradeRule.setTimeWindow(config.getWindowTime());

				degradeRule.setRtSlowRequestAmount(1);

				if (DegradeRuleManager.isValidRule(degradeRule)) {
					DegradeRuleManager.loadRules(Collections.singletonList(degradeRule));
				}
			}
		}

	}

}
