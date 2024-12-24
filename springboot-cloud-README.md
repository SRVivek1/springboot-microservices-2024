# Spring Cloud Reference Doc[3.4.0]
	- This documentation is created for reference from the Spring cloud POC we have developed in this turotial

---
## 00. Port standardization

- Below are be the port specific to each service we'l be developing in this tutorial.

| **Application** | **Port** | **Info** |
| --------------- | -------- | -------- |
| Limits Microservice | *8080, 8081, etc.* | ~ |
| Spring Cloud Config Server | *8888* | Port 8888 is default port for config srver as per spring docs. |
| Currency Exchange Microservice | *8000, 8001, 8002, etc.* | ~ |
| Currency Conversion Microservice | *8100, 8101, 8102, etc.* | ~ |
| Netflix Eureka Naming Server | *8761* | ~ |
| API Gateway | *8765* | ~ |
| Zipkin Distributed Tracing Server | *9411* | ~ |

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

## 4. OpenFeign client: Connect to other mircoservice
### Project ref: *b5-currency-conversion-service-openfeign*
- **<ins>Purpose / Feature</ins>**
  - Easy way to make rest service calls. 
  - Removes bioler plate code need to be written while using `RestTemplate` to invoke a rest service.
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-openfeign</artifactId>
		</dependency>

- **<ins>Code changes</ins>**
  - Application: Enable feign clients using ***@EnableFeignClients***.
    - Scans for interfaces that declare they are feign clients `(via org.springframework.cloud.openfeign.FeignClient @FeignClient)`. 
    - Configures component scanning directives for use with `org.springframework.context.annotation.Configuration @Configuration` classes.
    -  imports
       - `import org.springframework.cloud.openfeign.EnableFeignClients;`
    - Annotate the SpringBoot application to enable feign clients.
	```java
		@EnableFeignClients  // Enables feign clients in application
		@SpringBootApplication
		public class B4CurrencyConversionServiceApplication {

			public static void main(String[] args) {
				SpringApplication.run(B4CurrencyConversionServiceApplication.class, args);
			}
		}
	```
  - Response java bean ***CurrencyConversion.java***.
    - This class must have all the properties expecting from `currency-exchange` service, to support auto population of response data. 
    -  imports
       - `import java.math.BigDecimal;`
    - Annotate the SpringBoot application to enable feign clients.
	```java
		public class CurrencyConversion {

			private Long id;
			private String from;
			private String to;
			private BigDecimal conversionMultiples;
			private BigDecimal quantity;
			private BigDecimal totalCalculatedAmount;
			private String environment;

			// constructors, setter-getters.
		}
	```

  - Create an feign client interface ***CurrencyExchangeProxy.java***
    -  imports
       - `import org.springframework.cloud.openfeign.FeignClient;`
    - Add validation in the properties of the bean.
	```java
		@FeignClient(name = "b3-currency-exchange-service", url = "localhost:8000")
		public interface CurrencyExchangeProxy {

			/**
			* Method as defined in the host service.
			* @param from
			* @param to
			* @return
			*/
			@GetMapping("/jpa/currency-exchange/from/{from}/to/{to}")
			public CurrencyConversion retrieveExchangeRateFromDatabase(@PathVariable String from, @PathVariable String to);
		}
	```
	- Controller/Service to use feign client interface.
    -  imports
       - `import org.springframework.cloud.openfeign.FeignClient;`
    - Add validation in the properties of the bean.
	```java
		@RestController
		public class CurrencyConversionController {

			private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

			@Autowired
			private CurrencyExchangeProxy currencyExchangeProxy;

			/**
			* Currency conversion using feign client.
			* @param from
			* @param to
			* @param quantity
			* @return
			*/
			@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
			public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
					@PathVariable BigDecimal quantity) {

				logger.info("Executing CurrencyConversionController.calculateCurrencyConversionFeign(..) API.");

				// Standardize
				from = from.toUpperCase();
				to = to.toUpperCase();

				// Send request to Currency exchange micro-service
				final CurrencyConversion currencyConversionExchange = currencyExchangeProxy
						.retrieveExchangeRateFromDatabase(from, to);

				logger.debug("Response from currency-exchange : {}", currencyConversionExchange);

				final CurrencyConversion currencyConversion = new CurrencyConversion(currencyConversionExchange.getId(), from,
						to, quantity, currencyConversionExchange.getConversionMultiples(),
						quantity.multiply(currencyConversionExchange.getConversionMultiples()),
						currencyConversionExchange.getEnvironment());

				logger.debug("Response returned : {}", currencyConversionExchange);

				return currencyConversion;
			}
		}
	```

