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
