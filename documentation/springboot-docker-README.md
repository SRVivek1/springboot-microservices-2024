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
    ```sh
        # Create an image from the 'Dockerfile' present i current directory.
        docker build --debug -t srvivek/hello-world-docker:v1 .
    ```
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

        # check logs of container
        docker container logs 6c29fd75b10a

        # login to docker
        docker login
        docker login -u $USERNAME

        # publish image on docker hub
        docker image push srvivek/hello-world-docker:v1

        # Delete image from local
        docker rmi srvivek/hello-world-docker:v1

        # docker logout
        docker logout
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
- **Docker compose:** *Dockerfile*
    ```properties
        FROM openjdk:17
        COPY ./target/*.jar app.jar
        EXPOSE 5000
        ENTRYPOINT [ "java", "-jar", "/app.jar" ]
    ```

- **<ins>References:</ins>**
  - [https://docs.docker.com/engine/install/ubuntu/](https://docs.docker.com/engine/install/ubuntu/)
  - [https://spring.io/guides/gs/spring-boot-docker](https://spring.io/guides/gs/spring-boot-docker)

---

## 2. Docker - Multi stage Dockerfile
### Project ref: *c2-sb-docker-multi-stage-dockerfile*
- **<ins>Purpose / Feature</ins>**
  - It's not recommended to build on local machine and then copy in docker image. As there may be change of different output when running build locally and run on difernt machine.
  - That's the resion when docker image is build entire build process should happen inside docker image.
    - The best practice is to build every thing which is needed inside the docker image.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Same as *c1-sb-docker-hello-world*
  - ***Step-1:*** Update ***Dockerfile*** to add maven build steps.
  - ***Step-2:*** Create a new docker image with different version tag e.g. `v2`.
  - **Docker compose:** *Dockerfile*
	```properties
		# Stage 1
        # AS build - names the stage as 'build'
        FROM maven:3.8.5-openjdk-17 AS build
        # Set workdir dir
        WORKDIR /home/app
        # copy java-maven project contents to given path
        COPY . /home/app/
        # build and package content as jar
        RUN mvn -f /home/app/pom.xml clean package

        # Stage 2
        FROM openjdk:17
        COPY --from=build /home/app/target/*.jar app.jar
        EXPOSE 5000
        ENTRYPOINT 	[ "sh", "-c", "java -jar /app.jar" ]
	```

> Note: For `ENTRYPOINT` we can also provide link to `.sh` file.

- **<ins>Notes:</ins>**
  - Drawback:
    - Build takes logner duration every time an image is build. Even a small/minor change. 
- **<ins>App links:</ins>**
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)
---

## 3. Docker - Image layer caching: improved image building performance
### Project ref: *c3-sb-docker-improved-layer-caching*
- **<ins>Purpose / Feature</ins>**
  - Each instruction in this Dockerfile translates to a layer in your final image. You can think of image layers as a stack, with each layer adding more content on top of the layers that came before it.
  - Whenever a layer changes, that layer will need to be re-built including all other layers that come after it are also affected. 
    - Then all downstream layers need to be rebuilt as well, even if they wouldn't build anything differently.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Same as *c2-sb-docker-multi-stage-dockerfile*
  - ***Step-1:*** Update ***Dockerfile*** and create layer for all resources at top which will be changed less frequently. E.g.
      - Copy POM.xml, Sboot main app runner `C3SbDockerHelloWorldApplication.java`.
  - ***Step-2:*** Add Run maven build command with above files. to create and cache layers to be reused.
    - This will also cache all the mvn dependencies which gets downloaded every time. Hence reducing build duration.
  - ***Step-3:*** Now copy all other resources from project and rerun maven build to generate final image. 
    - Each command generates a seperate layer. Hence unless there's  a change in POM.xml or app main runner class from 2nd build onwards fist five layers/commands will be used from cache. 
  - **Docker compose:** *Dockerfile*
	```properties
        # Stage 1 - build and cache resources expecting less frequent changes
        # AS build - names the stage as 'build'
        FROM maven:3.8.5-openjdk-17 AS build
        # Set workdir dir
        WORKDIR /home/app
        # copy pom.xml and main app class.
        COPY ./src/main/java/com/srvivek/sboot/mservices/docker/C3SbDockerHelloWorldApplication.java \
        /home/app/src/main/java/com/srvivek/sboot/mservices/docker/C3SbDockerHelloWorldApplication.java

        COPY ./pom.xml /home/app/pom.xml

        # build and store layer with mvn downloads and basic app
        RUN mvn -f /home/app/pom.xml clean package


        # Stage 2 : Now copy other resources which can changed frequesntly

        # copy java-maven project contents to given path
        COPY . /home/app/

        # build and package content as jar
        RUN mvn -f /home/app/pom.xml clean package


        # Stage 3
        FROM openjdk:17
        COPY --from=build /home/app/target/*.jar app.jar
        EXPOSE 5000
        ENTRYPOINT 	[ "sh", "-c", "java -jar /app.jar" ]
	```

> Note: For `ENTRYPOINT` we can also provide link to `.sh` file.

- **<ins>Notes:</ins>**
  - Drawback:
    - Build takes logner duration every time an image is build. Even a small/minor change. 
- **<ins>App links:</ins>**
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)
---

