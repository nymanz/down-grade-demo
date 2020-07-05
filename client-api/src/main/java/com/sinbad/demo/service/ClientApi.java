package com.sinbad.demo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientApi {





	@Resource
	private RestTemplate restTemplate;



	public String getDemoTimeout() {
		return restTemplate.getForObject("http://provider-demo/provider/get/timeout", String.class);
	}


	public String getDemoError() {
		return restTemplate.getForObject("http://provider-demo/provider/get/error", String.class);
	}




}
