package com.sinbad.demo.webdemo.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.reflect.TypeToken;
import com.sinbad.demo.enums.RuleTypePathEnum;
import com.sinbad.demo.utils.GsonUtil;

/**
 * @author sinbad on 2020/07/07.
 */
@Component
public class SentinelConfigLoader {

	private static final int RETRY_TIMES = 3;
	private static final int SLEEP_TIME = 1000;

	private static final String CONFIG_ROOT = "/miuivip";

	@Value("${sentinel.zookeeper.remoteAddress}")
	private String remoteAddress;


	//需要与配置中心的保持一致
	@Value("${sentinel.zookeeper.configPath}")
	private String configPath;

	//一般都是本项目名字
	//需要与注册到配置中心的保持一致
	@Value("${sentinel.zookeeper.projectName}")
	private String projectName;


	@PostConstruct
	public void loadRules() {

		//流控规则数据源
		ReadableDataSource<String, List<FlowRule>> flowRuleDataSource =
				new ZookeeperDataSource<>(remoteAddress, CONFIG_ROOT + configPath + RuleTypePathEnum.FLOW.getTypePath() + "/" + projectName,
						source -> GsonUtil.fromJson(source, new TypeReference<List<FlowRule>>() {
						}.getType()));
		FlowRuleManager.register2Property(flowRuleDataSource.getProperty());


		//流控规则数据源
		ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource =
				new ZookeeperDataSource<>(remoteAddress, CONFIG_ROOT + configPath + RuleTypePathEnum.DEGRADE.getTypePath() + "/" + projectName,
						source -> GsonUtil.fromJson(source, new TypeToken<List<DegradeRule>>() {
						}.getType()));
		DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
	}


	@Bean
	public CuratorFramework getCuratorFramework() {

		CuratorFramework zkClient =
				CuratorFrameworkFactory.newClient(remoteAddress,
						new ExponentialBackoffRetry(SLEEP_TIME, RETRY_TIMES));
		zkClient.start();

		return zkClient;
	}

//	public static void main(String[] args) {
//		String zkServers = ZKFacade.getZKSettings().getZKServers(ZKFacade.getZKSettings().getEnvironmentType());
//		System.out.println(zkServers);
//	}

}
