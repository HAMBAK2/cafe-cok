package com.sideproject.cafe_cok.global.config.properties;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth.kakao")
@Getter
public class KakaoProperties {

    private final String baseUri;
    private final String clientId;
    private final String redirectUri;
    private final String grantType;

    public KakaoProperties(final String baseUri, final String clientId,
                           final String redirectUri, final String grantType) {
        this.baseUri = baseUri;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
    }
}
