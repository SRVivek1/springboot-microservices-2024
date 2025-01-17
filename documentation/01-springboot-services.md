# springboot Basics [v3.4.0]
## 1. Return response with link to newly created resource
### Project ref: [a2-sboot-ms-social-media-app](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a2-sboot-ms-social-media-app)
- **<ins>Maven / External dependency</ins>**
	- Below required resources are available in Spring web dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-bo	ot-starter-web</artifactId>
		</dependency> 
- **<ins>Code changes</ins>**
    - imports
      - *`import org.springframework.web.servlet.support.ServletUriComponentsBuilder`*
	- Build URL to new Resource using current request.
		- *`ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(savedUser.getId()).toUri();`*
	- Wrap new URL and response object in *`ResponseEntity`* and return the *`ResponseEntity`* object.
		- *`return ResponseEntity.created(location).body(savedUser);`*
	- Controller method
		```java
			@PostMapping("/users")
			public ResponseEntity<User> createUser(@RequestBody User user) {

				logger.debug("User to save : {}", user);
				User savedUser = userDaoService.save(user);
	
				URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
	    			.buildAndExpand(savedUser.getId()).toUri();
	    
				return ResponseEntity.created(location).body(savedUser);
			}
		```
---
## 2. Property, method param or Return type validation
### Project ref: [a3-sboot-ms-validation](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a3-sboot-ms-validation)
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - imports
    - `import jakarta.validation.Valid;`
  - Annotate the method parameter for validation.
	```java
		@PostMapping("/users")
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

			logger.debug("User to save : {}", user);

			var savedUser = userDaoService.save(user);

			var location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(savedUser.getId())
					.toUri();
			return ResponseEntity.created(location).body(savedUser);
		}
	```

  - imports
    - `import jakarta.validation.constraints.Past;`
	- `import jakarta.validation.constraints.Size;`
  - Add validation in the properties of the bean.
	```java
		public class User {

			private Integer id;

			@Size(min = 3, max = 20, message = "Name must be more than 2 characters.")
			private String name;

			@Past(message = "Birth date should be in past.")
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
			private LocalDate birthDate;

			// constructors, setter-getters and other methods.
		}
	```
  - **<ins>Notes:</ins>**
    - Spring internally usages `jakarta-validation` API.
    - Check `jakarta.validation.constraints.*` for more validation classes.
      - `@Valid` annotation:
        - Marks a property, method parameter or method return type for validation cascading.
        - Constraints defined on the object and its properties are validated when the property, method parameter or method return type is validated.
      - `@Size` annotation
        - Validates property value to match defined size constraints.
      - `@Past` annotation
        - Validates date value for must be a past date.

---
## 3. API documentation using openAPI, swagger-ui

### Project ref: [a4-sboot-ms-springdoc-swagger-openapi](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a4-sboot-ms-springdoc-swagger-openapi)
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.7.0</version>
		</dependency>
	```
- **<ins>Code changes</ins>**
  - `None`.
- **<ins>Swagger URL:</ins>**
  - http://localhost:8080/swagger-ui
  - http://localhost:8080/swagger-ui/index.html
- **<ins>Notes:</ins>**
  - `No code change required to enable swagger documentation`.
  - It's enabled by default if the ependency is present in `POM.xml`
- **<ins>References:</ins>**
  - `https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui`
  - `https://springdoc.org/#getting-started`
---

## 4. Content negotiation for Response parsing
### Project ref: [a5-sboot-ms-content-negotiation](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a5-sboot-ms-content-negotiation)
- **<ins>Maven / External dependency</ins>**
  - Add following dependency in POM.xml
 	```xml
    	<!-- XML conversion -->
		<dependency>
			<groupId>com.fasterxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-xml</artifactId>
		</dependency>
	```
- **<ins>Code changes</ins>**
  - `None`
- **<ins>Notes:</ins>**
  - If client requests for `Accept: application/xml` header.
  - Spring willl internally check `jackson-dataformat-xml` API dependency, if found bean will be transformed to xml.
---

