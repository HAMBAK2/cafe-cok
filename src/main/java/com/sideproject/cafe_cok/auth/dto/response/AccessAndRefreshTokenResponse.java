package com.sideproject.cafe_cok.auth.dto.response;


import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "로그인 성공 응답")
public class AccessAndRefreshTokenResponse extends RepresentationModel<AccessAndRefreshTokenResponse> {

    @Schema(description = "로그인 된 사용자 인증에 사용하는 토큰",
            example = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9" +
                    ".eyJzdWIiOiAiMTIzNDU2Nzg5MCIsICJuYW1lIjogIkpvaG4gRG9lIiwgImlhdCI6IDE1MTYyMzkwMjJ9" +
                    ".Ld2sPmo6DHwOdi8_H6A_8RQCCuNTNeKp1ON5I_6xZc")
    private String accessToken;

    @Schema(description = "액세스  토큰 인증 만료 시 갱신을 위해 사용하는 토큰",
            example = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9" +
                    ".eyJzdWIiOiAiMTIzNDU2Nzg5MCIsICJuYW1lIjogIkpvaG4gRG9lIiwgImlhdCI6IDE1MTYyMzkwMjJ9" +
                    ".Ld2sPmo6DHwOdi8_H6A_8RQCCuNTNeKp1ON5I_6xZc")
    private String refreshToken;

    public AccessAndRefreshTokenResponse(final String accessToken,
                                         final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
