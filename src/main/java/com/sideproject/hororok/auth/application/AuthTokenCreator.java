package com.sideproject.hororok.auth.application;

import com.sideproject.hororok.auth.domain.redis.AuthRefreshToken;
import com.sideproject.hororok.auth.domain.AuthToken;
import com.sideproject.hororok.auth.domain.redis.AuthRefreshTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthTokenCreator implements TokenCreator{

    private final TokenProvider tokenProvider;
    private final AuthRefreshTokenRepository authRefreshTokenRepository;

    public AuthTokenCreator(TokenProvider tokenProvider, AuthRefreshTokenRepository authRefreshTokenRepository) {
        this.tokenProvider = tokenProvider;
        this.authRefreshTokenRepository = authRefreshTokenRepository;
    }

    @Override
    public AuthToken createAuthToken(Long memberId) {
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = createRefreshToken(memberId).getRefreshToken();
        return new AuthToken(accessToken, refreshToken);
    }

    private AuthRefreshToken createRefreshToken(final Long memberId) {
        Optional<AuthRefreshToken> authRefreshToken = authRefreshTokenRepository.findById(memberId);
        if (authRefreshToken.isPresent()) {
            return authRefreshToken.get();
        }

        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));
        return authRefreshTokenRepository.save(new AuthRefreshToken(String.valueOf(memberId), refreshToken));
    }


    @Override
    public AuthToken renewAuthToken(final String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(refreshToken));

        String accessTokenForRenew = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshTokenForRenew = authRefreshTokenRepository.findById(memberId).get().getRefreshToken();

        AuthToken renewalAuthToken = new AuthToken(accessTokenForRenew, refreshTokenForRenew);
        renewalAuthToken.validateHasSameRefreshToken(refreshToken);
        return renewalAuthToken;
    }


    @Override
    public Long extractPayload(String accessToken) {
        tokenProvider.validateToken(accessToken);
        return Long.valueOf(tokenProvider.getPayload(accessToken));
    }
}
