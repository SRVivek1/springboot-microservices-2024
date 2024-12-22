# Spring Cloud Reference Doc[3.4.0]
	- This documentation is created for reference from the Spring cloud POC we have developed in this turotial

---
## 00. Port standardization

- Below are be the port specific to each service we'l be developing in this tutorial.

| **Application** | **Port** |
| --------------- | -------- |
| Limits Microservice | *8080, 8081, etc.* |
| Spring Cloud Config Server | *8888* |
| Currency Exchange Microservice | *8000, 8001, 8002, etc.* |
| Currency Conversion Microservice | *8100, 8101, 8102, etc.* |
| Netflix Eureka Naming Server | *8761* |
| API Gateway | *8765* |
| Zipkin Distributed Tracing Server | *9411* |

---

## 01. Develop cloud config server
### Project ref: *b2-sboot-cloud-config-server*
- **<ins>Purpose / Feature</ins>**
  - Spring Cloud Config provides server-side and client-side support for externalized configuration in a distributed system. 
  - With the Config Server, you have a central place to manage external properties for applications across all environments. 
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - B2SbootCloudConfigServerApplication.java main app.
    - imports
      - `import org.springframework.cloud.config.server.EnableConfigServer;`
      - Config server is enabled by adding annotation on main app.
    	```java
    		/* Enable config server. */
			@EnableConfigServer 
			@SpringBootApplication
			public class B2SbootCloudConfigServerApplication {

				public static void main(String[] args) {
					SpringApplication.run(B2SbootCloudConfigServerApplication.class, args);
				}

			}
    	```
  - **application.properties**
    ```properties
		spring.application.name=b2-sboot-cloud-config-server

		# Server port for config server
		server.port=8888

		# Start: Git repo config

		# Github.com
		spring.cloud.config.server.git.uri=https://github.com/SRVivek1/spring-cloud-config-server-git-repo.git

		# local git
		#spring.cloud.config.server.git.uri=file:///home/srvivek/wrkspace/spring-cloud-config-server-git-repo

		# windows
		#spring.cloud.config.server.git.uri=file:///c:/wrkspace/spring-cloud-config-server-git-repo

		# End: Git repo config
	```
  - **Git repo**
    - In git repo, create propertie file using microervice name - `<microservice-name>[-<env>].properties` in the base directory.
		```properties
			./limits-service-prod.properties
			./limits-service-dev.properties
			./limits-service.properties
			./limits-service-qa.properties
			./currency-exchange-service.properties

		```
	- Content
		```properties
			limits-service.properties:limits-service.minimum=8
			limits-service.properties:limits-service.maximum=888
			limits-service-dev.properties:limits-service.minimum=1
			limits-service-dev.properties:limits-service.maximum=111
			limits-service-qa.properties:limits-service.minimum=3
			limits-service-qa.properties:limits-service.maximum=333
			limits-service-prod.properties:limits-service.minimum=6
			limits-service-prod.properties:limits-service.maximum=666
		```
- **<ins>Notes:</ins>**
  - `@EnableConfigServer` annotation:
    - Help enable and configure Config. Server artifacts.


- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-cloud-config/docs/current/reference/html/](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
  - [https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_server](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_server)

---
## 02. Develop/Enable cloud config client 
### Project ref: *b1-limits-service*
- **<ins>Purpose / Feature</ins>**
  - A Spring Boot application can take immediate advantage of the Spring Config Server (or other external property sources provided by the application developer). 
  - It also picks up some additional useful features related to Environment change events.
  - **Config client is enabled by default.  
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - Create a `configurationProperties` class.
    - imports
      - `import org.springframework.boot.context.properties.ConfigurationProperties;`
    - Annotate the class for injecting properties.
  	```java
  		@ConfigurationProperties(prefix = "limits-service")
		@Component
		public class LimitsServiceConfiguration {

			// it will match to property named - limits-service.minimum
			private int minimum;

			// it will match to property named - limits-service.maximum
			private int maximum;

			// constructors, getter-setters.
		}
  	```
  - Controller / Service class
    - imports
      - `None specific to config client.`
    - Add validation in the properties of the bean.
  	```java
  		@RestController
		public class LimitsServiceController {

			@Autowired
			LimitsServiceConfiguration limitsServiceConfiguration;

			/**
			* Read property value from application.properties.
			* @return
			*/
			@GetMapping("/limits")
			public Limits retrieveLimits() {
				return new Limits(limitsServiceConfiguration.getMinimum(), limitsServiceConfiguration.getMaximum());
			}
		}
  	```
  - Limits Java bean to represent the data in one unit
	```java
		public class Limits {

			private int minimum;
			private int maximum;

			public Limits(int minimum, int maximum) {
				super();
				this.minimum = minimum;
				this.maximum = maximum;
			}

			//getter-setters & other methods
		}
	```
  - application.properties
	```properties
		#spring.profiles.active=dev,qa

		# Start: Cloud config client

		# Mention profile for default use.
		# spring.cloud.config.profile=default
		spring.cloud.config.profile=dev
		#spring.cloud.config.profile=qa

		# Provide name for this services, used to match for available properties file.
		# for dev profile it will check the file name - limits-service-dev.properties in config. server.
		spring.cloud.config.name=limits-service

		# this part is mandatory if config client dependency is added in POM to start the app.
		#spring.config.import=optional:configserver:

		# 8888 - it's default port for config server
		spring.config.import=optional:configserver:http://localhost:8888/

		# To enable/disable remote configuration, by default is enabled 
		#spring.cloud.config.enabled=false 

		# End: Cloud config client


		# Start: local service configuration
		limits-service.minimum=20
		limits-service.maximum=21

		app-config.minimum=10
		app-config.maximum=11
		# End: local service configuration
	
	```
- **<ins>Notes:</ins>**
  - Spring Boot 2.4 introduced a new way to import configuration data via the `spring.config.import` property. 
  - This is now the default way to bind to Config Server.
  - To optionally connect to config server set the following in `application.properties`:
	```properties
		spring.config.import=optional:configserver:
	```
  - This will connect to the Config Server at the default location of [http://localhost:8888](http://localhost:8888).
  - Removing the optional: prefix will cause the Config Client to fail if it is unable to connect to Config Server. 
  - To change the location of Config Server either set `spring.cloud.config.uri` or add the url to the `spring.config.import` statement such as, `spring.config.import=optional:configserver:http://myhost:8888`. 
  - The location in the import property has precedence over the uri property.
  - Spring Boot Config Data resolves configuration in a two step process. 
    - First it loads all configuration using the default profile. This allows Spring Boot to gather all configuration which may activate any additional profiles. 
    - After it has gathered all activated profiles it will load any additional configuration for the active profiles. 
    - Due to this you may see multiple requests being made to the Spring Cloud Config Server to fetch configuration. 
    - This is normal and is a side effect of how Spring Boot loads configuration when using `spring.config.import`.


- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-cloud-config/docs/current/reference/html/](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/)
  - [https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_client](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_spring_cloud_config_client)

---

## 3. RestTemplate: Connect to other mircoservice
### Project ref: *b4-currency-conversion-service*
- **<ins>Purpose / Feature</ins>**
  - A synchronous client to perform HTTP requests, exposing a simple, template method API over underlying HTTP client libraries such as the JDK HttpURLConnection, Apache HttpComponents, and others. 
  - RestTemplate offers templates for common scenarios by HTTP method, in addition to the generalized exchange and execute methods that support less frequent cases.
  - RestTemplate is typically used as a shared component. 
    - However, its configuration does not support concurrent modification, and as such its configuration is typically prepared on startup. 
  - If necessary, you can create multiple, differently configured RestTemplate instances on startup.
    - Such instances may use the same underlying ClientHttpRequestFactory if they need to share HTTP client resources.
  - RestTemplate and RestClient share the same infrastructure (i.e. request factories, request interceptors and initializers, message converters, etc.), so any improvements made therein are shared as well. 
    - However, RestClient is the focus for new higher-level features.
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - imports
    - `import org.springframework.web.client.RestTemplate;`
  - Annotate the method parameter for validation.
	```java
				@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
				public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
						@PathVariable BigDecimal quantity) {

					logger.info("Executing CurrencyConversionController.calculateCurrencyConversion(..) API.");

					// Standardize
					from = from.toUpperCase();
					to = to.toUpperCase();

					final Map<String, String> uriVariables = new HashMap<>();
					uriVariables.put("from", from);
					uriVariables.put("to", to);
					
					// Send request to Currency exchange micro-service
					final ResponseEntity<CurrencyConversion> response = new RestTemplate().getForEntity(
							"http://localhost:8000/jpa/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
							uriVariables);
					final CurrencyConversion currencyConversionExchange = response.getBody();

					logger.debug("Response from currency-exchange : {}", currencyConversionExchange);

					final CurrencyConversion currencyConversion = new CurrencyConversion(currencyConversionExchange.getId(), from, to, quantity,
							currencyConversionExchange.getConversionMultiples(),
							quantity.multiply(currencyConversionExchange.getConversionMultiples()), currencyConversionExchange.getEnvironment());
					
					logger.debug("Response returned : {}", currencyConversionExchange);
					
					return currencyConversion;
				}
	```
  - imports
    - `import java.math.BigDecimal;`
  - Annotate the method parameter for validation.
	```java
		public class CurrencyConversion {

			private Long id;
			private String from;
			private String to;
			private BigDecimal conversionMultiples;
			private BigDecimal quantity;
			private BigDecimal totalCalculatedAmount;
			private String environment;

			// contructors, setter-getters
		}
	```
> Note: When using `RestTemplate` we have to write boiler plate code to call rest service. To overcome this, we can use another spring dependency `spring-cloud-openfeign`.


- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html)

---

## 4. Using openfeign for calling rest services [***in progress***]
### Project ref: *bx-xxx*
- **<ins>Purpose / Feature</ins>**
  - This is xyz feature.
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-xxxxx</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - imports
    - `import some.dependent.resource`
  - Annotate the method parameter for validation.
	```java
		@PostMapping("/users")
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

			// Impacted code goes here.
		}
	```

  - imports
    - `import some.dependent.resource`
  - Add validation in the properties of the bean.
	```java
		public class User {

			// Impacted code goes here.
		}
	```

> Note: This is an ***important*** note.

- **<ins>Notes:</ins>**
  - Some important key point / takeaway note.
  - Some takeaway:
    - Sub topic takeaway.

- **<ins>Pros & Cons</ins>**

| Pros | Cons |
| ---- | ---- |
| Pros 1 | Cons 1 |
| Pros 2 | Cons 2 |

- **<ins>References:</ins>**
  - [https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml](https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml)
  - [xyz service](http://website.com/some-resource-path)

---

