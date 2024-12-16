# springboot-microservices-2024

## a2-sboot-ms-social-media-app
- Return URL and create status in response.

```
	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		logger.debug("User to save : {}", user);

		User savedUser = userDaoService.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).body(savedUser);
	}

```

- API in use.
	- Below resource is used to build the URI dynamically.

```
org.springframework.web.servlet.support.ServletUriComponentsBuilder
```
---

## a3-sboot-ms-social-