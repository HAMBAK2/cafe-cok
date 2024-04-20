package com.sideproject.hororok.auth.dto;

import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.SocialType;
import lombok.Getter;

@Getter
public class OAuthMember {

    private final String email;
    private final String nickname;
    private final String picture;
    private final String refreshToken;

    public OAuthMember(final String email,
                       final String nickname,
                       final String picture,
                       final String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.refreshToken = refreshToken;
    }

    public Member toMember() {
        return new Member(email, nickname, picture, SocialType.KAKAO);
    }
}
