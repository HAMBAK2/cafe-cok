package com.sideproject.cafe_cok.auth.dto.response;

import lombok.Getter;

@Getter
public class AccessTokenResponse {

    private String accessToken;

    private AccessTokenResponse(){}

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
