package com.srvivek.sboot.mservices.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		// All requests must be authorized
		// Else return HTTP 403
		httpSecurity.authorizeHttpRequests(
				authorizationManagerRequestMatcherRegistryCustomizer -> authorizationManagerRequestMatcherRegistryCustomizer
						.anyRequest().authenticated());

		// Prompt for authentication if request is not authorized
		// Using default customizer
		httpSecurity.httpBasic(Customizer.withDefaults());

		/*
		 * Disabling CSRF as it may cause issue with HTTP methods - POST & PUT.
		 * 
		 * if enabled, Keep prompting for user credentials for post request.
		 */
		httpSecurity.csrf(csrf -> csrf.disable());

		return httpSecurity.build();
	}
}
