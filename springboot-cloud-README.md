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

