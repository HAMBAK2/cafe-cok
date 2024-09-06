package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.member.domain.Member;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class MemberResponse extends RepresentationModel<MemberResponse> {

    private String nickname;
    private String picture;

    public MemberResponse(final String nickname,
                          final String picture) {
        this.nickname = nickname;
        this.picture = picture;
    }

    public MemberResponse(final Member member) {
        this(member.getNickname(), member.getPicture());
    }
}
