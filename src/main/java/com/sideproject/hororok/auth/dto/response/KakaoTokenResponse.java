package com.sideproject.hororok.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResponse {

    private String refreshToken;
    private String idToken;

    private KakaoTokenResponse() {
    }

    public KakaoTokenResponse(String refreshToken, String idToken) {
        this.refreshToken = refreshToken;
        this.idToken = idToken;
    }
}