## 5. Internationalization (i18n)
### Project ref: [a6-sboot-ms-content-i18n](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a6-sboot-ms-content-i18n)
- **<ins>Maven / External dependency</ins>**
  - Required API is available as part of spring-context dependency.
  - This is imported with spring web dependency internally.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
	```
- **<ins>Code changes</ins>**
  - imports
    - `import org.springframework.context.MessageSource;`
  - Autowire the MessageSource in required class.
	```java
		@RestController
		public class HelloWorldI18n {

			private MessageSource messageSource;

			// autowire (constructor injection) messageSource.
			public HelloWorldI18n(MessageSource messageSource) {
				super();
				this.messageSource = messageSource;
			}

			@GetMapping("/say-hello-i18n")
			public String sayHello() {

				var locale = LocaleContextHolder.getLocale();

				return this.messageSource.getMessage("good.morning", null, "Default - Good Morning", locale);
			}
		}
	```
  - Create property files for different locale 
    - File: `messages[-<locale>].properties`
    - Here's some examples:
      - Default: `messages.properties`
      - Spanish: `messages_es.properties`
      - German: `messages_ger.properties`
- **<ins>Notes:</ins>**
  - Default message files name prefix is `messages` & suffix is `.properties`.
  - Spring reads the value of `Accept-Language` Header from `HTTP Request`and replaces it with `<locale>` when locating `messsages[-<locale>].properties` file.

---

## 6. Microservice API Versioning
### Project ref: [a7-sboot-ms-api-versioning](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a7-sboot-ms-api-versioning)
- **<ins>Maven / External dependency</ins>**
  - No dependency required.
  - API versioning is HTTP architectural style.
	- `None`
- **<ins>Common Java Beans Code</ins>**
  - Personv1.java bean
	```java
		public class PersonV1 {

			private String name;

			public PersonV1(String name) {
				super();
				this.name = name;
			}
			// getter-setters
		}
	```
  - Personv2.java bean 
	```java
		public class PersonV2 {

			private Name name;

			public PersonV2(Name name) {
				super();
				this.name = name;
			}
			// getter-setters
		}
	```
  - Name.java bean
	```java
		public class Name {

			private String firstName;
			private String lastName;

			public Name(String firstName, String lastName) {
				super();
				this.firstName = firstName;
				this.lastName = lastName;
			}
			// getters-setters
		}
	```
- **<ins>URI Versioning</ins>**
  - In this versioning style, a version number is appended in URL to create new URL version.
  - **Twitter** also follows same versoning strrateegy.
    - **Ref:** https://developer.x.com/en/docs/x-ads-api/versioning
  - **<ins>Drawback</ins>**
    - Polluting URL
  - **<ins>Controller Code changes</ins>**
    - imports
      - `import org.springframework.web.bind.annotation.GetMapping;`
    - Here's two versions of API defined.
		```java
			@RestController
			public class UriVersioningPersonController {

				/**
				* Version 1
				* @return
				*/
				@GetMapping("/v1/person")
				public PersonV1 getPersonV1() {
					return new PersonV1("URI Versioning v1");
				}

				/**
				* Version 2
				* @return
				*/
				@GetMapping("/v2/person")
				public PersonV2 getPersonV2() {
					return new PersonV2(new Name("URI", "Versioning V2"));
				}
			}
		```
- **<ins>Request Param Versioning</ins>**
  - In this versioning style, a request param is sent with API version number.
  - Base URL remains unchanged. 
  - **Amazon** also follows same versoning strrateegy.
    - **Ref:** *https://*
  - **<ins>Drawback</ins>**
    - *Polluting URL*
  - **<ins>Controller Code changes</ins>**
    - imports
      - `import org.springframework.web.bind.annotation.GetMapping;`
    - Here's two versions of API defined.
		```java
			@RestController
			public class RequestParamVersioningController {

				/**
				* Version 1
				* @return
				*/
				@GetMapping(path = "/person/param", params = "version=1")
				public PersonV1 getPersonV1() {
					return new PersonV1("Request Param versioning v1");
				}

				/**
				* Version 2
				* @return
				*/
				@GetMapping(path = "/person/param", params = "version=2")
				public PersonV2 getPersonV2() {
					return new PersonV2(new Name("Request Parama", "Versioning v2"));
				}
			}
		```
- **<ins>Custom Header Versioning</ins>**
  - In this versioning style, a custom HTTP header is sent with API version number.
  - Base URL remains unchanged. 
  - **Microsoft** also follows same versoning strrateegy.
    - **Ref:** https://
  - **<ins>Drawback</ins>**
    - Misuse of HTTP Headers
  - **<ins>Controller Code changes</ins>**
    - imports
      - `import org.springframework.web.bind.annotation.GetMapping;`
    - Here's two versions of API defined.
		```java
			@RestController
			public class CustomHeaderVersioning {

				/**
				* Version 1
				* @return
				*/
				@GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
				public PersonV1 getPersonV1() {
					return new PersonV1("Custom Header Versioning v1");
				}

				/**
				* Version 2
				* @return
				*/
				@GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
				public PersonV2 getPersonV2() {
					return new PersonV2(new Name("Custom Header", "Versioning v2"));
				}
			}
		```
- **<ins>Content Negotiation (Media type) Versioning</ins>**
  - a.k.a `Content negotiation` or `Accept header` versioning.
  - In this versioning style, a custome media type is sent in `Accept` HTTP header.
  - Which API matches the header value request is forwarded to it.
    - e.g. `Accept: application/vnd.comp.app-v2+json`
  - Base URL remains unchanged. 
  - **Github** also follows same versoning strrateegy.
    - **Ref:** *https://*
  - **<ins>Drawback</ins>**
    - Misuse of HTTP Headers
  - **<ins>Controller Code changes</ins>**
    - imports
      - `import org.springframework.web.bind.annotation.GetMapping;`
    - Here's two versions of API defined.
		```java
			@RestController
			public class MediaTypeVersioning {

				/**
				* Version 1
				* @return
				*/
				@GetMapping(path = "/person/accept", produces = "application/vnd.comp.app-v1+json")
				public PersonV1 getPersonV1() {
					return new PersonV1("Mediatype Versioning v1.");
				}

				/**
				* Version 2
				* @return
				*/
				@GetMapping(path = "/person/accept", produces = "application/vnd.comp.app-v2+json")
				public PersonV2 getPersonV2() {
					return new PersonV2(new Name("Media type", "Versioning v2"));
				}
			}
	```
---

## 7. SpringBoot HATEOAS
### Project ref: [a8-sboot-ms-hateoas](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a8-sboot-ms-hateoas)
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateoas</artifactId>
		</dependency>
	```
