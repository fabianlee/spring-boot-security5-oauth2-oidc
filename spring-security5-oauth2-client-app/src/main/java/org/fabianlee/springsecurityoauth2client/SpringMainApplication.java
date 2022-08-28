package org.fabianlee.springsecurityoauth2client;


import java.util.Map;

import org.fabianlee.springsecurityoauth2client.oauth2.SSLConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringMainApplication {
	
	@Autowired
	SSLConfig sslConfig;

	public static void main(String[] args) {
		
		// show only at trace level
		log.trace("==== ADFS ENV VARS =====");
		for(Map.Entry<String,String> entry:System.getenv().entrySet()) {
			if(entry.getKey().startsWith("ADFS"))
				log.trace(entry.getKey() + "=" + entry.getValue());
		}
		
		SpringApplication.run(SpringMainApplication.class, args);
	}

}
