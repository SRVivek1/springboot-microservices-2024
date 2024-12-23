package com.srvivek.sboot.mservices.ccs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class B4CurrencyConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(B4CurrencyConversionServiceApplication.class, args);
	}

}
