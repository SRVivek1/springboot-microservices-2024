package com.srvivek.sboot.mservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.HelloWorldBean;

@RestController
public class HelloWorldController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Greeting API.
	 * 
	 * @return
	 */
	@GetMapping(path = "/hello-world")
	public String sayHello() {

		logger.info("HelloWorldController.sayHello() API exection started.");

		return "Hello World !!!";
	}

	/**
	 * Greeting API as java bean in response.
	 * 
	 * @return
	 */
	@GetMapping(path = "/hello-world-bean")
	public HelloWorldBean sayHelloBean() {

		logger.info("HelloWorldController.sayHelloBean() API exection started.");

		return new HelloWorldBean("Hello World !!!");
	}

	/**
	 * Greeting API accepting user name and returns message as java bean in
	 * response.
	 * 
	 * @return
	 */
	@GetMapping(path = "/hello-world-bean/pv/{name}")
	public HelloWorldBean sayHelloBeanPathVariable(@PathVariable String name) {

		logger.info("HelloWorldController.sayHelloBean() API exection started.");

		return new HelloWorldBean(String.format("Hello MR. %s !!!", name));
	}
}
