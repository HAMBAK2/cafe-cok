package com.sideproject.cafe_cok.member.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberFeedbackRequest {

    private String content;

    public MemberFeedbackRequest(final String content) {
        this.content = content;
    }
}
