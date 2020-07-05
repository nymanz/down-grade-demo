package com.sinbad.demo.providerdemo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinbad.demo.pojo.DemoPojo;

@RestController
public class ProviderController {


	@GetMapping(value = "/provider/get")
	public DemoPojo getProvider() {

		return null;
	}


}
