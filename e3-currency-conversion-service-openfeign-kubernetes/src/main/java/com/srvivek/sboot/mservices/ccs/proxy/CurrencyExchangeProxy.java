package com.srvivek.sboot.mservices.ccs.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.srvivek.sboot.mservices.ccs.bean.CurrencyConversion;

// Connect to locahost:8000 to find the service
//@FeignClient(name = "E2-CURRENCY-EXCHANGE-SERVICE-KUBERNETES", url = "localhost:8000")

// Find server info from Eureka with help of service name.
//@FeignClient(name = "E2-CURRENCY-EXCHANGE-SERVICE-KUBERNETES")

// if 'CURRENCY_EXCHANGE_SERVICE_HOST' found in system property use the value to connect to the service or else
// connect to the 'localhost'.
@FeignClient(name = "E2-CURRENCY-EXCHANGE-SERVICE-KUBERNETES", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")
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
