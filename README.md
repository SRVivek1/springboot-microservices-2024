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


## a3-sboot-ms-validation
- TODO


## a4-sboot-ms-springdoc-swagger-openapi
- open API swagger documentation required below dependency in classpath.

```
	<dependency>
		<groupId>org.springdoc</groupId>
		<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
		<version>2.7.0</version>
	</dependency>

```

- Swagger API URL

```
http://localhost:8080/swagger-ui/index.html#/user-resource/retrieveAllUsers
```

- Reference

```
- https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml
- https://springdoc.org/#getting-started
```


## a5-sboot-ms-content-negotiation
- TODO