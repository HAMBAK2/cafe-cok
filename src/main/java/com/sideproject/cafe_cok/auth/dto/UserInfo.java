package com.sideproject.cafe_cok.auth.dto;

import lombok.Getter;

@Getter
public class UserInfo {

    private String email;

    private UserInfo() {
    }

    public UserInfo(final String email) {
        this.email = email;
    }


}
