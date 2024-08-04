package com.sideproject.cafe_cok.member.dto.response;

import lombok.Getter;

@Getter
public class MyPageResponse {

    private String nickname;
    private String picture;

    public MyPageResponse(String nickname, String picture) {
        this.nickname = nickname;
        this.picture = picture;
    }
}
