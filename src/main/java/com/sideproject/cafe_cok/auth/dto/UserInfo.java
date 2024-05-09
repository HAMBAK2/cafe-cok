package com.sideproject.cafe_cok.auth.dto;

import lombok.Getter;

@Getter
public class UserInfo {

    private String email;
    private String nickname;

    private UserInfo() {
    }

    public UserInfo(final String email, final String nickname) {
        this.email = email;
        this.nickname = nickname;
    }


}
