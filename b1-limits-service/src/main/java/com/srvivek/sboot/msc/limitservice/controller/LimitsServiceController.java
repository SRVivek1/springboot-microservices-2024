package com.srvivek.sboot.msc.limitservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.msc.limitservice.bean.Limits;

@RestController
public class LimitsServiceController {

	@GetMapping("/limits")
	public Limits retrieveLimits() {
		return new Limits(10, 1000);
	}
}
