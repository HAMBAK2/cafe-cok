package com.sideproject.cafe_cok.auth.domain.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "authRefreshToken", timeToLive = 2 * 30 * 24 * 60 * 60)
@Getter
public class AuthRefreshToken {

    @Id
    private String memberId;

    private String refreshToken;

    public AuthRefreshToken(final String memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public void changeRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
