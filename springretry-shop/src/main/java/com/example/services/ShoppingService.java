package com.example.services;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
@Service
public class ShoppingService {
	int retry =0;
	@Retryable(value = { SQLException.class }, maxAttempts = 3)
	public String simpleRetry() throws SQLException {
		retry++;
		System.out.println("Shopping Service Failed "+ retry);
		throw new SQLException();
	}
	@Recover
	public String recover(SQLException t){
		System.out.println("Service recovering");
		return "recovered from shopping service failure.";
	}
}