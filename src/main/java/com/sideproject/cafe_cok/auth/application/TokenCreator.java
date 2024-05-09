package com.sideproject.cafe_cok.auth.application;

import com.sideproject.cafe_cok.auth.domain.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final Long memberId);

    AuthToken renewAuthToken(final String outRefreshToken);

    Long extractPayload(final String accessToken);
}
