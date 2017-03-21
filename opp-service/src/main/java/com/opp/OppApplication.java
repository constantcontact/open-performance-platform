package com.opp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OppApplication {

	public static void main(String[] args) {
		SpringApplication.run(OppApplication.class, args);
	}

}
