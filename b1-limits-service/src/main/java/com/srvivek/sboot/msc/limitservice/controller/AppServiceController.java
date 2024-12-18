package com.srvivek.sboot.msc.limitservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.msc.limitservice.bean.AppService;
import com.srvivek.sboot.msc.limitservice.config.AppServiceConfiguration;

@RestController
public class AppServiceController {

	@Autowired
	AppServiceConfiguration appServiceConfiguration;

	/**
	 * Read property value from config.
	 * 
	 * @return
	 */
	@GetMapping("/app-config")
	public AppService retrieveAppConfig() {

		return new AppService(appServiceConfiguration.getMinimum(), appServiceConfiguration.getMaximum());
	}
}
