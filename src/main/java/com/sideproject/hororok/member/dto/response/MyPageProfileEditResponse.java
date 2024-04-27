package com.sideproject.hororok.member.dto.response;

import lombok.Getter;

@Getter
public class MyPageProfileEditResponse {

    private String nickname;
    private String picture;

    protected MyPageProfileEditResponse() {
    }

    public MyPageProfileEditResponse(final String nickname, final String picture) {
        this.nickname = nickname;
        this.picture = picture;
    }
}
