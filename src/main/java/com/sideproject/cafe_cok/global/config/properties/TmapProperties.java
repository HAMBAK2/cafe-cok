package com.sideproject.cafe_cok.global.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("map.tmap")
@Getter
public class TmapProperties {

    private final String baseUri;
    private final String appKey;
    private final String speed;

    public TmapProperties(final String baseUri, final String appKey, final String speed) {
        this.baseUri = baseUri;
        this.appKey = appKey;
        this.speed = speed;
    }
}
