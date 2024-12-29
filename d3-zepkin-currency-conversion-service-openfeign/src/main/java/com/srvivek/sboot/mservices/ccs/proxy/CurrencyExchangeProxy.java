package com.srvivek.sboot.mservices.ccs.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.srvivek.sboot.mservices.ccs.bean.CurrencyConversion;

//@FeignClient(name = "d2-zipkin-currency-exchange-service", url = "localhost:8000")
@FeignClient(name = "d2-zipkin-currency-exchange-service")
public interface CurrencyExchangeProxy {

	/**
	 * Method as defined in the host service.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@GetMapping("/jpa/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retrieveExchangeRateFromDatabase(@PathVariable String from, @PathVariable String to);

}
