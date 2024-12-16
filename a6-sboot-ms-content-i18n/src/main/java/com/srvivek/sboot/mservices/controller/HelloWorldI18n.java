package com.srvivek.sboot.mservices.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldI18n {

	private MessageSource messageSource;

	// autowire messageSource.
	public HelloWorldI18n(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}

	@GetMapping("/say-hello-i18n")
	public String sayHello() {

		var locale = LocaleContextHolder.getLocale();

		return this.messageSource.getMessage("good.morning", null, "Default - Good Morning", locale);
	}
}
