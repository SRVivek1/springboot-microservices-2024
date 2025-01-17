package com.srvivek.sboot.mservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.srvivek.sboot.mservices.bean.User;
import com.srvivek.sboot.mservices.dao.UserDaoService;
import com.srvivek.sboot.mservices.error.UserNotFoundException;

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
		User user = userDaoService.findById(id);

		if (user == null) {
			throw new UserNotFoundException(String.format("No user exists with id : %s", id));
		}

		return user;
	}

	/**
	 * Create user.
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		logger.debug("User to save : {}", user);

		var savedUser = userDaoService.save(user);

		var location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).body(savedUser);
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable Integer id) {

		logger.debug("Deltete the user with id : {}", id);

		userDaoService.deleteById(id);
	}

}
