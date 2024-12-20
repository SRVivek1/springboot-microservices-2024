# springboot-microservices-2024

## Return response with link to newly created resource

### Project ref: a2-sboot-ms-social-media-app
- **<ins>Maven / External dependency</ins>**
	- Below required resources are available in Spring web dependency.
 	- ```
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
    	```	 

- **<ins>Code changes</ins>**
	- Build URL to new Resource using current request.
		- *`import org.springframework.web.servlet.support.ServletUriComponentsBuilder`*
		- *`ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(savedUser.getId())
						.toUri();`*
	- Wrap new URL and response object in *`ResponseEntity`*.
	- Return the *`ResponseEntity`* object.
		- *`return ResponseEntity.created(location).body(savedUser);`*

		- ```
				@PostMapping("/users")
				public ResponseEntity<User> createUser(@RequestBody User user) {
	
					logger.debug("User to save : {}", user);
			
					User savedUser = userDaoService.save(user);
		
					URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
	    					.buildAndExpand(savedUser.getId()).toUri();
	    
					return ResponseEntity.created(location).body(savedUser);
				}	```


# Formatted till here
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

## a8-sboot-ms-hateoas
- Maven dependency

```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateoas</artifactId>
		</dependency>
```

- Spring resources

```
		import org.springframework.hateoas.EntityModel;
		import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

```

- API Refactoring

```
		/**
		 * Retrieve the users.
		 * 
		 * @return
		 */
		@GetMapping(path = "/users/{id}", produces = {"application/json", "application/xml"})
		public EntityModel<User> retrieveUser(@PathVariable Integer id) {
			User user = userDaoService.findById(id);
	
			if (user == null) {
				throw new UserNotFoundException(String.format("No user exists with id : %s", id));
			}
	
			// Create link to method
			var link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
	
			// EntityModel object supports Model and allows to add links
			final EntityModel<User> entityModel = EntityModel.of(user);
			entityModel.add(link.withRel("all-users"));
			
			return entityModel;
		}

```


## a9-sboot-ms-static-filtering

### Static filtering

- Resource

```
		import com.fasterxml.jackson.annotation.JsonIgnore;
		import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
```

- Static filtering code

```
		/**
		 * Do not send field2, field4, field6
		 */
		@JsonIgnoreProperties(value = {"field2", "field4"})
		public class SomeBean {
		
			private String field1;
			private String field2;
			private String field3;
			private String field4;
			private String field5;
			
			//Ignore in json response
			@JsonIgnore
			private String field6;
			
			// constructor
			
			// getters
			
			// toString()
		}

```


### Dynamic filtering
- Resource

```
		import com.fasterxml.jackson.databind.ser.FilterProvider;
		import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
		import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

```


- Dynamic Filtering code

```
		@RestController
		public class DynamicFilteringController {
		
			@GetMapping("/dyna-filtering")
			public SomeBeanDynamicFilter filtering() {
				SomeBeanDynamicFilter SomeBeanDynamicFilter = new SomeBeanDynamicFilter("Value-1", "Value-2", "Value-3",
						"Value-4", "Value-5", "Value-6");
		
				// Dynamic filtering
				final SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field2",
						"field4", "field6");
		
				final SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider().addFilter("SomeBeanDynamicFilter",
						simpleBeanPropertyFilter);
		
				final MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SomeBeanDynamicFilter);
				mappingJacksonValue.setFilters(simpleFilterProvider);
		
				return SomeBeanDynamicFilter;
			}
		
			@GetMapping("/dyna-filtering-list")
			public MappingJacksonValue filteringList() {
				List<SomeBeanDynamicFilter> SomeBeanDynamicFilterList = Arrays.asList(
						new SomeBeanDynamicFilter("Value-1", "Value-2", "Value-3", "Value-4", "Value-5", "Value-6"),
						new SomeBeanDynamicFilter("Value-11", "Value-22", "Value-33", "Value-44", "Value-55", "Value-66"),
						new SomeBeanDynamicFilter("Value-111", "Value-222", "Value-333", "Value-444", "Value-555",
								"Value-666"));
		
				// Dynamic filtering
				SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field1",
						"field3", "field5", "field6");
				FilterProvider simpleFilterProvider = new SimpleFilterProvider().addFilter("SomeBeanDynamicFilter",
						simpleBeanPropertyFilter);
		
				final MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SomeBeanDynamicFilterList);
				mappingJacksonValue.setFilters(simpleFilterProvider);
		
				return mappingJacksonValue;
			}

```

