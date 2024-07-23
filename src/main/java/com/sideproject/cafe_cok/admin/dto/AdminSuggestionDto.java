package com.sideproject.cafe_cok.admin.dto;

import com.sideproject.cafe_cok.member.domain.Feedback;
import lombok.Getter;

@Getter
public class AdminSuggestionDto {

    private Long id;
    private String email;
    private String content;

    public AdminSuggestionDto(final Feedback feedback) {
        this.id = feedback.getId();
        this.email = feedback.getEmail();
        this.content = feedback.getContent();
    }
}
