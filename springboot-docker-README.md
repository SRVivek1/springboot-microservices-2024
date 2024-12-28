## 1. Docker - Cretae docker image of an SBoot application
### Project ref: *c1-sb-docker-hello-world*
- **<ins>Purpose / Feature</ins>**
  - We can have **Dockerfile** in our project to create docker image from the app jar.
  - We can also configure base OS, softawares required and execution steps to launch the app. 
- **<ins>Steps</ins>**
  - ***Project Setup:*** 
    - Create\reuse a simple 'Sboot app with a rest controller'.
    - Create a ***dockerhub*** account and setup repository.
  - ***Step-1:*** Create a new file named ***Dockerfile*** in project root directory.
  - ***Step-2:*** Add the instructions to compose required softwares in target docker image.
  - ***Step-3:*** Execute below docker command to create image using `Dockerfile`.
    - ***docker build --debug -t srvivek/hello-world-docker:v1 .***
  - ***Step-4:*** Create and run container using the created docker image, and connect map local port `9000` to container port `5000`.
    ```sh
        # List images
        docker image ls
        docker images

        # List contaiers (including inactive one's)
        docker container ls -a
        
        # Create and run container using image name and tag
        docker run -d -p 9000:5000 srvivek/hello-world-docker:v1

        # Create and run container using image id.
        docker run -d -p 9000:5000 da6e304ee5d9
    ```
    - we can also use image id instead of the image name.

- **<ins>Docker hub useful links</ins>**
 
| Action | Resource link |
| ------ | ------------- |
| List repositories | [https://hub.docker.com/repositories/srvivek](https://hub.docker.com/repositories/srvivek) |
| Create repository | [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek) |


- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
- **<ins>Code / Config changes</ins>**
  - **Controller:** *HelloWorldController.java*
	```java
        @RestController
        public class HelloWorldController {

            @GetMapping("/")
            public String helloWorld() {
                return "{\"message\":\"Hello wolrd Sboot + docker v1.\"}";
            }
        }
	```
  - **Application Config:** *application.properties*
	```properties
		server.port=5000
	```

- **<ins>References:</ins>**
  - [https://docs.docker.com/engine/install/ubuntu/](https://docs.docker.com/engine/install/ubuntu/)

---

## 2. Docker - Multi stage Dockerfile
### Project ref: *xx-xxxx-xx-xxxx*
- **<ins>Purpose / Feature</ins>**
  - This is xyz feature.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Some change/step
  - ***Step-1:*** Some change/step
  - ***Step-2:*** Some change/step
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>xxx.xxxx.xxxx</groupId>
			<artifactId>xxx-xxxx-xxx-xxxxx</artifactId>
		</dependency>
- **<ins>Code / Config changes</ins>**
  - **Controller:** *AbcController.java*
    - imports
      - `import some.dependent.resource`
    - Annotate the method parameter for validation.
	```java
		@PostMapping("/users")
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

			// Impacted code goes here.
		}
	```
  - **Service:** *AbcResource.java*
    - imports
      - `import some.dependent.resource`
    - Annotate the method parameter for validation.
	```java
		public Object createUser(@Valid @RequestBody User user) {

			// Impacted code goes here.
		}
	```
  - **Application Config:** *application.properties*
	```properties
		spring.abc.xyz=false
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

- **<ins>App links:</ins>**
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)

- **<ins>References:</ins>**
  - [https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml](https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml)
  - [xyz service](http://website.com/some-resource-path)

---

