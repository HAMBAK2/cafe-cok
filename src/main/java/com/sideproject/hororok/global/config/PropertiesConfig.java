package com.sideproject.hororok.global.config;

import com.sideproject.hororok.global.config.properties.KakaoProperties;
import com.sideproject.hororok.global.config.properties.TmapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KakaoProperties.class, TmapProperties.class})
public class PropertiesConfig {
}
