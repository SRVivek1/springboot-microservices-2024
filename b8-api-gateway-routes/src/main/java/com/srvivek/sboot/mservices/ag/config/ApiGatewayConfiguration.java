package com.srvivek.sboot.mservices.ag.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to build custom routes and request customization.
 */
@Configuration
public class ApiGatewayConfiguration {

	/**
	 * Define routes.
	 * 
	 * @param routeLocatorBuilder
	 * @return
	 */
	@Bean
	RouteLocator getwayRoute(RouteLocatorBuilder routeLocatorBuilder) {
		/*
		 * Redirect request to external service. Also, optionally we can add http
		 * headers and request params in the request.
		 */
		Builder routeLocator = routeLocatorBuilder.routes()
				.route((p) -> p.path("/get")
						.filters(f -> f.addRequestHeader("MY-HEADER", "MY-CUSTOM-HEADER")
								.addRequestParameter("MY-REQUEST-PARAM", "MY-CUSTOM-REQUEST-PARAM"))
						.uri("http://httpbin.org:80/"));

		/**
		 * Route URLs to load balancer and eureka service name
		 */
		routeLocator = routeLocator.route(p -> p.path("/currency-exchange/**").uri("lb://b3-currency-exchange-service"))
				.route(p -> p.path("/currency-conversion-feign/**")
						.uri("lb://b5-currency-conversion-service-openfeign"));

		/**
		 * Rewrite URL and copy the path
		 */
		routeLocator.route(p -> p.path("/ccfs/**")
				.filters(f -> f.rewritePath("/ccfs/(?<segment>.*)", "/currency-conversion-feign/${segment}"))
				.uri("lb://b5-currency-conversion-service-openfeign"));

		return routeLocator.build();
	}
}
