package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageProfileResponse {

    private String nickname;
    private String picture;
    private Long reviewCount;

    public static MyPageProfileResponse of(final Member member, final Long reviewCount) {

        return MyPageProfileResponse.builder()
                .nickname(member.getNickname())
                .picture(member.getPicture())
                .reviewCount(reviewCount)
                .build();
    }

}
