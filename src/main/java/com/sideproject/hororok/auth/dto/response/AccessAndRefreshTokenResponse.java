package com.sideproject.hororok.auth.dto.response;


import lombok.Getter;

@Getter
public class AccessAndRefreshTokenResponse {

    private String accessToken;
    private String refreshToken;

    public AccessAndRefreshTokenResponse() {
    }

    public AccessAndRefreshTokenResponse(final String accessToken,
                                         final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
