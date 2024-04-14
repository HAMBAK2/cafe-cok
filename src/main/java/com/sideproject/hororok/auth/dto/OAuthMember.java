package com.sideproject.hororok.auth.dto;

import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.SocialType;
import lombok.Getter;

@Getter
public class OAuthMember {

    private final String email;
    private final String nickname;
    private final String refreshToken;

    public OAuthMember(final String email, final String nickname, final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.refreshToken = refreshToken;
    }

    public Member toMember() {
        return new Member(email, nickname, SocialType.KAKAO);
    }
}
