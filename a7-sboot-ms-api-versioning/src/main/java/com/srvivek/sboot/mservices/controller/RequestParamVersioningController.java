package com.srvivek.sboot.mservices.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.Name;
import com.srvivek.sboot.mservices.bean.PersonV1;
import com.srvivek.sboot.mservices.bean.PersonV2;

@RestController
public class RequestParamVersioningController {

	/**
	 * Version 1
	 * 
	 * @return
	 */
	@GetMapping(path = "/person/param", params = "version=1")
	public PersonV1 getPersonV1() {

		return new PersonV1("Request Param versioning v1");
	}

	/**
	 * Version 2
	 * 
	 * @return
	 */
	@GetMapping(path = "/person/param", params = "version=2")
	public PersonV2 getPersonV2() {

		return new PersonV2(new Name("Request Parama", "Versioning v2"));
	}
}
