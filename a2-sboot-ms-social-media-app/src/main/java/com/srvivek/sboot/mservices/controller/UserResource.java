package com.srvivek.sboot.mservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.srvivek.sboot.mservices.bean.User;
import com.srvivek.sboot.mservices.dao.UserDaoService;

@RestController
public class UserResource {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private UserDaoService userDaoService;

	// Constructor autowire injection
	public UserResource(UserDaoService userDaoService) {
		this.userDaoService = userDaoService;
	}

	/**
	 * Retrieve all users.
	 * 
	 * @return
	 */
	@GetMapping(path = "/users")
	public List<User> retrieveAllUsers() {
		return userDaoService.findAll();
	}

	/**
	 * Retrieve all users.
	 * 
	 * @return
	 */
	@GetMapping(path = "/users/{id}")
	public User retrieveUser(@PathVariable Integer id) {
		return userDaoService.findById(id);
	}

	@PostMapping("/users")
	public User createUser(@RequestBody User user) {

		logger.debug("User to save : {}", user);
		return userDaoService.save(user);
	}

}
