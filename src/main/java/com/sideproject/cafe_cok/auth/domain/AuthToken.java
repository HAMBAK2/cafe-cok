package com.sideproject.cafe_cok.auth.domain;

import com.sideproject.cafe_cok.auth.exception.NoSuchRefreshTokenException;
import lombok.Getter;

@Getter
public class AuthToken {

    private String accessToken;
    private String refreshToken;

    public AuthToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void validateHasSameRefreshToken(final String targetRefreshToken) {
        if(!refreshToken.equals(targetRefreshToken)) {
            throw new NoSuchRefreshTokenException("회원의 리프레시 토큰이 아닙니다.");
        }
    }


}
