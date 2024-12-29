package com.srvivek.sboot.ms.ces.conroller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.ms.ces.bean.CurrencyExchange;
import com.srvivek.sboot.ms.ces.execption.CurrencyNotFoundException;
import com.srvivek.sboot.ms.ces.repository.CurrencyExchangeRepository;

@RestController
public class CurrencyExchangeController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Environment environment;

	@Autowired
	private CurrencyExchangeRepository currencyExchangeRepository;

	/**
	 * Return conversion rate for given currencies.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeRate(@PathVariable String from, @PathVariable String to) {

		logger.info("Executing CurrencyExchangeController - retrieveExchangeRate()");

		final String port = environment.getProperty("local.server.port");

		final CurrencyExchange currencyExchange = new CurrencyExchange(1001l, from, to, BigDecimal.valueOf(65));
		currencyExchange.setEnvironment(port);

		logger.debug("Response {}", currencyExchange);

		return currencyExchange;
	}

	/**
	 * Return conversion rate for given currencies.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@GetMapping("/jpa/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeRateFromDatabase(@PathVariable String from, @PathVariable String to) {

		
		/*
		 * 2024-12-29T08:12:16.506+05:30  INFO [d2-zipkin-currency-exchange-service,1b97943a10732863f2cf9e6358846b12,9f55ef3da9ce0d1a] 11842 --- 
		 * [d2-zipkin-currency-exchange-service] [nio-8000-exec-1] [1b97943a10732863f2cf9e6358846b12-9f55ef3da9ce0d1a] 
		 * c.s.s.m.c.c.CurrencyExchangeController   : Executing CurrencyExchangeController - retrieveExchangeRate() with USD to INR.
		 */
		logger.info("Executing CurrencyExchangeController - retrieveExchangeRate() with {} to {}.", from, to);

		// Currency names are case sensitive
		from = from.toUpperCase();
		to = to.toUpperCase();

		final String port = environment.getProperty("local.server.port");

		final CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);

		if (currencyExchange == null) {
			throw new CurrencyNotFoundException(
					String.format("Currency exchange not available - from %s to %s", from, to));
		}

		currencyExchange.setEnvironment(port);

		logger.debug("Response {}", currencyExchange);

		return currencyExchange;
	}
}