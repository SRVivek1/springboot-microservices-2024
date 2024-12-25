package com.srvivek.sboot.mservices.cb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.cb.exception.CustomRetryRuntimeException;
import com.srvivek.sboot.mservices.cb.exception.DefaultRetryRuntimeException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class HelloWorldControllerCircuitBreaker {

	private static final Logger logger = LoggerFactory.getLogger(HelloWorldControllerCircuitBreaker.class);
	private Integer counter = 1;

	/**
	 * Retry default return failure after 3 retries.
	 * 
	 * @return
	 */
	@GetMapping("/cb/greet")
	// @Retry(name = "default", fallbackMethod = "hardcodedResponseFallbackMethod")
	@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponseFallbackMethod")
	public String greeting() {

		logger.info("***** HelloWorldController.greeting() method called.");
		logger.info("***** Request id : {}", counter);
		try {
			if (counter % 2 == 0) {
				throw new DefaultRetryRuntimeException("curcuite breacker test. Request Id : " + counter);
			}
		} catch (Exception ex) {
			logger.info("**** Failed for request id : {}", counter);
			throw ex;
		} finally {
			counter++;
		}

		return "Guten Morgen";
	}

	/**
	 * Circuit breaker fallback method.
	 * 
	 * @param ex
	 */
	public String hardcodedResponseFallbackMethod(Exception ex) {

		if (ex instanceof DefaultRetryRuntimeException) {
			return "Guten Morgen, default resonse for DefaultRetryRuntimeException";
		} else if (ex instanceof CustomRetryRuntimeException) {
			return "Guten Morgen, default resonse for CustomRetryRuntimeException";
		} else {
			return "default response.";
		}
	}
}
