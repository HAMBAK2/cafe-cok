package com.sideproject.cafe_cok.keword.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "키워드, 키워드 개수 DTO")
public class KeywordCountDto {

    @Schema(description = "키워드 이름", example = "데이트/모임")
    private String name;

    @Schema(description = "키워드 선택 횟수", example = "5")
    private Long count;

    @QueryProjection
    public KeywordCountDto(final String name,
                           final Long count) {
        this.name = name;
        this.count = count;
    }
}
