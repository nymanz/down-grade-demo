/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.csp.sentinel.dashboard.util.ZookeeperConfigUtil;

@Configuration
public class ZookeeperConfig {


	@Value("${sentinel.zookeeper.remoteAddress}")
	private String remoteAddress;

	@Bean
	public CuratorFramework zkClient() {
		CuratorFramework zkClient =
				CuratorFrameworkFactory.newClient(remoteAddress,
						new ExponentialBackoffRetry(ZookeeperConfigUtil.SLEEP_TIME, ZookeeperConfigUtil.RETRY_TIMES));
		zkClient.start();

		return zkClient;
	}
}