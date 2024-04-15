package com.sideproject.hororok.keword.dto;

import lombok.Getter;

@Getter
public class KeywordCount {

    private final Long id;
    private final String name;
    private final Long count;

    public KeywordCount(final Long id, final String name, final Long count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }
}
