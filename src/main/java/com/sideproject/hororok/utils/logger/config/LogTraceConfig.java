package com.sideproject.hororok.utils.logger.config;

import com.sideproject.hororok.utils.logger.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new LogTrace();
    }
}
