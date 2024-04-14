package com.sideproject.hororok.auth.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OAuthAccessTokenResponse {

    private String accessToken;

    public OAuthAccessTokenResponse() {
    }

    public OAuthAccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
