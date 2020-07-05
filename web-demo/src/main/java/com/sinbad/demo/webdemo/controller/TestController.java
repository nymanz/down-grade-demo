package com.sinbad.demo.webdemo.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {



	@Resource
	private RestTemplate restTemplate;



	@GetMapping(value = "/demo/get")
	public DemoPojo getDemo() {
		restTemplate.
	}

}
