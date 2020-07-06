package com.sinbad.demo.webdemo.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinbad.demo.pojo.DemoPojo;
import com.sinbad.demo.webdemo.service.ProviderServiceBulkheadImpl;
import com.sinbad.demo.webdemo.service.SentinelServiceImpl;

@RestController
@RequestMapping(value = "/sentinel")
public class SentinelTestController {


	@Resource
	private SentinelServiceImpl sentinelService;


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


}
