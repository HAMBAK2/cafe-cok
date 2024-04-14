package com.sideproject.hororok;

import com.sideproject.hororok.aop.aspect.LogTraceAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(LogTraceAspect.class)
public class HororokApplication {

	public static void main(String[] args) {
		SpringApplication.run(HororokApplication.class, args);
	}

}