- Dynamic filterig bean

-Resource

```
		import com.fasterxml.jackson.annotation.JsonFilter;
```

- Bean class

```
		/**
		 * Dynamically exclude properties as per the specified filter.
		 */
		@JsonFilter("SomeBeanDynamicFilter")
		public class SomeBeanDynamicFilter {
		
			private String field1;
			private String field2;
			private String field3;
			private String field4;
			private String field5;
			private String field6;

			// constructor
			
			// getters
			
			// toString()
		}
```


## a11-sboot-ms-hal-explorer
- Dependency

```
		<!-- Spring boot HAL explorer -->
		<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-rest-hal-explorer</artifactId>
		</dependency>
```

- Default URL

```
		- http://localhost:8080/explorer
		- http://localhost:8080/explorer/index.html#
```


##a13-sboot-ms-mysql-jpa
- Launch MySQL as Docker container

```
		docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=social-media-user --env MYSQL_PASSWORD=dummypassword --env MYSQL_DATABASE=social-media-database --name mysql --publish 3306:3306 mysql:8-oracle

```

- mysqlsh commands

```
		mysqlsh
		\connect social-media-user@localhost:3306
		\sql
		use social-media-database
		select * from user_details;
		select * from post;
		\quit
```



- /pom.xml Modified

```
		<!-- Use this for Spring Boot 3.1 and higher -->
		<dependency>
		    <groupId>com.mysql</groupId>
		    <artifactId>mysql-connector-j</artifactId>
		</dependency> 
		 
		<!-- Use this if you are using Spring Boot 3.0 or lower
		    <dependency>
		        <groupId>mysql</groupId>
		        <artifactId>mysql-connector-java</artifactId>
		    </dependency> 
		-->
```

- /src/main/resources/application.properties

```
		#spring.datasource.url=jdbc:h2:mem:testdb
		spring.jpa.show-sql=true
		spring.datasource.url=jdbc:mysql://localhost:3306/social-media-database
		spring.datasource.username=social-media-user
		spring.datasource.password=dummypassword
		spring.jpa.hibernate.ddl-auto=update
		spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```


##a14-sboot-sc-basic-authentication

- Dependency

```
		<!-- Spring Security -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

```

- Note: If facing any issue while starting the application, try following
		- Stop the server.
		- Update maven project (Alt + f5).
		- Start the server.


- Default user is 'user'.
- Get auto generated password from log.
	- Search in logs for "Using generated security password: " text to get the auto generated password.


- Configuring user and password in application properties

```
		spring.security.user.name=vivek
		spring.security.user.password=welcome
```


- Customizing default authentication
	- Create a Configuration class to override default authetication


```
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.security.config.Customizer;
		import org.springframework.security.config.annotation.web.builders.HttpSecurity;
		import org.springframework.security.web.SecurityFilterChain;
		
		@Configuration
		public class SpringSecurityConfiguration {
		
			@Bean
			SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
				/* 
				 * All requests must be authorized.
				 * 
				 * Else return HTTP 403, it doesn't prompt for user creds.
				 */
				httpSecurity.authorizeHttpRequests(
						authorizationManagerRequestMatcherRegistryCustomizer -> authorizationManagerRequestMatcherRegistryCustomizer
								.anyRequest().authenticated());
		
				/* 
				 * Prompt for authentication if request is not authorized.
				 * 
				 * Using default customizer
				 */
				httpSecurity.httpBasic(Customizer.withDefaults());
		
				/*
				 * Disabling CSRF as it may cause issue with HTTP methods - POST & PUT.
				 * 
				 * if enabled, Keep prompting for user credentials for post request.
				 */
				httpSecurity.csrf(csrf -> csrf.disable());
		
				return httpSecurity.build();
			}
		}

```


- X
