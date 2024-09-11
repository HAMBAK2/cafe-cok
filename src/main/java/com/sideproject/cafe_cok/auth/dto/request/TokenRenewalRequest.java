package com.sideproject.cafe_cok.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "액세스 토큰 갱신 요청")
public class TokenRenewalRequest {

    @Schema(description = "액세스 토큰 갱신에 사용되는 토큰",
            example = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9" +
                    ".eyJzdWIiOiAiMTIzNDU2Nzg5MCIsICJuYW1lIjogIkpvaG4gRG9lIiwgImlhdCI6IDE1MTYyMzkwMjJ9" +
                    ".Ld2sPmo6DHwOdi8_H6A_8RQCCuNTNeKp1ON5I_6xZc")
    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
    private String refreshToken;

    public TokenRenewalRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
