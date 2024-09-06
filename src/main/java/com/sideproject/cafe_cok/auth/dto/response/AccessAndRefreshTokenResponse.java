package com.sideproject.cafe_cok.auth.dto.response;


import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class AccessAndRefreshTokenResponse extends RepresentationModel<AccessAndRefreshTokenResponse> {

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
