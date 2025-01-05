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

@RestController
public class CurrencyConversionController {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

	@Autowired
	private RestTemplate restTemplate;

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

		// Send request to Currency exchange micro-service
		// final ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity(
		final ResponseEntity<CurrencyConversion> response = restTemplate.getForEntity(
				"http://localhost:8000/jpa/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
				uriVariables);
		final CurrencyConversion currencyConversion = response.getBody();

		logger.debug("Response from currency-exchange : {}", currencyConversion);

		currencyConversion.setQuantity(quantity);
		currencyConversion.setTotalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiples()));

		logger.debug("Response returned : {}", currencyConversion);

		return currencyConversion;
	}
}
