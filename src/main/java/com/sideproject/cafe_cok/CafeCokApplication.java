package com.sideproject.cafe_cok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CafeCokApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeCokApplication.class, args);
	}

}
