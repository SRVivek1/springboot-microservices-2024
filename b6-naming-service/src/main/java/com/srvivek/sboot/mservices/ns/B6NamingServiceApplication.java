package com.srvivek.sboot.mservices.ns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class B6NamingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(B6NamingServiceApplication.class, args);
	}

}