- **<ins>Code changes</ins>**
  - imports
    - `import org.springframework.hateoas.EntityModel;`
    - `import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;`
  - Add validation in the properties of the bean.
	```java
		/**
		 * Retrieve the users.
		 * @return
		 */
		@GetMapping(path = "/users/{id}", produces = {"application/json", "application/xml"})
		public EntityModel<User> retrieveUser(@PathVariable Integer id) {
			User user = userDaoService.findById(id);
	
			if (user == null) {
				throw new UserNotFoundException(String.format("No user exists with id : %s", id));
			}
	
			// Hateoas: Create link to method
			var link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
	
			// EntityModel object supports Model and allows to add links
			final EntityModel<User> entityModel = EntityModel.of(user);
			
			//Hateoas: Add link to Model response object.
			entityModel.add(link.withRel("all-users"));
			
			return entityModel;
		}
	```
- **<ins>Notes:</ins>**
  - `Spring HATEOAS` provides some APIs to ease creating REST representations that follow the HATEOAS principle when working with Spring and especially Spring MVC. 
  - The `core problem it tries to address` is link creation and representation assembly.
- **<ins>References:</ins>**
  - `https://spring.io/projects/spring-hateoas`
---

## 8. Static content filtering
### Project ref: [a9-sboot-ms-static-filtering](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a9-sboot-ms-static-filtering)
- **<ins>Purpose / Feature</ins>**
  - Content filtering refers to removing the properties from Response bean when sending back to requestor.
  - This feature is available in `jackson-annotations-x.x.jar`, which is added as part of `spring-boot-starter-web` dependency.
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - We only need to update the Java Bean properties. No change required in controller.
  - imports
    - `import com.fasterxml.jackson.annotation.JsonIgnore;`
    - `import com.fasterxml.jackson.annotation.JsonIgnoreProperties;`
  - Annotate java properties to ignore while building the response.
	```java
		/** Ignore all specified properties from the class.  */
		@JsonIgnoreProperties(value = {"property4", "property6"})
		public class SomeBean {

			private String property1;
			
			// Ignore this property
			@JsonIgnore
			private String property2;
			private String property3;
			
			// @JsonIgnoreProperties - Ignore this
			private String property4;
			private String property5;
			
			// @JsonIgnoreProperties - Ignore this
			private String property6;

			// constructors, setter-getters and utility methods
		}
	```
