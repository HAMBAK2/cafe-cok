package com.sideproject.hororok.keword.dto;

import lombok.Getter;

@Getter
public class KeywordCountDto {

    private Long id;
    private String name;
    private Long count;

    public KeywordCountDto(final Long id, final String name, final Long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
