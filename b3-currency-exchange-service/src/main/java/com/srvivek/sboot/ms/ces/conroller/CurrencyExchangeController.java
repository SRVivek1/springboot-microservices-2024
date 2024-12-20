package com.srvivek.sboot.ms.ces.conroller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.ms.ces.bean.CurrencyExchange;

@RestController
public class CurrencyExchangeController {

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeRate(@PathVariable String from, @PathVariable String to) {
		return new CurrencyExchange(1001l, from, to, BigDecimal.valueOf(65));
	}
}
