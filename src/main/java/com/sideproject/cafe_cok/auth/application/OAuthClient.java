package com.sideproject.cafe_cok.auth.application;

import com.sideproject.cafe_cok.auth.dto.OAuthMember;
import com.sideproject.cafe_cok.auth.dto.response.OAuthAccessTokenResponse;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);

    OAuthAccessTokenResponse getAccessToken(final String refreshToken);
}
