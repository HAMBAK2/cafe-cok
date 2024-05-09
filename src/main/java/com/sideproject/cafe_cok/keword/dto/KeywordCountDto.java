package com.sideproject.cafe_cok.keword.dto;

import lombok.Getter;

@Getter
public class KeywordCountDto {

    private String name;
    private Long count;

    public KeywordCountDto(final String name, final Long count) {
        this.name = name;
        this.count = count;
    }
}
