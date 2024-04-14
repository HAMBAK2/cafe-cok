package com.sideproject.hororok.global.config;

import com.sideproject.hororok.global.config.properties.KakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KakaoProperties.class)
public class PropertiesConfig {
}
