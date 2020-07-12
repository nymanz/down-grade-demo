package com.sinbad.demo.webdemo.service;


import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sinbad.demo.pojo.DemoPojo;

@Service
public class ProviderServiceBulkheadImpl {


	@Resource
	private RestTemplate restTemplate;


	@SentinelResource(value = "ProviderServiceBulkheadImpl.getDemo")
	public DemoPojo getDemo() {

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return restTemplate.getForObject("http://provider-demo/provider/get", DemoPojo.class);
	}
	@SentinelResource(value = "ProviderServiceBulkheadImpl.getDemo2")
	public DemoPojo getDemo2() {

		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return restTemplate.getForObject("http://provider-demo/provider/get", DemoPojo.class);
	}


	@HystrixCommand(
			threadPoolKey = "provider-demo",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "3"),
					@HystrixProperty(name = "maximumSize", value = "10"),
					@HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
					@HystrixProperty(name = "keepAliveTimeMinutes", value = "100"),
					@HystrixProperty(name = "keepAliveTimeMinutes", value = "100"),

			},
			commandProperties = {
					//执行的超时时间
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					@HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "2000"),
			}, fallbackMethod = "fallBack"
	)
	public DemoPojo getDemoTimeout() {
		return restTemplate.getForObject("http://provider-demo/provider/get/timeout", DemoPojo.class);
	}



	//触发打开断路器的配置
	@HystrixCommand(
			commandProperties = {
					//监控 窗口时间
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "3000"),
					//请求数
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
					//失败请求率
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					//重试时间窗口
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds ", value = "3000"),
			}, fallbackMethod = "fallBack"
	)
	public DemoPojo getDemoError() {
		return restTemplate.getForObject("http://provider-demo/provider/get/error", DemoPojo.class);
	}


	public DemoPojo fallBack() {

		DemoPojo demoPojo = new DemoPojo();
		demoPojo.setName("Fallback");
		return demoPojo;

	}

}
