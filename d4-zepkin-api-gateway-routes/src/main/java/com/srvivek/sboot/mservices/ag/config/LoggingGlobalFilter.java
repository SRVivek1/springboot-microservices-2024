package com.srvivek.sboot.mservices.ag.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalFilter implements GlobalFilter {

	private static Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		// log the request
		logger.info("Request -> Method: {}, Path: {}", exchange.getRequest().getMethod(),
				exchange.getRequest().getPath());

		return chain.filter(exchange);
	}

}
