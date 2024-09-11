package com.sideproject.cafe_cok.member.dto.response;

import com.sideproject.cafe_cok.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "회원 저장/수정/개선의견 응답")
public class MemberResponse extends RepresentationModel<MemberResponse> {

    @Schema(description = "회원 닉네임", example = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 이미지 URL", example = "//회원_이미지_URL")
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
