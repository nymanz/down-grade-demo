package com.sinbad.demo.webdemo.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import com.sinbad.demo.pojo.DemoPojo;

@Service
public class ProviderServiceImpl {


	@Resource
	private RestTemplate restTemplate;


	public DemoPojo getDemo() {
		return restTemplate.getForObject("http://provider-demo/provider/get", DemoPojo.class);
	}


	@HystrixCommand(
			commandProperties = {
					//执行的超时时间
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
			}, fallbackMethod = "fallBack"
	)
	public DemoPojo getDemoTimeout() {
		return restTemplate.getForObject("http://provider-demo/provider/get/timeout", DemoPojo.class);
	}



	@HystrixCommand(
			commandProperties = {
					//执行的超时时间
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
			}, fallbackMethod = "fallBack"
	)
	public DemoPojo getDemoError() {
		return restTemplate.getForObject("http://provider-demo/provider/get/error", DemoPojo.class);
	}




	@HystrixCommand(
			commandProperties = {
					//执行的超时时间
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
			}, fallbackMethod = "fallBack"
	)
	public DemoPojo getDemoOpenCircuit() {
		return restTemplate.getForObject("http://provider-demo/provider/get/error", DemoPojo.class);
	}



	public DemoPojo fallBack() {

		DemoPojo demoPojo = new DemoPojo();
		demoPojo.setName("Fallback");
		return demoPojo;

	}

}
