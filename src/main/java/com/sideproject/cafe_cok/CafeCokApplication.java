package com.sideproject.cafe_cok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@SpringBootApplication
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class CafeCokApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeCokApplication.class, args);
	}

}