- **<ins>Notes:</ins>**
  - In this approach we're hardcoding that which properties should not be returned in response when the given bean is returned in response, irrespective of which API is using this.
  - Hence, this is called `Static Filtering`.
  - `@JsonIgnore` annotation:
    - Marks the property, to be removed when building response from this bean.
  - `@JsonIgnoreProperties` annotation
    - Marks a list of properties inside this class, to be removed when building response from this bean.

- **<ins>References:</ins>**
  - `TBU`

---

## 9. Dynamic content filtering
### Project ref: [a9-sboot-ms-static-filtering](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a9-sboot-ms-static-filtering)
- **<ins>Purpose / Feature</ins>**
  - Content filtering refers to removing the properties from Response bean when sending back to requestor.
  - This feature is available in `jackson-annotations-x.x.jar`, which is added as part of `spring-boot-starter-web` dependency.
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - Bean
    - Update the Java Bean class with the `filter name` create inside controller.
    - All controller APIs should update the filter with same `filter name`.
    - imports
      - `import com.fasterxml.jackson.annotation.JsonFilter;`
    - Annotate java properties to ignore while building the response.
  	```java
  		/** Dynamically exclude properties as per the specified filter set by the API. */
  		@JsonFilter("dyna-filter-for-somebean")
		public class SomeBeanDynamicFilter {

			private String field1;
			private String field2;
			private String field3;
			private String field4;
			private String field5;
			private String field6;

  			// constructors, setter-getters and utility methods
  		}
  	```
  - Controller
    - Create instace of `` and mention the properties to be excluded and add it with a unique name in filter provider.
	- imports
      - `import com.fasterxml.jackson.databind.ser.FilterProvider;`
      - `import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;`
      - `import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;`
      - `import com.srvivek.sboot.mservices.bean.SomeBeanDynamicFilter;`
    - Create filter.
  	```java
  		@RestController
		public class DynamicFilteringController {

			@GetMapping("/dyna-filtering")
			public MappingJacksonValue filtering() {

				// Dynamic filtering
				final SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field2",
						"field4", "field6");

				final SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider()
						.addFilter("dyna-filter-for-somebean", simpleBeanPropertyFilter);

				// Construct resposnse bean
				final SomeBeanDynamicFilter SomeBeanDynamicFilter = new SomeBeanDynamicFilter("Value-1", "Value-2", "Value-3",
						"Value-4", "Value-5", "Value-6");
				final MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SomeBeanDynamicFilter);
				mappingJacksonValue.setFilters(simpleFilterProvider);

				return mappingJacksonValue;
			}

			@GetMapping("/dyna-filtering-list")
			public MappingJacksonValue filteringList() {

				// Dynamic filtering
				SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field1",
						"field3", "field5", "field6");
				FilterProvider simpleFilterProvider = new SimpleFilterProvider().addFilter("dyna-filter-for-somebean",
						simpleBeanPropertyFilter);

				// Construct list of resposnse beans
				List<SomeBeanDynamicFilter> SomeBeanDynamicFilterList = Arrays.asList(
						new SomeBeanDynamicFilter("Value-1", "Value-2", "Value-3", "Value-4", "Value-5", "Value-6"),
						new SomeBeanDynamicFilter("Value-11", "Value-22", "Value-33", "Value-44", "Value-55", "Value-66"),
						new SomeBeanDynamicFilter("Value-111", "Value-222", "Value-333", "Value-444", "Value-555",
								"Value-666"));

				final MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(SomeBeanDynamicFilterList);
				mappingJacksonValue.setFilters(simpleFilterProvider);

				return mappingJacksonValue;
			}
		}
  	```
