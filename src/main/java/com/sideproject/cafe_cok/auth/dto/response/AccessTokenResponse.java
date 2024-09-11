package com.sideproject.cafe_cok.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "액세스 토큰 갱신 성공 응답")
public class AccessTokenResponse extends RepresentationModel<AccessTokenResponse> {

    @Schema(description = "갱신된 액세스 토큰",
            example = "eyJhbGciOiAiSFMyNTYiLCAidHlwIjogIkpXVCJ9" +
                    ".eyJzdWIiOiAiMTIzNDU2Nzg5MCIsICJuYW1lIjogIkpvaG4gRG9lIiwgImlhdCI6IDE1MTYyMzkwMjJ9" +
                    ".Ld2sPmo6DHwOdi8_H6A_8RQCCuNTNeKp1ON5I_6xZc")
    private String accessToken;

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
