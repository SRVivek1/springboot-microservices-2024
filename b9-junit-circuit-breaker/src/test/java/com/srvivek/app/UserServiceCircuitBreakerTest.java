package com.srvivek.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.srvivek.app.service.UserService;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@SpringBootTest
public class UserServiceCircuitBreakerTest {

    @Autowired
    UserService userService;

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry;

    CircuitBreaker cb;

    @BeforeEach
    void setUp() {
        cb = circuitBreakerRegistry.circuitBreaker("test-poc");
        // Reset state before each test to ensure deterministic behavior
        cb.reset();
    }

    @Test
    void validName_whenCircuitClosed_returnsName() {
        String name = userService.name("Alice");
        assertEquals("Alice", name);
        // Circuit should remain closed for a successful call
        assertEquals(CircuitBreaker.State.CLOSED, cb.getState());
    }

    @Test
    void repeatedFailures_openCircuit_and_useFallback() {
        // Make four failing calls. Config requires minimumNumberOfCalls=4,
        // failureRateThreshold=70 -> 4 failures = 100% -> circuit opens.
        for (int i = 0; i < 4; i++) {
            // invalid names cause InvalidNameException inside UserService
            assertThrows(RuntimeException.class, () -> userService.name("123"));
        }

        // After the failures, circuit should be OPEN
        org.junit.jupiter.api.Assertions.assertEquals(CircuitBreaker.State.OPEN, cb.getState());

        // Now, even for a valid name, the fallback should be executed and return "Guest"
        String result = userService.name("Bob");
        assertEquals("Guest", result);
    }
}
