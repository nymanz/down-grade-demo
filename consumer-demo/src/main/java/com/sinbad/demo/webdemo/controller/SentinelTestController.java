package com.sinbad.demo.webdemo.controller;


import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinbad.demo.utils.GsonUtil;
import com.sinbad.demo.webdemo.service.SentinelServiceImpl;

@RestController
@RequestMapping(value = "/sentinel")
public class SentinelTestController {


	@Resource
	private SentinelServiceImpl sentinelService;

	@Resource
	private CuratorFramework curatorFramework;


	@GetMapping(value = "/get/default")
	public String sentinelDefault() {

		return sentinelService.sentinelDefault();
	}

	@GetMapping(value = "/get/timeout")
	public String sentinelTimeOut() {

		return sentinelService.sentinelTimeOut();
	}

	@GetMapping(value = "/get/error")
	public String sentinelError() {

		return sentinelService.sentinelError();
	}

	@GetMapping(value = "/get/config")
	public Object getSentinelConfig() {


		NodeCache nodeCache = new NodeCache(curatorFramework, "/staging/miuivip/config/sentinel/consumer-demo");

		ChildData currentData = nodeCache.getCurrentData();
		byte[] data = currentData.getData();
		System.out.println(GsonUtil.toJson(currentData));
		System.out.println(GsonUtil.toJson(new String(data)));

		return currentData;
	}




}