- **<ins>Notes:</ins>**
  - In this approach we're dynamically marking the properties which should not be returned in response when the given bean is returned in response, for the API which is returring this bean.
  - Hence, this is called `Dynamic Filtering`.
  - `@JsonFilter` annotation:
    - Defines a filter name, which should be evaluated and applied every time when object of this bean is returned in response.
    - The given filter name `dyna-filter-for-somebean` is created and update in API method of controller class.

- **<ins>References:</ins>**
  - `TBU`
---

## 10. Exception / Error Handling (Custom Exceptions)
### Project ref: [a2-sboot-ms-social-media-app](https://github.com/SRVivek1/springboot-microservices-2024/tree/main/a2-sboot-ms-social-media-app)
- **<ins>Purpose / Feature</ins>**
  - Spring boot provides mechanism to handle and customize the exceptions thrown by the applications.
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - Java Bean
    - Java Bean to standarized the error message returned to requestor/cient.
    - ErrorDetails.java
      - imports
        - `None`
      - Class with required properties
    	```java
    		public class ErrorDetails {

				private LocalDateTime timestamp;
				private String message;
				private String details;

				// Constructor, setter-getters
			}
    	```
  - Custom Exception class
    - Create a custom exception class to determine the type of error that has occured.
	- UserNotFoundException.java
      - imports
        - `import org.springframework.http.HttpStatus;`
		- `import org.springframework.web.bind.annotation.ResponseStatus;`
      - Override required constructors and can have fields if any info need to be stored.
    	```java
    		@ResponseStatus(code = HttpStatus.NOT_FOUND)
			public class UserNotFoundException extends RuntimeException {

				private static final long serialVersionUID = 4882099180124262207L;

				public UserNotFoundException(String message) {
					super(message);
				}
			}
    	```
  - Controller Advice class
    - Create a custom Controller advice class to determine the type of error that has occured.
	- UserNotFoundException.java
      - imports
        - `import org.springframework.core.annotation.Order;`
		- `import org.springframework.http.HttpStatus;`
        - `import org.springframework.http.ResponseEntity;`
        - `import org.springframework.web.bind.annotation.ControllerAdvice;`
        - `import org.springframework.web.bind.annotation.ExceptionHandler;`
        - `import org.springframework.web.bind.annotation.RestControllerAdvice;`
        - `import org.springframework.web.context.request.WebRequest;`
        - `import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;`
        - `import jakarta.annotation.Priority;`
      - Override required required method and process the exception.
    	```java
    		@ControllerAdvice
			//@ControllerAdvice(basePackages = "com.srvivek.x.y.z") //for specific package
			//@Order(value = 1) // for defining ordering
			//@Priority(value = 1) // for defining ordering
			//@RestControllerAdvice(basePackages = "com.srvivek.x.y.z") // @ControllerAdvice + @ResponseBody
			public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

				private static final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

				/**
				* Generic exception handling.
				* @param ex
				* @param request
				* @return
				* @throws Exception
				*/
				@ExceptionHandler(exception = Exception.class)
				public final ResponseEntity<ErrorDetails> handleAllException(Exception ex, WebRequest request) throws Exception {

					logger.error("Error stacktrace: {}", ex);

					ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
							request.getDescription(false));

					return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
				}

				/**
				* Return HTTP 404 for UserNotFoundException.
				* @param ex
				* @param request
				* @return
				* @throws Exception
				*/
				@ExceptionHandler(exception = UserNotFoundException.class)
				public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request)
						throws Exception {

					logger.error("Error stacktrace: {}", ex);

					ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(),
							request.getDescription(false));

					return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
				}
			}
    	```	
- **<ins>Notes:</ins>**
  - We can define controller advice for specific package.
  - While handling exceptions, also log the error explicitly or else it will not be logged in log files.

- **<ins>References:</ins>**
  - `TBU`

---


## a11-sboot-ms-hal-explorer [***TODO***]
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

---

##a13-sboot-ms-h2-jpa [***TODO***]

##a13-sboot-ms-mysql-jpa [***TODO***]
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

##a13-sboot-ms-mysql-jpa-JDBC-template [***TODO***]

##a14-sboot-sc-basic-authentication [***TODO***]

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


