# Spring boot microservices with docker

## 0. Resources
- **Exchange**
  - http://localhost:8000/currency-exchange/from/usd/to/inr
  - http://localhost:8000/jpa/currency-exchange/from/USD/to/INR
- **Currency Conversion**
  - http://localhost:8100/currency-conversion/from/usd/to/inr/quantity/10
  - http://localhost:8100/currency-conversion-feign/from/UsD/to/iNr/quantity/100
- **Eureka**
  - http://localhost:8761/
- **Zipkin**
  - http://localhost:9411/
- **API Gateway**
  - http://localhost:8765/currency-exchange/from/usd/to/inr
  - http://localhost:8765/currency-conversion-feign/from/UsD/to/iNr/quantity/100
  - http://localhost:8765/ccfs/from/UsD/to/iNr/quantity/100


## 1. Spring cloud : Tracing service with zipkinm server (Docker)
### Project ref: *N/A*
- **<ins>Purpose / Feature</ins>**
  - Zipkin⁠ is a distributed tracing system. 
  - It helps gather timing data needed to troubleshoot latency problems in service architectures. Features include both the collection and lookup of this data.
  - If you have a trace ID in a log file, we can jump directly to it. Otherwise, we can query based on attributes such as service, operation name, tags and duration. Some interesting data will be summarized for you, such as the percentage of time spent in a service, and whether or not operations failed.
  - Each of the `microservice` send the tracing info to the zipkin tracing server. Zipkin store the information in database and provides UI.
  - Dependencies:
      - Micrometer:  
        - Bridges the Micrometer Observation API to OpenTelemetry.
        - Handles logs, traces & matrics.
      - OpenTelemetry: 
        - Reports traces to Zipkin.
        - Handles logs, traces & matrics.
      - OpenZipkin brave:
        - Intercepts production requests, gathers timing data, and propagates trace contexts. 
        - Primary objective is to facilitate the correlation of timing data within distributed systems, enabling efficient troubleshooting for latency issues.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Docker must be installed and running.
  - ***Step-1:*** Pull zipkin server image from docker hub and create container.
    - ***docker run -d -p 9411:9411 openzipkin/zipkin***
  - ***Step-2:*** Now we can access `Zipkin` locally on `http://localhost:9411/zipkin/`.

