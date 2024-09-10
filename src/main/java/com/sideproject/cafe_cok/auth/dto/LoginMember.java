package com.sideproject.cafe_cok.auth.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "JWT 사용자 인증 후 전달되는 사용자 정보")
public class LoginMember {

    @Schema(description = "로그인한 사용자의 ID 값", example = "123")
    private Long id;

    public LoginMember(final Long id) {
        this.id = id;
    }
}
