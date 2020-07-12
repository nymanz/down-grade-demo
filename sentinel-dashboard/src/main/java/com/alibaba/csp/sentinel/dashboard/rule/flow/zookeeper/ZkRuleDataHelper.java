package com.alibaba.csp.sentinel.dashboard.rule.flow.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import com.alibaba.csp.sentinel.dashboard.util.ZookeeperConfigUtil;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.sinbad.demo.enums.RuleTypePathEnum;

/**
 * @author sinbad on 2020/07/12.
 */
public class ZkRuleDataHelper<T> {

	private CuratorFramework curatorFramework;
	private Converter<String, List<T>> converterToData;
	private Converter<List<T>, String> converterToString;


	public ZkRuleDataHelper(CuratorFramework curatorFramework, Converter<String, List<T>> converterToData, Converter<List<T>, String> converterToString) {
		this.curatorFramework = curatorFramework;
		this.converterToData = converterToData;
		this.converterToString = converterToString;
	}

	public void publish(String appName, int ruleType, List<T> rules) throws Exception {

		String pathByType = getRulePath(ruleType);

		AssertUtil.notEmpty(appName, "app name cannot be empty");

		String path = ZookeeperConfigUtil.getPath(pathByType, appName);
		Stat stat = curatorFramework.checkExists().forPath(path);
		if (stat == null) {
			curatorFramework.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
		}
		byte[] data = CollectionUtils.isEmpty(rules) ? "[]".getBytes() : converterToString.convert(rules).getBytes();
		curatorFramework.setData().forPath(path, data);
	}


	public List<T> getRules(String appName, int ruleType) throws Exception {
		String pathByType = getRulePath(ruleType);
		String zkPath = ZookeeperConfigUtil.getPath(pathByType, appName);
		Stat stat = curatorFramework.checkExists().forPath(zkPath);
		if (stat == null) {
			return new ArrayList<>(0);
		}
		byte[] bytes = curatorFramework.getData().forPath(zkPath);
		if (null == bytes || bytes.length == 0) {
			return new ArrayList<>();
		}
		String resource = new String(bytes);

		return converterToData.convert(resource);
	}


	private String getRulePath(int ruleType) {
		String pathByType = RuleTypePathEnum.getPathByType(ruleType);

		AssertUtil.notEmpty(pathByType, "rule pathByType cannot be empty");
		return pathByType;
	}


}