> Note: We can also develop spring boot app with zipkin serevr intead of using Zipking docker image.
- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-boot/reference/actuator/tracing.html](https://docs.spring.io/spring-boot/reference/actuator/tracing.html)
  - [https://www.baeldung.com/tracing-services-with-zipkin](https://www.baeldung.com/tracing-services-with-zipkin)
  - [https://spring.io/projects/spring-cloud-sleuth](https://spring.io/projects/spring-cloud-sleuth)
  - [https://docs.micrometer.io/tracing/reference/1.4](https://docs.micrometer.io/tracing/reference/1.4)
---

## 2. Spring cloud: Zipkin client
### Project ref: 
  - *d1-zipkin-tracing-server*
  - *d2-zipkin-currency-exchange-service*
  - *d3-zepkin-currency-conversion-service-openfeign*
  - *d4-zepkin-api-gateway-routes*

- **<ins>Purpose / Feature</ins>**
  - Centralized tracing using logs, matrices and/or graphs.
  - **Observation:** 
    - ***Span Id*** is different in all microservice call trace.
    - Another property ***Parent Id*** is present in all microservice call trace.
- **<ins>Steps</ins>**
  - ***Project Setup:*** An existing microservice / create one.
  - ***Step-1:*** pom.xml: Add micrometer, micrometer tracing bridge & opentelemetry dependencies.
    - **Micrometer:** Assigns an ID to the request, which remains same in all microservices requests called in that specific request chain. 
      - *e.g. Req. 1:* currency-conversion-service --> currency-exchange-service 
    - **feign-micrometer:** Add dependency to pom.xml, to trace requests made using `feign` clients. By default requests made using feign clients are not traced.
  - ***Step-2:*** Add sampling configuration to define how much percentage of request will be sampled.
    - `management.tracing.sampling.probability=1.0 # 1.0 -> 100%,  #SB3 `
    - `logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}] #SB3`
  - ***Step-3:*** Build RestTemplate instance using Builder class.
    - Build `RestTemplate` using `RestTemplateBuilder` to enable tracing of requests.
      - RestTemplate instance created using constructor `new RestTemplate()` will not be traced.
      - So, cretae a config class and return a Bean of `RestTemplate` and autowire in controller.
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
    ```xml
        <!-- SB3 :  Micrometer 
              > OpenTelemetry 
              > Zipkin 
        -->

        <!-- Micrometer - Vendor-neutral application observability facade. 
            Instrument your JVM-based application code without vendor lock-in.  
            Observation (Metrics & Logs) + Tracing.
        -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-observation</artifactId>
        </dependency>

        <!-- Open Telemetry
            - Open Telemetry as Bridge (RECOMMENDED)
            - Simplified Observability (metrics, logs, and traces) -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-tracing-bridge-otel</artifactId>
        </dependency>

        <dependency>
            <groupId>io.opentelemetry</groupId>
            <artifactId>opentelemetry-exporter-zipkin</artifactId>
        </dependency>

        <!-- requres where feign client is getting used. -->
        <!-- Enables tracing of REST API calls made using Feign - SB-V3 ONLY-->
        <dependency>
          <groupId>io.github.openfeign</groupId>
          <artifactId>feign-micrometer</artifactId>
        </dependency>
      ```
- **<ins>Code / Config changes</ins>**
  - **Congiration:** *RestTemplateConfiguration.java*
    - imports
      - `import org.springframework.boot.web.client.RestTemplateBuilder;`
    - Create bean of RestTemplate in configuration class.
      ```java
          @Configuration
          public class RestTemplateConfiguration {

            @Bean
            RestTemplate restTemplate(RestTemplateBuilder builder) {
              return builder.build();
            }
          }
      ```
  - **Controller:** *CurrencyConversionController.java*
    - imports
      - `import org.springframework.boot.web.client.RestTemplateBuilder;`
    - Use autowired RestTemplate instance to consume services.
      ```java
          // standard controller code

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
            final ResponseEntity<CurrencyConversion> response = restTemplate.getForEntity(
                "http://localhost:8000/jpa/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,
                uriVariables);

            final CurrencyConversion currencyConversionExchange = response.getBody();

            logger.debug("Response from currency-exchange : {}", currencyConversionExchange);

            final CurrencyConversion currencyConversion = new CurrencyConversion(currencyConversionExchange.getId(), from,
                to, quantity, currencyConversionExchange.getConversionMultiples(),
                quantity.multiply(currencyConversionExchange.getConversionMultiples()),
                currencyConversionExchange.getEnvironment());

            logger.debug("Response returned : {}", currencyConversionExchange);

            return currencyConversion;
          }
          // other APIs
  	  ```
  - **Application Config:** *application.properties*
	```properties
    # logging
    logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
    logging.level.com.srvivek.sboot=debug

    # Start: micrometer configuration

    # Add sampling configuration to define how much percentage of request will be sampled.
    management.tracing.sampling.probability=1.0  # 100%

    # End: micrometer configuration
	```
---

## 3. Docker componse: Maintain microservices in Docker
### Project ref: *d5-docker-componse*
- **<ins>Purpose / Feature</ins>**
  - Docker Compose is a tool for defining and running multi-container applications. It is the key to unlocking a streamlined and efficient development and deployment experience.
  - Compose simplifies the control of your entire application stack, making it easy to manage services, networks, and volumes in a single, comprehensible YAML configuration file. Then, with a single command, you create and start all the services from your configuration file.
  - Compose works in all environments; production, staging, development, testing, as well as CI workflows. 
  - It also has commands for managing the whole lifecycle of your application:
    - Start, stop, and rebuild services
    - View the status of running services
    - Stream the log output of running services
    - Run a one-off command on a service
  - The Compose file
    - The default path for a Compose file is ***compose.yaml*** (preferred) or ***compose.yml*** that is placed in the working directory. 
    - Compose also supports ***docker-compose.yaml*** and ***docker-compose.yml*** for backwards compatibility of earlier versions. 
      - If both files exist, Compose prefers the canonical ***compose.yaml***.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Check and install docker-componse.
    - **Linux:** *sudo curl -SL https://github.com/docker/compose/releases/download/v2.32.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose*
  - ***Step-1:*** Create `docker-compose.yaml`.
    - Don't use `tabs` for formatting/indentation, use only spaces.
    - Add all service configs (image, port, mem. limit etc.)
  - ***Step-2:*** Run command ***docker-compose up*** to launch all services configured.
    - This command will look for the ***docker-compose.yaml*** in working directory.
- **<ins>docker-ccompose.yaml</ins>**
 	```yaml
      version: '3.7'

      services:   
        naming-server:
          image: srvivek/b6-naming-service:0.0.1-SNAPSHOT
          mem_limit: 800m
          ports:
            - "8761:8761"
          networks:
            - currency-network
        
        currency-exchange:
          image: srvivek/d2-zipkin-currency-exchange-service:0.0.1-SNAPSHOT
          mem_limit: 800m
          ports:
            - "8000:8000"
          networks:
            - currency-network
          depends_on:
            - naming-server
          environment: # Properties - override defaults in appplication.properties
            EUREKA.CLIENT.SERVICE-URL.DEFAULTZONE: http://naming-server:8761/eureka
            MANAGEMENT.ZIPKIN.TRACING.ENDPOINT: http://zipkin-server:9411/api/v2/spans

        currency-conversion:
          image: srvivek/d3-zepkin-currency-conversion-service-openfeign:0.0.1-SNAPSHOT
          mem_limit: 800m
          ports:
            - "8100:8100"
          networks:
            - currency-network
          depends_on:
            - naming-server
          environment: # Properties - override defaults in appplication.properties
            EUREKA.CLIENT.SERVICE-URL.DEFAULTZONE: http://naming-server:8761/eureka
            MANAGEMENT.ZIPKIN.TRACING.ENDPOINT: http://zipkin-server:9411/api/v2/spans

        api-gateway:
          image: srvivek/d4-zepkin-api-gateway-routes:0.0.1-SNAPSHOT
          mem_limit: 800m
          ports:
            - "8765:8765"
          networks:
            - currency-network
          depends_on:
            - naming-server
          environment: # Properties - override defaults in appplication.properties
            EUREKA.CLIENT.SERVICE-URL.DEFAULTZONE: http://naming-server:8761/eureka
            MANAGEMENT.ZIPKIN.TRACING.ENDPOINT: http://zipkin-server:9411/api/v2/spans

        zipkin-server:
          image: openzipkin/zipkin
          mem_limit: 800m
          ports:
            - "9411:9411"
          networks:
            - currency-network
          restart: always #Restart if there is a problem starting up

      networks:
        currency-network:
  ```
- **<ins>References:</ins>**
  - [https://docs.docker.com/compose/gettingstarted/](https://docs.docker.com/compose/gettingstarted/)
---

# --------------------------- End ---------------------------