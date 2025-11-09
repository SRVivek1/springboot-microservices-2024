package com.srvivek.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.app.service.UserService;

@RestController
public class HelloWorldController {

	@Autowired
	UserService userService;
	
	@GetMapping(path = {"/greet/{user}"})
	public String greet(@PathVariable(name = "user") String name) {
		return "Hello Mr. " + userService.name(name);
	}
}
