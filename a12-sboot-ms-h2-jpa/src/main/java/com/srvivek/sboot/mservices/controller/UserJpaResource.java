package com.srvivek.sboot.mservices.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.srvivek.sboot.mservices.bean.User;
import com.srvivek.sboot.mservices.dao.UserRepository;
import com.srvivek.sboot.mservices.error.UserNotFoundException;

import jakarta.persistence.Id;
import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private UserRepository userRepository;

	// Constructor autowire injection
	public UserJpaResource(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Retrieve all users.
	 * 
	 * @return
	 */
	@GetMapping(path = "/jpa/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Retrieve the users.
	 * 
	 * Note: HATEOAS / EntitlyModel for response, it filters out the {@link Id}
	 * filed from bean.
	 * 
	 * @return
	 */
	@GetMapping(path = "/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable Integer id) {
		logger.info("Find user associated with id : {}", id);
		Optional<User> user = userRepository.findById(id);

		logger.debug("User found in DB : {}", user.get());

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("No user exists with id : %s", id));
		}

		var link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());

		final EntityModel<User> entityModel = EntityModel.of(user.get());
		entityModel.add(link.withRel("all-users"));

		return entityModel;
	}

	/**
	 * Retrieve the users, without HATEOAS
	 * 
	 * Note: HATEOAS / EntitlyModel for response, it filters out the {@link Id}
	 * filed from bean.
	 * 
	 * @return
	 */
	@GetMapping(path = "/jpa/wh/users/{id}")
	public User retrieveUserNoHateoas(@PathVariable Integer id) {
		logger.info("Find user associated with id : {}", id);
		Optional<User> user = userRepository.findById(id);

		logger.debug("User found in DB : {}", user.get());

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("No user exists with id : %s", id));
		}

		return user.get();
	}

	/**
	 * Create user.
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/jpa/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

		logger.debug("User to save : {}", user);

		var savedUser = userRepository.save(user);

		var location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).body(savedUser);
	}

	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable Integer id) {

		logger.debug("Deltete the user with id : {}", id);

		userRepository.deleteById(id);
	}

}
