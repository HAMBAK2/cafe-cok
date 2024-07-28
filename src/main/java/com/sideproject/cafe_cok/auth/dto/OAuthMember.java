package com.sideproject.cafe_cok.auth.dto;

import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.enums.SocialType;
import lombok.Getter;

@Getter
public class OAuthMember {

    private String email;
    private String refreshToken;

    public OAuthMember(final String email,
                       final String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
