package com.srvivek.sboot.mservices.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.Name;
import com.srvivek.sboot.mservices.bean.PersonV1;
import com.srvivek.sboot.mservices.bean.PersonV2;

@RestController
public class UriVersioningPersonController {

	/**
	 * Version 1
	 * 
	 * @return
	 */
	@GetMapping("/v1/person")
	public PersonV1 getPersonV1() {

		return new PersonV1("URI Versioning v1");
	}

	/**
	 * Version 2
	 * 
	 * @return
	 */
	@GetMapping("/v2/person")
	public PersonV2 getPersonV2() {

		return new PersonV2(new Name("URI", "Versioning V2"));
	}
}
