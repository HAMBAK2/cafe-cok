package com.sideproject.hororok.keword.dto;

import com.sideproject.hororok.keword.domain.Keyword;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class KeywordInfo {

    private final Long id;
    private final String name;


    public static KeywordInfo from(final Keyword keyword) {

        return KeywordInfo.builder()
                .id(keyword.getId())
                .name(keyword.getName())
                .build();
    }
}
