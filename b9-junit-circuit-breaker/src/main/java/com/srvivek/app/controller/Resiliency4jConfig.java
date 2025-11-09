package com.srvivek.app.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.srvivek.app.exception.InvalidNameException;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Configuration
public class Resiliency4jConfig {

	// Optimized configuration to open the circuit after 3 failures and wait for 5
	// minutes
	@Bean
	CircuitBreakerConfig circuitBreakerConfig() {
		return CircuitBreakerConfig.custom()
				// 1. Set the failure rate high enough to capture 3 failures out of the window
				// size.
				// If the window size is 4, 3/4 = 75%. Setting the threshold to 70% ensures it
				// opens.
				.failureRateThreshold(70)

				// 2. Set the duration the circuit will remain in the open state (5 minutes).
				.waitDurationInOpenState(Duration.ofMinutes(2))

				// 3. Set the minimum number of calls required before the circuit breaker can
				// calculate
				// the failure rate and transition from CLOSED to OPEN. Set to 4 to ensure a
				// reliable
				// sample size for 3 failures.
				.minimumNumberOfCalls(4)

				// 4. Use COUNT_BASED window to precisely track 4 recent calls.
				.slidingWindowType(SlidingWindowType.COUNT_BASED)

				// 5. Set the window size to 4.
				.slidingWindowSize(4)

				// 6. Define which exceptions count as a failure.
				// We'll use RuntimeException.class, but you can list specific exceptions.
				.recordExceptions(InvalidNameException.class, RuntimeException.class)

				// Keep the slow call config, but it's not strictly necessary for this POC
				// failure test.
				.slowCallRateThreshold(100) // Disable slow call metric for this test
				.slowCallDurationThreshold(Duration.ofSeconds(60))

				// Half-open is the state after 5 mins, waiting to test the service.
				// 3 test calls will be made in the half-open state before deciding to close or
				// re-open.
				.permittedNumberOfCallsInHalfOpenState(3)

				.build();
	}

	@Bean
	CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
		return CircuitBreakerRegistry.of(circuitBreakerConfig);
	}

	@Bean
	CircuitBreaker circuitBreakerWithDefaultConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
		return circuitBreakerRegistry.circuitBreaker("default");
	}

	@Bean
	CircuitBreaker circuitBreakerTestPocConfig(CircuitBreakerRegistry circuitBreakerRegistry,
			CircuitBreakerConfig circuitBreakerConfig) {
		return circuitBreakerRegistry.circuitBreaker("test-poc", circuitBreakerConfig);
	}
}
