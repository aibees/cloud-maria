package com.aibees.service.maria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.aibees.service.maria"})
public class MariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MariaApplication.class, args);
	}

}
