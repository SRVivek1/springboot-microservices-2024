package com.srvivek.sboot.mservices.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.Name;
import com.srvivek.sboot.mservices.bean.PersonV1;
import com.srvivek.sboot.mservices.bean.PersonV2;

@RestController
public class CustomHeaderVersioning {

	/**
	 * Version 1
	 * 
	 * @return
	 */
	@GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
	public PersonV1 getPersonV1() {

		return new PersonV1("Custom Header Versioning v1");
	}

	/**
	 * Version 2
	 * 
	 * @return
	 */
	@GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
	public PersonV2 getPersonV2() {

		return new PersonV2(new Name("Custom Header", "Versioning v2"));
	}
}
