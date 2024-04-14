package com.sideproject.hororok.auth.application;

import com.sideproject.hororok.auth.dto.OAuthMember;
import com.sideproject.hororok.auth.dto.response.OAuthAccessTokenResponse;

public interface OAuthClient {

    OAuthMember getOAuthMember(final String code);

    OAuthAccessTokenResponse getAccessToken(final String refreshToken);
}
