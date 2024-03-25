package com.sideproject.hororok.keword.dto;

import com.sideproject.hororok.keword.entity.Keyword;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class KeywordDto {

    private final Long id;
    private final String name;


    public static KeywordDto from(final Keyword keyword) {

        return KeywordDto.builder()
                .id(keyword.getId())
                .name(keyword.getName())
                .build();
    }
}
