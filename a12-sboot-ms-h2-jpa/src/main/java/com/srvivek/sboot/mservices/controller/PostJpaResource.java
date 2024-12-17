package com.srvivek.sboot.mservices.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.srvivek.sboot.mservices.bean.Post;
import com.srvivek.sboot.mservices.bean.User;
import com.srvivek.sboot.mservices.dao.PostRepository;
import com.srvivek.sboot.mservices.dao.UserRepository;
import com.srvivek.sboot.mservices.error.PostNotFoundException;
import com.srvivek.sboot.mservices.error.UserNotFoundException;

import jakarta.validation.Valid;

@RestController
public class PostJpaResource {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private PostRepository postRepository;

	private UserRepository userRepository;

	// const-injection of dependency
	public PostJpaResource(PostRepository postRepository, UserRepository userRepository) {
		super();
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Retrieve posts associated with provided user id.
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/jpa/users/{id}/posts")
	public CollectionModel<Post> retrieveAllPostsOfUser(@PathVariable(name = "id") Integer userId) {

		logger.info("PostJpaResource - retrieveAllPostsOfUser() API started.");

		final Optional<User> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("No user found with id %s", userId));
		}

		final List<Post> postList = user.get().getPosts();

		logger.debug("Posts for user {} : {}", userId, postList);

		final WebMvcLinkBuilder link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UserJpaResource.class).retrieveUser(userId));

		final CollectionModel<Post> posts = CollectionModel.of(postList);
		posts.add(link.withRel(String.format("user-%s", userId)));

		return posts;
	}

	/**
	 * Create a new post.
	 * 
	 * @param userId
	 * @param post
	 * @return
	 */
	@PostMapping(value = "/jpa/users/{id}/posts")
	public @Valid ResponseEntity<Object> createPostForUser(@PathVariable(name = "id") Integer userId,
			@Valid @RequestBody Post post) {

		logger.info("PostJpaResource - createPostForUser() API started.");

		final Optional<User> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("No user found with id %s", userId));
		}

		post.setUser(user.get());

		@Valid
		final Post savedPost = postRepository.save(post);

		// Construct link to newly created post
		final URI resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedPost.getId()).toUri();

		return ResponseEntity.created(resourceLocation).build();
	}

	/**
	 * Retrieve post for the given user id and post id.
	 * 
	 * @param id
	 * @param postId
	 * @return
	 */
	@GetMapping(value = "/jpa/users/{id}/posts/{pid}")
	public EntityModel<Post> retrievePostForPostId(@PathVariable Integer id,
			@PathVariable(name = "pid") Integer postId) {

		logger.info("PostJpaResource - retrievePostForPostId() API started.");

		// user has to exists
		Optional<User> user = userRepository.findById(id);

		if (user.isEmpty()) {
			throw new UserNotFoundException(String.format("No user exists with id %s", id));
		}

		logger.debug("User found with id {} : {}", id, user.get());

		Optional<Post> post = postRepository.findByIdAndUser(postId, user.get());

		if (post.isEmpty()) {
			throw new PostNotFoundException(String.format("No post found with id: %s, pid: %s", id, postId));
		}

		logger.debug("Post for id {} : {}", postId, post);

		final EntityModel<Post> resp = EntityModel.of(post.get());

		return resp;
	}

}
