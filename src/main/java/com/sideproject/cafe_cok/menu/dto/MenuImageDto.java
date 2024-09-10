package com.sideproject.cafe_cok.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sideproject.cafe_cok.util.FormatConverter.*;

@Getter
@NoArgsConstructor
public class MenuImageDto {

    private String name;
    private String price;
    private String originUrl;
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
