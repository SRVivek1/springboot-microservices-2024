package com.srvivek.sboot.msc.limitservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.msc.limitservice.bean.Limits;

@RestController
public class LimitsServiceController {

	@Autowired
	LimitsServiceConfiguration limitsServiceConfiguration;

	/**
	 * Read property value from application.properties.
	 * 
	 * @return
	 */
	@GetMapping("/limits")
	public Limits retrieveLimits() {

		return new Limits(limitsServiceConfiguration.getMinimum(), limitsServiceConfiguration.getMaximum());
	}
}
