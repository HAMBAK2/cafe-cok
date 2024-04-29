package com.sideproject.hororok.auth.dto;

import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.enums.SocialType;
import lombok.Getter;

@Getter
public class OAuthMember {

    private String email;
    private String nickname;
    private String refreshToken;

    public OAuthMember(final String email,
                       final String nickname,
                       final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }

    public Member toMember() {
        return new Member(email, nickname, SocialType.KAKAO);
    }
}
