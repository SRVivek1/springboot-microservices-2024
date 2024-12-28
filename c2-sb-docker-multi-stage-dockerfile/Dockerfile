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
