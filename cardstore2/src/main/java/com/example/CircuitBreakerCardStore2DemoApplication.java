package com.example;

import com.example.services.CardStore2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class CircuitBreakerCardStore2DemoApplication {

	@Autowired
	private CardStore2Service store2Service;

	@Bean
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}

	@GetMapping("/store2")
	public String toRead() {
		return store2Service.getValue();
	}

	@GetMapping("/health")
	public String health(){
		System.out.println("inside store1 service");
		return "Health is Ok.";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerCardStore2DemoApplication.class, args);
	}

}
