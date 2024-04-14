package com.sideproject.hororok.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "com.sideproject.hororok.auth.domain.redis")
public class RedisConfig {
}
