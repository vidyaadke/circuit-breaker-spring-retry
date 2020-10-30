package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootApplication
@RestController
public class CircuitBreakerRetryDemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerRetryDemoApplication.class, args);
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
}

/**
 * controllers
 */
@RestController
@RequestMapping("/api/client")
class CustomController {

	@Autowired
	private CustomService customService;

	@GetMapping("/store1")
	public String customerName() throws RuntimeException {
		try {
			//return robustService.getExternalCustomerName();
			return customService.resilientCustomerName();
		} catch (RuntimeException e) {
			throw new RuntimeException("InternalService is down");
		}

	}
}

@RestController
@RequestMapping("/api/internal")
class InternalService {

	@GetMapping("/store1")
	public String customerName() {
		System.out.println("called InternalService api/internal/store1");
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(2);
		if (randomInt < 2) {
			throw new CustomException("service is unavailable");
		}
		return "Vidya";

	}
	@GetMapping("/health")
	public String health(){
		System.out.println("inside store1 service");
		return "Health is Ok.";
	}
}

/**
 * service
 */

@Service
class CustomService {

	@Autowired RetryTemplate retryTemplate;

	public String getExternalCustomerName() {
		ResponseEntity<String> exchange = new RestTemplate().exchange("http://localhost:8083/api/internal/store1",
				HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
				});
		return exchange.getBody();
	}

	@CircuitBreaker(maxAttempts=3,openTimeout=15000l, resetTimeout=30000l)
	@Retryable
	public String resilientCustomerName() {
		return retryTemplate.execute(context -> {
			System.out.println(String.format("Retry count %d", context.getRetryCount()));
			return getExternalCustomerName();

		});
	}

	@Recover
	public String fallback(RuntimeException e) {
		System.out.println("returning from fallback method");
		return "Vidya-fallback";
	}

}

/**
 * Exceptions
 */
class CustomException extends RuntimeException {

	public CustomException(String message) {
		super(message);
	}
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
