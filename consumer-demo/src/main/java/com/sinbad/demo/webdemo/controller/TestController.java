package com.sinbad.demo.webdemo.controller;


import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sinbad.demo.pojo.DemoPojo;
import com.sinbad.demo.webdemo.service.ProviderServiceBulkheadImpl;
import com.sinbad.demo.webdemo.service.ProviderServiceImpl;

@RestController
public class TestController {


//	@Resource
//	private ProviderServiceImpl providerService;
	@Resource
	private ProviderServiceBulkheadImpl providerService;


	@GetMapping(value = "/demo/get")
	public DemoPojo getDemo() {
		DemoPojo demo = providerService.getDemo();
		demo = providerService.getDemo2();

		return demo;
	}

	@GetMapping(value = "/demo/get/timeout")
	public DemoPojo getDemoTimeout() {

		return providerService.getDemoTimeout();
	}

	@GetMapping(value = "/demo/get/error")
	public DemoPojo getDemoError() {

		return providerService.getDemoError();
	}

}
