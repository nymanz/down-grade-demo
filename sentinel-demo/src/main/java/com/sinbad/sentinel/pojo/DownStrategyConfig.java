package com.sinbad.sentinel.pojo;


import com.alibaba.csp.sentinel.slots.block.RuleConstant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author sinbad on 2020/4/24
 */
@Data
public class DownStrategyConfig {


	/**
	 * 配置类型 降级规则
	 *
	 * @see ConfigTypeEnum
	 */
	private Integer configType;

	// 资源名字
	private String resourceName;
	/**
	 * 限流阈值类型， QPS（1） 或 并发线程数（0）
	 *
	 * @see RuleConstant#FLOW_GRADE_QPS
	 * @see RuleConstant#FLOW_GRADE_THREAD
	 */
	private Integer gradeType;

	// Qps or Thread num
	private Integer qpsOrThreadNum;

	// 窗口时间 秒
	private Integer windowTime;

	//流控效果
	private Integer controlBehavior;


	@Getter
	@AllArgsConstructor

	public enum ConfigTypeEnum {
		FlowRule(1, "流量控制规则"),
		DegradeRule(2, "熔断降级规则"),

		;
		private int key;
		private String name;
	}
}