> Note: The `CurrencyConversion.java` bean has all the properties which will be returned from `currency-exchange' service.

- **<ins>References:</ins>**
  - [https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html)
  - [https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)

---

## 5. Service Registry / Naming server : Eureka naming server
### Project ref: *b6-naming-service*
- **<ins>Purpose / Feature</ins>**
  - It's an application that contains information about all micro services including the name of the service, port, and IP address. 
  - Each microservice has to register itself with the Eureka Server.
  - Service is available at `/` context. [http://localhost:8761/](http://localhost:8761/) 
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - **Controller:** *AbcController.java*
    - imports
      - `import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;`
    - Annotate the method parameter for validation.
	```java
		@EnableEurekaServer
		@SpringBootApplication
		public class B6NamingServiceApplication {

			public static void main(String[] args) {
				SpringApplication.run(B6NamingServiceApplication.class, args);
			}	
		}
	```
  - **Applcation Config:** *application.properties*
    - Configure Eureka server mandatory properties.
	```properties
		spring.application.name=b6-naming-service

		server.port=8761

		# Eureka : start

		# Indicates whether or not this instance should register its information with eureka server for discovery by others.
		eureka.client.register-with-eureka=false

		# Indicates whether this client should fetch eureka registry information from eureka server.
		eureka.client.fetch-registry=false

		# when guessing a hostname, the IP address of the server should be used in preference to the hostname reported by the OS
		# eureka.instance.prefer-ip-address=true

		# The hostname if it can be determined at configuration time (otherwise it will be guessed from OS primitives).
		# eureka.instance.hostname=localhost

		# Eureka : end
	```
---

## 6. Eureka Naming server client configuration
### Project ref: *b3-currency-exchange-service* & *b5-currency-conversion-service-openfeign*
- **<ins>Purpose / Feature</ins>**
  - Register's the micro-service to Name server.
- **<ins>Steps</ins>**
  - ***Step-1:*** Add Eureka client in POM.xml
  - ***Step-2:*** Configure eureka server URI in application.properties
   
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-netflix-eureka-client</artifactId>
		</dependency>
- **<ins>Config changes</ins>**
  - **Application Config:** *application.properties*
	```properties
		# Start: Eureka client config

		# Map of availability zone to list of fully qualified URLs to communicate with eureka server. 
		# Each value can be a single URL or a comma separated list of alternative locations. 
		# Typically the eureka server URLs carry protocol,host,port,context and version information if any. 
		# Example: https://ec2-256-156-243-129.compute-1.amazonaws.com:7001/eureka/ 
		eureka.client.service-url.defaultZone=http://localhost:8761/eureka

		# End: Eureka client config
	```

> Note: If `Eureka client` dependency is present in `POM.xml`, spring will automatically try to register this service to Naming server by looking for `Eureka Server` on it's default `Eureka port - 8761`.

---

## 7. Client side Load Balancing microservices
### Project ref: *b3-currency-exchange-service* & *b5-currency-conversion-service-openfeign*
- **<ins>Purpose / Feature</ins>**
  - Balance the traffic to the services dynamically by checking the current running instances.
- **<ins>Steps</ins>**
  - ***Step-1:*** Add `spring-cloud-starter-loadbalancer` dependency in POM.xml.
  - ***Step-2:*** Add eureka properties in `application.propeties`.
  - ***Step-3:*** Update the feign client ***@FeignClient*** `annotation` and remove `url` property.
  - ***Step-4:*** Restart your service and verify in eureka server that your micro-service is regitered.
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-loadbalancer</artifactId>
		</dependency>
- **<ins>Code / Config changes</ins>**
  - **Feign Client:** *AbcController.java*
    - imports
      - `import org.springframework.cloud.openfeign.FeignClient;`
    - Annotate `@FeignClient` with only service name.
	```java
		//@FeignClient(name = "b3-currency-exchange-service", url = "localhost:8000")
		/* Find service details from name server using service name. */
		@FeignClient(name = "b3-currency-exchange-service") 
		public interface CurrencyExchangeProxy {

			/**
			* Method as defined in the host service.
			* @param from
			* @param to
			* @return
			*/
			@GetMapping("/jpa/currency-exchange/from/{from}/to/{to}")
			public CurrencyConversion retrieveExchangeRateFromDatabase(@PathVariable String from, @PathVariable String to);
		}
 
 - **Application Config:** *application.properties*
	```properties

		# load balancer
		logging.level.com.netflix.discovery=debug

		# Start: Eureka client config

		# Map of availability zone to list of fully qualified URLs to communicate with eureka server. 
		# Each value can be a single URL or a comma separated list of alternative locations. 
		# Typically the eureka server URLs carry protocol,host,port,context and version information if any. 
		# Example: https://ec2-256-156-243-129.compute-1.amazonaws.com:7001/eureka/ 
		eureka.client.service-url.defaultZone=http://localhost:8761/eureka

		# Enables the Eureka health check handler.
		eureka.client.healthcheck.enabled=true

		eureka.instance.lease-renewal-interval-in-seconds=60
		eureka.instance.lease-expiration-duration-in-seconds=60

		# End: Eureka client config


		# Start: Spring Load Balancer

		# Enable load balancer.
		spring.cloud.loadbalancer.enabled=true

		# Enables LoadBalancer retries.
		spring.cloud.loadbalancer.retry.enabled=true

		# End: Spring Load Balancer


		# Start: Cloud config client

		# by default it's enabled
		spring.cloud.config.enabled=true

		# Name of the service to be shared wit config server
		spring.cloud.config.name=currency-conversion-service

		# default active profile
		spring.cloud.config.profile=dev

		# 8888 - it's default port for config server
		spring.config.import=optional:configserver:http://localhost:8888

		# End: Cloud config client
	```
	
