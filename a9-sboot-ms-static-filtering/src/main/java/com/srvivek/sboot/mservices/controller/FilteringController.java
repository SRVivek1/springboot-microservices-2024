package com.srvivek.sboot.mservices.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.SomeBean;

@RestController
public class FilteringController {

	@GetMapping("/filtering")
	public SomeBean filtering() {
		return new SomeBean("Value-1", "Value-2", "Value-3", "Value-4", "Value-5", "Value-6");
	}
}
