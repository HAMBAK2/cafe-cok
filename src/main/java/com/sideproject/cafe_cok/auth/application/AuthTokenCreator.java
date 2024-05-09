package com.sideproject.cafe_cok.auth.application;

import com.sideproject.cafe_cok.auth.domain.redis.AuthRefreshToken;
import com.sideproject.cafe_cok.auth.domain.AuthToken;
import com.sideproject.cafe_cok.auth.domain.redis.AuthRefreshTokenRepository;
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
        Optional<AuthRefreshToken> optionalAuthRefreshToken = authRefreshTokenRepository.findById(memberId);
        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));

        if (optionalAuthRefreshToken.isPresent()) {
            AuthRefreshToken findAuthRefreshToken = optionalAuthRefreshToken.get();
            findAuthRefreshToken.changeRefreshToken(refreshToken);
            return authRefreshTokenRepository.save(findAuthRefreshToken);
        }

        return authRefreshTokenRepository.save(new AuthRefreshToken(String.valueOf(memberId), refreshToken));
    }


    @Override
    public AuthToken renewAuthToken(final String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(refreshToken));

        String accessTokenForRenew = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshTokenForRenew = authRefreshTokenRepository.getById(memberId).getRefreshToken();

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
