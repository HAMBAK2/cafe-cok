package com.sideproject.cafe_cok.keword.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Schema(description = "키워드 정보 DTO")
public class KeywordDto {

    @Schema(description = "키워드 ID", example = "1")
    private Long id;

    @Schema(description = "키워드 카테고리", example = "PURPOSE")
    private Category category;

    @Schema(description = "키워드 이름", example = "데이트/모임")
    private String name;

    @QueryProjection
    public KeywordDto (final Keyword keyword) {
        this.id = keyword.getId();
        this.category = keyword.getCategory();
        this.name = keyword.getName();
    }
}
