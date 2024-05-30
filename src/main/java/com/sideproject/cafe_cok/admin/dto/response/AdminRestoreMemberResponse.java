package com.sideproject.cafe_cok.admin.dto.response;

import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.enums.SocialType;
import lombok.Getter;


@Getter
public class AdminRestoreMemberResponse {

    private Long id;
    private String email;
    private String picture;
    private String nickname;
    private SocialType socialType;

    public AdminRestoreMemberResponse(final Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.picture = member.getPicture();
        this.nickname = member.getNickname();
        this.socialType = member.getSocialType();
    }
}
