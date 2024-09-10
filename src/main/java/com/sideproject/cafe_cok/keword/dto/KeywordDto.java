package com.sideproject.cafe_cok.keword.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class KeywordDto {

    private Long id;
    private Category category;
    private String name;

    @QueryProjection
    public KeywordDto (final Keyword keyword) {
        this.id = keyword.getId();
        this.category = keyword.getCategory();
        this.name = keyword.getName();
    }
}
