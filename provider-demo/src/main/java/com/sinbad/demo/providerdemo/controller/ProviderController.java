package com.sinbad.demo.providerdemo.controller;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinbad.demo.pojo.DemoPojo;

@RestController
public class ProviderController {


	@GetMapping(value = "/provider/get")
	public DemoPojo getProvider() {

		DemoPojo demoPojo = new DemoPojo();
		demoPojo.setBirthday(new Date());
		demoPojo.setId(1L);
		demoPojo.setName(UUID.randomUUID().toString());
		return demoPojo;
	}


	@GetMapping(value = "/provider/get/timeout")
	public DemoPojo getProviderTimeout() throws InterruptedException {

		TimeUnit.SECONDS.sleep(RandomUtils.nextInt(3) + 1);

		DemoPojo demoPojo = new DemoPojo();
		demoPojo.setBirthday(new Date());
		demoPojo.setId(1L);
		demoPojo.setName(UUID.randomUUID().toString());
		return demoPojo;
	}

	@GetMapping(value = "/provider/get/error")
	public DemoPojo getProviderError() {
		throw new RuntimeException("fall back");
	}


}