> Note: 
> With service name it finds the server details from `Eureka Server`.
> `spring-load-balancer` is mandatory dependency with feign client.

- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html](https://docs.spring.io/spring-cloud-commons/reference/spring-cloud-commons/loadbalancer.html)


---

## 8. API Gateway
### Project ref: *b7-api-gateway*
- **<ins>Purpose / Feature</ins>**
  - All requests will be routed via API Gateway.
  - This gives us flexibility to implement all common features at one place.
  - Build on top of Spring WebFlux (Reactive Approach).
  - Provide cross cutting concerns
    - Security
    - Monitoring / Metrics
  - Features:
    - Match routes to any request attribute to route to right service.
    - Allows to define Predicates (Matcher) & Filters (eg. Authentication, Authorization, Logging etc.).
    - Integrates with Spring Cloud Discovery client to load balancing to the service for which the request is received.
    - Path rewriting.
- **<ins>Steps</ins>**
  - ***Step-1:*** Ensure following dependencies are added.
    - Eureka Discovery Client
    - Reactive API Gateay
  - ***Step-2:*** Enable API gateway client discovery in `application.properties`
  - ***Step-3:*** Now, we can access services with the Service name registered in Eureka server and path to controller API.
    - http://localhost:8765/B5-CURRENCY-CONVERSION-SERVICE-OPENFEIGN/currency-conversion-feign/from/UsD/to/iNr/quantity/100
  - ***Step-4:*** Add property to accept service name in lower case
    - http://localhost:8765/b5-currency-conversion-service-openfeign/currency-conversion-feign/from/UsD/to/iNr/quantity/100
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
- **<ins>Code / Config changes</ins>**
  - **Application Config:** *application.properties*
	```properties
		spring.application.name=b7-api-gateway
		server.port=8765

		# Start: Eureka client config

		# Map of availability zone to list of fully qualified URLs to communicate with eureka server. 
		# Each value can be a single URL or a comma separated list of alternative locations. 
		# Typically the eureka server URLs carry protocol,host,port,context and version information if any. 
		# Example: https://ec2-256-156-243-129.compute-1.amazonaws.com:7001/eureka/ 
		eureka.client.service-url.defaultZone=http://localhost:8761/eureka

		# Enables the Eureka health check handler.
		eureka.client.healthcheck.enabled=true

		eureka.instance.lease-renewal-interval-in-seconds=60
		eureka.instance.lease-expiration-duration-in-seconds=60

		# End: Eureka client config

		# Start: Api Gateway

		# Flag that enables Discovery Client gateway integration.
		# This allows us to invoke service using service-name registered in Eureka
		# http://localhost:8765/B3-CURRENCY-EXCHANGE-SERVICE/currency-exchange/from/usd/to/inr
		spring.cloud.gateway.discovery.locator.enabled=true

		# Option to lower case serviceId in predicates and filters, defaults to false. 
		# Useful with eureka when it automatically uppercases serviceId. so MYSERIVCE, would match /myservice/**
		spring.cloud.gateway.discovery.locator.lower-case-service-id=true

		# End: Api Gateway
	```

> Note: This is an ***important*** note.

- **<ins>Notes:</ins>**
  - Service client discovery is by defaut `disabled`. It need to be enabed explicitly in `application.properties`. Check above config for details.

- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)

---

## 9. Routes with spring cloud gateway
### Project ref: *b8-api-gateway-routes*
- **<ins>Purpose / Feature</ins>**
  - API Gateway `Routers` and `Filters` provides the option to intercept and process the requests.
  - It allow to route specific URLs to desired service.
  - We can add HTTP headers & params in the request.
  - We can also rewrite the URL.
  - We can also implement common checks such as authentication, authorization, logging etc. 
