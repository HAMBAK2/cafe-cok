package com.sideproject.cafe_cok.global.config;

import com.sideproject.cafe_cok.global.config.properties.KakaoProperties;
import com.sideproject.cafe_cok.global.config.properties.TmapProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KakaoProperties.class, TmapProperties.class})
public class PropertiesConfig {
}
