package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {

    private String nickname;
    private String picture;

    public static MemberResponse from(final Member member) {
        return MemberResponse
                .builder()
                .nickname(member.getNickname())
                .picture(member.getPicture())
                .build();
    }
}
