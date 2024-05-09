package com.sideproject.cafe_cok.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.sideproject.cafe_cok",
    excludeFilters = @ComponentScan.Filter(
            type = FilterType.ASPECTJ, pattern = "com.sideproject.cafe_cok.auth.domain.redis.*"
    )
)
public class JpaConfig {
}
