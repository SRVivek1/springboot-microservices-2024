## 1. Spring cloud : Tracing service with zipkinm server (Docker)
### Project ref: *N/A*
- **<ins>Purpose / Feature</ins>**
  - Zipkin⁠ is a distributed tracing system. 
  - It helps gather timing data needed to troubleshoot latency problems in service architectures.
  - Features include both the collection and lookup of this data.
  - If you have a trace ID in a log file, you can jump directly to it. Otherwise, you can query based on attributes such as service, operation name, tags and duration. Some interesting data will be summarized for you, such as the percentage of time spent in a service, and whether or not operations failed.
  - Each of the `microservice` send the tracing info to the zipkin tracing server. Zipkin store the information in database and provides UI.
  - Dependencies:
      - Micrometer:  
        - Bridges the Micrometer Observation API to OpenTelemetry.
        - Hamdles logs, traces & matrics.
      - OpenTelemetry: 
        - Reports traces to Zipkin.
        - Hamdles logs, traces & matrics.
      - OpenZipkin brave: 
- **<ins>Steps</ins>**
  - ***Project Setup:*** Docker must be installed and running.
  - ***Step-1:*** Pull zepkin image from docker hub and create container.
    - ***docker run -d -p 9411:9411 openzipkin/zipkin***
  - ***Step-2:*** Now we can access `Zipkin` locally on `http://localhost:9411/zipkin/`.

> Note: We can also create spring boot app with zepkin serevr.

- **<ins>Notes:</ins>**
  - Some important key point / takeaway note.
  - Some takeaway:
    - Sub topic takeaway.

- **<ins>References:</ins>**
  - [https://docs.spring.io/spring-boot/reference/actuator/tracing.html](https://docs.spring.io/spring-boot/reference/actuator/tracing.html)
  - [https://www.baeldung.com/tracing-services-with-zipkin](https://www.baeldung.com/tracing-services-with-zipkin)
  - [https://spring.io/projects/spring-cloud-sleuth](https://spring.io/projects/spring-cloud-sleuth)
  - [https://docs.micrometer.io/tracing/reference/1.4](https://docs.micrometer.io/tracing/reference/1.4)
  - 
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
    - Micrometer: Assigns ID to request, which remains same when other microservices are called in the same request. 
      - E.g. Req. 1: currency-conversion-service --> currency-exchange-service 
  - ***Step-2:*** Add sampling configuration to define how much percentage of request will be sampled.
    - `management.tracing.sampling.probability=1.0 # 1.0 -> 100%,  #SB3 `
    - `logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}] #SB3`
  - Add `feign-micrometer` dependency to pom.xml, to trace requests made using `feign` client. By default it's not traced.
  - Build `RestTemplate` using `RestTemplateBuilder` to enable tracing of requests.
    - If `new RestTemplate()` is used, it will not be traced.
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
      
- **<ins>Code / Config changes</ins>**
  - **Congiration:** *RestTemplateConfiguration.java*
    - imports
      - `import org.springframework.boot.web.client.RestTemplateBuilder;`
    - Annotate the method parameter for validation.
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
    - Annotate the method parameter for validation.
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
    management.tracing.sampling.probability=1.0

    # End: micrometer configuration
	```
---

