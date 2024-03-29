package com.sideproject.hororok.auth.dto;

import com.sideproject.hororok.auth.kakao.dto.KakaoAccount;
import com.sideproject.hororok.auth.kakao.dto.KakaoInfo;
import com.sideproject.hororok.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor(staticName = "of")
public class SessionUser implements Serializable {

    private KakaoAccount account;
    private String accessToken;
}
