package com.sideproject.hororok.auth.dto;

import com.sideproject.hororok.user.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String email;
    private String nickname;
    private String accessToken;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

    public SessionUser(User user, String accessToken) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.accessToken = accessToken;
    }

}
