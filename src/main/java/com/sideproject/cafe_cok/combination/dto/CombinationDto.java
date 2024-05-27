package com.sideproject.cafe_cok.combination.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CombinationDto {

    private Long id;
    private String name;
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
