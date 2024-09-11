package com.sideproject.cafe_cok.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sideproject.cafe_cok.util.FormatConverter.*;

@Getter
@NoArgsConstructor
@Schema(description = "메뉴 정보 DTO")
public class MenuImageDto {

    @Schema(description = "메뉴 이름", example = "아메리카노")
    private String name;

    @Schema(description = "메뉴 가격", example = "5,000원")
    private String price;

    @Schema(description = "메뉴 원본 이미지 URL", example = "//원본_이미지_URL")
    private String originUrl;

    @Schema(description = "메뉴 썸네일 이미지 URL", example = "//썸네일_이미지_URL")
    private String thumbnailUrl;
    
    @QueryProjection
    public MenuImageDto(final String name,
                        final Integer price,
                        final String origin,
                        final String thumbnail) {
        this.name = name;
        this.price = priceConvert(price);
        this.originUrl = origin;
        this.thumbnailUrl = thumbnail;
    }
}
