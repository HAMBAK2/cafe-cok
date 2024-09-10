package com.sideproject.cafe_cok.auth.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Hidden
@Getter
@NoArgsConstructor
public class LoginMember {

    private Long id;

    public LoginMember(final Long id) {
        this.id = id;
    }
}
