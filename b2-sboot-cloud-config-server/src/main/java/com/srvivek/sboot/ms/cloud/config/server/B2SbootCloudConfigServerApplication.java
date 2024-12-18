package com.srvivek.sboot.ms.cloud.config.server;

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
