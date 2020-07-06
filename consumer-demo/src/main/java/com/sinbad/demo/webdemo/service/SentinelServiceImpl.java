package com.sinbad.demo.webdemo.service;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("Do try, uuid={} date={}", UUID.randomUUID(), new Date().toString());
		try {
			TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10));
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}


	@SentinelResource(value = "SentinelServiceImpl.sentinelError")
	public String sentinelError() {

		try {
			return String.valueOf(1 / RandomUtils.nextInt(2));
		} catch (Exception e) {
			throw e;
		}
	}

}