- **<ins>Steps</ins>**
  - ***Step-1:*** Considering tht API Gateway is already implemented.
    - If not, refer `section 8` above for API Gateway coniguration.
  - ***Step-2:*** Update application configuration in `application.properties`.
    - Disable `gateway discovery locator` configuration in `application.properties` to auto discover clients from Eureka server.
  - ***Step-3:*** Create a Configuration class.
    - Create a Bean of `o.s.c.gateway.route.RouteLocator` class with method accepting `RouteLocatorBuilder` class as argument.
    - Now using `RouteLocatorBuilder` instance, we can customize routes, e.g. 
      - Route any URL to `lb://<service-name-in-eureka>` to redirect and do load-balancing.
      - Route to any external service [http://httpbin.org/](http://httpbin.org/).
        - Request failing for `HTTPS` requests, requires SSL confguration.
      - Rewrite the requested URL to corresponfing inernal service URL using filters.
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-gateway</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
		</dependency>
- **<ins>Code / Config changes</ins>**
  - **Configuratio:** *ApiGatewayConfiguration.java*
    - imports
      - `import org.springframework.cloud.gateway.route.RouteLocator;`
      - `import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;`
      - `import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder.Builder;`
    - Create a `Bean` of `RouteLocator` class.
	```java
		/**
		* Configuration class to build custom routes and request customization.
		*/
		@Configuration
		public class ApiGatewayConfiguration {

			/**
			* Define routes.
			* @param routeLocatorBuilder
			* @return
			*/
			@Bean
			RouteLocator getwayRoute(RouteLocatorBuilder routeLocatorBuilder) {
				/*
				* Redirect request to external service. Also, optionally we can add http
				* headers and request params in the request.
				*/
				Builder routeLocator = routeLocatorBuilder.routes()
						.route((p) -> p.path("/get")
								.filters(f -> f.addRequestHeader("MY-HEADER", "MY-CUSTOM-HEADER")
										.addRequestParameter("MY-REQUEST-PARAM", "MY-CUSTOM-REQUEST-PARAM"))
								.uri("http://httpbin.org:80/"));

				/**
				* Route URLs to load balancer and eureka service name
				*/
				routeLocator = routeLocator.route(p -> p.path("/currency-exchange/**").uri("lb://b3-currency-exchange-service"))
						.route(p -> p.path("/currency-conversion-feign/**")
								.uri("lb://b5-currency-conversion-service-openfeign"));

				/**
				* Rewrite URL and copy the path
				*/
				routeLocator.route(p -> p.path("/ccfs/**")
						.filters(f -> f.rewritePath("/ccfs/(?<segment>.*)", "/currency-conversion-feign/${segment}"))
						.uri("lb://b5-currency-conversion-service-openfeign"));

				return routeLocator.build();
			}
		}
	```
  - **Application Config:** *application.properties*
	```properties
		spring.application.name=b8-api-gateway-routes

		server.port=8765

		# Start: Eureka client config

		# Map of availability zone to list of fully qualified URLs to communicate with eureka server. 
		# Each value can be a single URL or a comma separated list of alternative locations. 
		# Typically the eureka server URLs carry protocol,host,port,context and version information if any. 
		# Example: https://ec2-256-156-243-129.compute-1.amazonaws.com:7001/eureka/ 
		eureka.client.service-url.defaultZone=http://localhost:8761/eureka

		# Enables the Eureka health check handler.
		eureka.client.healthcheck.enabled=true

		eureka.instance.lease-renewal-interval-in-seconds=60
		eureka.instance.lease-expiration-duration-in-seconds=60

		# End: Eureka client config


		# Start: Api Gateway

		# Flag that enables Discovery Client gateway integration.
		# This allows us to invoke service using service-name registered in Eureka
		# http://localhost:8765/B3-CURRENCY-EXCHANGE-SERVICE/currency-exchange/from/usd/to/inr
		# Disbling - to use Routes and Filters
		#spring.cloud.gateway.discovery.locator.enabled=true
		spring.cloud.gateway.discovery.locator.enabled=false

		# Option to lower case serviceId in predicates and filters, defaults to false. 
		# Useful with eureka when it automatically uppercases serviceId. so MYSERIVCE, would match /myservice/**
		# Disbling - to use Routes and Filters
		#spring.cloud.gateway.discovery.locator.lower-case-service-id=true
		spring.cloud.gateway.discovery.locator.lower-case-service-id=false

		# Option to lower case serviceId in predicates and filters, defaults to false. 
		# Useful with eureka when it automatically uppercases serviceId. so MYSERIVCE, would match /myservice/**
		# spring.cloud.gateway.discovery.locator.lowerCaseServiceId=true

		# End: Api Gateway	

	```

> Note: When using routes in API Gateway, eureka discover client must be disabled in `application.properties`.

- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-mvc/filters/rewritepath.html](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-mvc/filters/rewritepath.html)

---




