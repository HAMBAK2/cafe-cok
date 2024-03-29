package com.sideproject.hororok.auth.kakao.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoAccount {
    private Profile profile;
    private String email;
}