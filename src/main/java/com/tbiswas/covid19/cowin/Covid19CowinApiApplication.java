package com.tbiswas.covid19.cowin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Covid19CowinApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19CowinApiApplication.class, args);
	}

}
