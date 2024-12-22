## Port standardization

- Port standards for POC application

```
		1. Limits Microservice
		Ports: 8080, 8081, etc.
		
		2. Spring Cloud Config Server
		Port: 8888
		
		3. Currency Exchange Microservice
		Ports: 8000, 8001, 8002, etc.
		
		4. Currency Conversion Microservice
		Ports: 8100, 8101, 8102, etc.
		
		5. Netflix Eureka Naming Server
		Port: 8761
		
		6. API Gateway
		Port: 8765
		
		7. Zipkin Distributed Tracing Server
		Port: 9411
```



## b2-sboot-cloud-config-server

- Enable config server on Springboot runner app

```
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.cloud.config.server.EnableConfigServer;
		
		/**
		 * Enable config server
		 */
		@EnableConfigServer
		@SpringBootApplication
		public class B2SbootCloudConfigServerApplication {
		
			public static void main(String[] args) {
				SpringApplication.run(B2SbootCloudConfigServerApplication.class, args);
			}
		
		}

```

- Create a git repo 
- Create property files and commit in git
- Note: Use following format for properties file <microservice-name>[-<env>].properties

```
- Files:

		limits-service-dev.properties
		limits-service-prod.properties
		limits-service.properties
		limits-service-qa.properties

- Content:

		limits-service.properties:limits-service.minimum=8
		limits-service.properties:limits-service.maximum=888
		limits-service-dev.properties:limits-service.minimum=1
		limits-service-dev.properties:limits-service.maximum=111
		limits-service-qa.properties:limits-service.minimum=3
		limits-service-qa.properties:limits-service.maximum=333
		limits-service-prod.properties:limits-service.minimum=6
		limits-service-prod.properties:limits-service.maximum=666

```


- Update application.properties

```
		# Config Server port, to avoid conflict with other services.
		server.port=8888
		
		# Start: Git repo config
		
		# Github.com
		# spring.cloud.config.server.git.uri=https://github.com/SRVivek1/spring-cloud-config-server-git-repo.git
		
		# local git
		spring.cloud.config.server.git.uri=file:///home/srvivek/wrkspace/spring-cloud-config-server-git-repo
		
		# windows
		#spring.cloud.config.server.git.uri=file:///c:/wrkspace/spring-cloud-config-server-git-repo
		
		# End: Git repo config
```


## b1-limits-service [Enable config client]

- Add cloud client dependency in pom.xml

```
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>

```


- Enable config. client and connect to config server
- Add/update properties in application.properties

```
		# Config client will use below app. name to determine the properties from config server
			spring.application.name=limits-service


		# Start: Cloud config client
		
			# mention profile
			# for default config.
			# spring.cloud.config.profile=default
			spring.cloud.config.profile=dev
		
			# this part is mandatory if config client dependency is added in POM to start the app.
			#spring.config.import=optional:configserver:
			
			spring.config.import=optional:configserver:http://localhost:8888/
			
			# To disable this check, set 
			#spring.cloud.config.enabled=false 
			#spring.cloud.config.import-check.enabled=false.
		
		# End: Cloud config client

		# Start: Limits service local configuration
		
			limits-service.minimum=20
			limits-service.maximum=21
			
			# ... other properties if any
		# End: Limits service local configuration

```


- Create configuration properties class 

```
		import org.springframework.boot.context.properties.ConfigurationProperties;
		import org.springframework.stereotype.Component;
		
	   /**
		* Define prefix for properties to search in config properties.
		*
		*/
		@ConfigurationProperties(prefix = "limits-service")
		@Component
		public class LimitsServiceConfiguration {
			
			// it will match to property named - limits-service.minimum
			private int minimum;

			// it will match to property named - limits-service.maximum
			private int maximum;
		
			// Default constructor.
		
			// Setter & getters
		
			// toString()
		
		}

```


## 


