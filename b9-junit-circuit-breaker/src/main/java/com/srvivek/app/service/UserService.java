package com.srvivek.app.service;

import java.util.logging.Logger;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.srvivek.app.exception.InvalidNameException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserService {

	Logger log = Logger.getLogger(UserService.class.getName());
	
	@CircuitBreaker(name = "test-poc", fallbackMethod = "guestName")
	public String name(String user) {
		
		log.info("Circuit breaker is close, executing main method.");
		if (user == null || user.isBlank() || user.matches(".*\\d.*")) {
			//return "Guest";
			throw new InvalidNameException("Invalid name.");
		} else {
			return user;
		}
	}
	
	public String guestName(String str, Throwable t) {
		log.info("Circuit breaker is open, fallback to default method.");
		log.info("String - " + str);
		log.info("Throwable - " + t.getMessage());
		
		return "Guest";
	}
}