## 3. Docker - SBoot maven plugin to create image
### Project ref: *c4-sb-docker-sboot-maven-plugin*
- **<ins>Purpose / Feature</ins>**
  - 
- **<ins>Steps</ins>**
  - ***Project Setup:*** Same as *c3-sb-docker-improved-layer-caching*
  - ***Step-1:*** we don't need `Dockerfile` whlile using spring-boot-maven-plugin tobuild image.
  - ***Step-2:*** Add configuration in `spring-boot-maven-plugin` to customize docker image.
  - ***Step-2:*** Run ***mvn spring-boot:build-image*** to generate docker image.
  - ***Step-3:*** Spring uses `OCI` format to build image, docker is compatible with `OCI`.
    - Requires latest eclipse IDE and JDK 17 or higher version.
    - It uses `paketobuildpacks` to create docker image.
    - It creates very efficient and smaller image files.
  - **Spring boot maven plugin:** *pom.xml*
    ```xml
        	<build>
                <plugins>
                    <plugin>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-maven-plugin</artifactId>
                        <configuration>
                            <image>
                                <name>srvivek/hello-world-docker:v4</name>
                            </image>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
    ```
  - **Docker compose:** *Dockerfile*
	```properties
        # Stage 1 - build and cache resources expecting less frequent changes
        # AS build - names the stage as 'build'
        FROM maven:3.8.5-openjdk-17 AS build
        # Set workdir dir
        WORKDIR /home/app
        # copy pom.xml and main app class.
        COPY ./src/main/java/com/srvivek/sboot/mservices/docker/C3SbDockerHelloWorldApplication.java \
        /home/app/src/main/java/com/srvivek/sboot/mservices/docker/C3SbDockerHelloWorldApplication.java

        COPY ./pom.xml /home/app/pom.xml

        # build and store layer with mvn downloads and basic app
        RUN mvn -f /home/app/pom.xml clean package


        # Stage 2 : Now copy other resources which can changed frequesntly

        # copy java-maven project contents to given path
        COPY . /home/app/

        # build and package content as jar
        RUN mvn -f /home/app/pom.xml clean package


        # Stage 3
        FROM openjdk:17
        COPY --from=build /home/app/target/*.jar app.jar
        EXPOSE 5000
        ENTRYPOINT 	[ "sh", "-c", "java -jar /app.jar" ]
	```

> Note: For `ENTRYPOINT` we can also provide link to `.sh` file.

- **<ins>Notes:</ins>**
  - Drawback:
    - Build takes logner duration every time an image is build. Even a small/minor change. 
- **<ins>App links:</ins>**
  - [https://docs.spring.io/spring-boot/maven-plugin/build-image.html](https://docs.spring.io/spring-boot/maven-plugin/build-image.html)
  - [https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1](https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1)
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)
---