package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageProfileEditResponse {

    private String nickname;
    private String picture;

    public static MyPageProfileEditResponse from(final Member member) {
        return MyPageProfileEditResponse
                .builder()
                .nickname(member.getNickname())
                .picture(member.getPicture())
                .build();
    }
}
