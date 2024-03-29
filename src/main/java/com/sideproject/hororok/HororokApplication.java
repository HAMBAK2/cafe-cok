package com.sideproject.hororok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//@SpringBootApplication
public class HororokApplication {

	public static void main(String[] args) {
		SpringApplication.run(HororokApplication.class, args);
	}

}
