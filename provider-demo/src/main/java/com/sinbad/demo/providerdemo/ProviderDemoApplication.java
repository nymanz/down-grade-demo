package com.sinbad.demo.providerdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProviderDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProviderDemoApplication.class, args);
	}

}
