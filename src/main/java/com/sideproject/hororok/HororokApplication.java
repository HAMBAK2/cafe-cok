package com.sideproject.hororok;

import com.sideproject.hororok.aop.aspect.LogTraceAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication
@EnableJpaAuditing
@Import(LogTraceAspect.class)
public class HororokApplication {

	public static void main(String[] args) {
		SpringApplication.run(HororokApplication.class, args);
	}

}
