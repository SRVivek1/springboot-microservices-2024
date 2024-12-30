package com.srvivek.sboot.mservices.ccs.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.srvivek.sboot.mservices.ccs.bean.CurrencyConversion;
import com.srvivek.sboot.mservices.ccs.proxy.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Convert currency using {@link RestTemplate}
	 * 
	 * @param from
	 * @param to
	 * @param quantity
	 * @return
	 */
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		logger.info("Executing CurrencyConversionController.calculateCurrencyConversion(..) API.");

		// Standardize
		from = from.toUpperCase();
		to = to.toUpperCase();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		/*
		 * Send request to Currency exchange micro-service.
		 * 
		 * Note: The localhost URL will not work inside docker. Use system property or
		 * service name to get working URL.
		 */
		final ResponseEntity<CurrencyConversion> response = restTemplate.getForEntity(
				"http://localhost:8000/jpa/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
				uriVariables);

		final CurrencyConversion currencyConversionExchange = response.getBody();

		logger.debug("Response from currency-exchange : {}", currencyConversionExchange);

		final CurrencyConversion currencyConversion = new CurrencyConversion(currencyConversionExchange.getId(), from,
				to, quantity, currencyConversionExchange.getConversionMultiples(),
				quantity.multiply(currencyConversionExchange.getConversionMultiples()),
				currencyConversionExchange.getEnvironment());

		logger.debug("Response returned : {}", currencyConversionExchange);

		return currencyConversion;
	}

	/**
	 * Currency conversion using feign client.
	 * 
	 * @param from
	 * @param to
	 * @param quantity
	 * @return
	 */
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		logger.info("Executing CurrencyConversionController.calculateCurrencyConversionFeign(..) API.");

		// Standardize
		from = from.toUpperCase();
		to = to.toUpperCase();

		// Send request to Currency exchange micro-service
		final CurrencyConversion currencyConversionExchange = currencyExchangeProxy
				.retrieveExchangeRateFromDatabase(from, to);

		logger.debug("Response from currency-exchange : {}", currencyConversionExchange);

		final CurrencyConversion currencyConversion = new CurrencyConversion(currencyConversionExchange.getId(), from,
				to, quantity, currencyConversionExchange.getConversionMultiples(),
				quantity.multiply(currencyConversionExchange.getConversionMultiples()),
				currencyConversionExchange.getEnvironment());

		logger.debug("Response returned : {}", currencyConversionExchange);

		return currencyConversion;
	}
}
