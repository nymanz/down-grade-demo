package com.sinbad.demo.webdemo.service;


import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

@Service
public class SentinelServiceImpl {



	@SentinelResource(value = "SentinelServiceImpl.default")
	public String sentinelDefault() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}



	@SentinelResource(value = "SentinelServiceImpl.sentinelTimeOut")
	public String sentinelTimeOut() {

		try {
			TimeUnit.SECONDS.sleep(RandomUtils.nextInt(1000));
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}


	@SentinelResource(value = "SentinelServiceImpl.sentinelError")
	public String sentinelError() {

		try {
			return String.valueOf(1 / RandomUtils.nextInt(5));
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}

}
