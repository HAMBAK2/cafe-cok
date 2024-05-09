package com.sideproject.cafe_cok.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "com.sideproject.cafe_cok.auth.domain.redis")
public class RedisConfig {
}
