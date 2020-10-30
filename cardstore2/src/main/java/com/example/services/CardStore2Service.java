package com.example.services;

import org.springframework.stereotype.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class CardStore2Service {
	private final RestTemplate restTemplate;

	public CardStore2Service(RestTemplate rest) {
		this.restTemplate = rest;
	}


	public String getValue() {
		System.out.println("inside store2 service");
		URI uri = URI.create("http://localhost:8081/store1");

		return this.restTemplate.getForObject(uri, String.class);
	}

	public String reliable() {
		return "Reply from CardStore2Service.reliable";
	}
}
