package com.example1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CircuitBreakerCardStore1DemoApplication {

	@GetMapping("/store1")
	@CircuitBreaker(maxAttempts=3,openTimeout=15000l, resetTimeout=30000l)
	public String readingList(){
		System.out.println("inside store1 service");
		return "Reply from store1.readingList";
	}

	@GetMapping("/health")
	public String health(){
		System.out.println("inside store1 service");
		return "Health is Ok.";
	}

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();

		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(1000l);
		retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		retryPolicy.setMaxAttempts(3);
		retryTemplate.setRetryPolicy(retryPolicy);

		return retryTemplate;
	}
	public static void main(String[] args)
	{
		System.out.println("inside main store1");
		SpringApplication.run(CircuitBreakerCardStore1DemoApplication.class, args);
	}

}
