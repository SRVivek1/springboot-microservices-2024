package com.srvivek.app.service;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import com.srvivek.app.exception.InvalidNameException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserService {

	@CircuitBreaker(name = "test-poc", fallbackMethod = "guestName")
	public String name(String user) {
		if (user == null || user.isBlank() || user.matches(".*\\d.*")) {
			//return "Guest";
			throw new InvalidNameException("Invalid name.");
		} else {
			return user;
		}
	}
	
	public String guestName(String str, Throwable t) {
		return "Guest";
	}
}
