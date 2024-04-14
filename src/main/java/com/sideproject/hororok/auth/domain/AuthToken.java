package com.sideproject.hororok.auth.domain;

import com.sideproject.hororok.auth.exception.NoSuchTokenException;
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
            throw new NoSuchTokenException("회원의 리프레시 토큰이 아닙니다.");
        }
    }


}
