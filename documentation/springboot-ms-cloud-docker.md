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

