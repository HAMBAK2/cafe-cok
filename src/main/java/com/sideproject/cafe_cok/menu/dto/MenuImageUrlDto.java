package com.sideproject.cafe_cok.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sideproject.cafe_cok.util.FormatConverter.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuImageUrlDto {

    private String name;
    private String price;
    private String originUrl;
    private String thumbnailUrl;

    @QueryProjection
    public MenuImageUrlDto(final String name,
                           final Integer price,
                           final String origin,
                           final String thumbnail) {
        this.name = name;
        this.price = priceConvert(price);
        this.originUrl = origin;
        this.thumbnailUrl = thumbnail;
    }
}
