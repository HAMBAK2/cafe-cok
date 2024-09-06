package com.sideproject.cafe_cok.auth.dto.response;

import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class AccessTokenResponse extends RepresentationModel<AccessTokenResponse> {

    private String accessToken;

    private AccessTokenResponse(){}

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
