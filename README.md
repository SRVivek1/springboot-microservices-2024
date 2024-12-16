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



## a6-sboot-ms-content-i18n
- TODO

## a7-sboot-ms-api-versioning
- URI Versioning

```
		V1: http://localhost:8080/v1/person
		
		@GetMapping("/v1/person")
		
		V2: http://localhost:8080/v2/person
		
		@GetMapping("/v2/person")

```


- Request Param Versioning

```
		V1: http://localhost:8080/person?version=1
		
		@GetMapping(path = "/person", params = "version=1")
		
		V2: http://localhost:8080/person?version=2
		
		@GetMapping(path = "/person", params = "version=2")
```

- Header Versioning

```
		V1: http://localhost:8080/person/header
		
		HEADER - X-API-VERSION:1
		
		@GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
		
		V2: http://localhost:8080/person/header
		
		HEADER - X-API-VERSION:2
		
		@GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
```

- Content Negotiation Versioning

```
		V1: http://localhost:8080/person/accept
		
		HEADER - Accept:application/vnd.company.app-v1+json
		
		@GetMapping(path = "/person/accept", produces = "application/vnd.company.app-v1+json")
		
		V2: http://localhost:8080/person/accept
		
		HEADER - Accept:application/vnd.company.app-v1+json
		
		@GetMapping(path = "/person/accept", produces = "application/vnd.company.app-v2+json")
```

## 