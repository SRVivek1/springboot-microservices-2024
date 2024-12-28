package com.srvivek.sboot.mservices.docker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@GetMapping("/")
	public String helloWorld() {
		return "{\"message\":\"Hello wolrd Sboot + docker v2.\"}";
	}
}
