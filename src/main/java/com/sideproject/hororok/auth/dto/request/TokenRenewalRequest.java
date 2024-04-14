package com.sideproject.hororok.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TokenRenewalRequest {

    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
    private String refreshToken;

    private TokenRenewalRequest() {
    }

    public TokenRenewalRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
