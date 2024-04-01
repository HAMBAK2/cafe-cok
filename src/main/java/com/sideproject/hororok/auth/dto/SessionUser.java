package com.sideproject.hororok.auth.dto;

import com.sideproject.hororok.auth.kakao.dto.KakaoAccount;
import com.sideproject.hororok.user.entity.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private KakaoAccount account;
    private String accessToken;

    public SessionUser(KakaoAccount account, String accessToken) {
        this.account = account;
        this.accessToken = accessToken;
    }
}
