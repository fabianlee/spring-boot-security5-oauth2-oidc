package org.fabianlee.springsecurityoauth2client;


import org.fabianlee.springsecurityoauth2client.oauth2.SSLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMainApplication {
	
	@Autowired
	SSLConfig sslConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringMainApplication.class, args);
	}

}
