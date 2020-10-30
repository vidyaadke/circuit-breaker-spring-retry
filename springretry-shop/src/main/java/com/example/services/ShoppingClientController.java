package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping(value="/shopping")
public class ShoppingClientController {
	@Autowired
	private com.example.services.ShoppingService shoppingService;
	@GetMapping("/retry")
	public String callRetryService() throws SQLException { return shoppingService.simpleRetry();
			}

	@GetMapping("/health")
	public String health(){
		System.out.println("inside store1 service");
		return "Health is Ok.";
	}
}