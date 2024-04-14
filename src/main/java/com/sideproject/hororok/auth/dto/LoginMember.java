package com.sideproject.hororok.auth.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;

@Getter
@Hidden
public class LoginMember {

    private Long id;

    private LoginMember(){}

    public LoginMember(final Long id) {
        this.id = id;
    }
}
