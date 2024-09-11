package com.sideproject.cafe_cok.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "회원 개선의견 요청")
public class MemberFeedbackRequest {

    @Schema(description = "회원 개선의견 내용", example = "회원 개선의견 내용")
    private String content;

    public MemberFeedbackRequest(final String content) {
        this.content = content;
    }
}
