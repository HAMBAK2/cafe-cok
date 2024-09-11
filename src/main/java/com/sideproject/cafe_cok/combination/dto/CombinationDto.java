package com.sideproject.cafe_cok.combination.dto;


import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "조합 정보 DTO")
public class CombinationDto {

    @Schema(description = "조합 ID", example = "1")
    private Long id;

    @Schema(description = "조합 이름", example = "조합 이름")
    private String name;

    @Schema(description = "조합 아이콘", example = "조합 아이콘")
    private String icon;

    @QueryProjection
    public CombinationDto(final Long id,
                          final String name,
                          final String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }
}
