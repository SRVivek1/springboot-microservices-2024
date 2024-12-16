package com.srvivek.sboot.mservices.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.Name;
import com.srvivek.sboot.mservices.bean.PersonV1;
import com.srvivek.sboot.mservices.bean.PersonV2;

@RestController
public class MediaTypeVersioning {

	/**
	 * Version 1
	 * 
	 * @return
	 */
	@GetMapping(path = "/person/accept", produces = "application/vnd.comp.app-v1+json")
	public PersonV1 getPersonV1() {

		return new PersonV1("Mediatype Versioning v1.");
	}

	/**
	 * Version 2
	 * 
	 * @return
	 */
	@GetMapping(path = "/person/accept", produces = "application/vnd.comp.app-v2+json")
	public PersonV2 getPersonV2() {

		return new PersonV2(new Name("Media type", "Versioning v2"));
	}
}
