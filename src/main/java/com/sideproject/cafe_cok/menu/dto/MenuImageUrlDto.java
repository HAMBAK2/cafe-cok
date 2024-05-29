package com.sideproject.cafe_cok.menu.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuImageUrlDto {

    private String name;
    private String price;
    private String originUrl;
    private String thumbnailUrl;

    public MenuImageUrlDto(final Menu menu) {
        this.name = menu.getName();
        this.price = priceConvert(menu.getPrice());
    }

    @QueryProjection
    public MenuImageUrlDto(final Menu menu,
                           final Image image) {
        this(menu);
        this.originUrl = image.getOrigin();
        this.thumbnailUrl = image.getThumbnail();
    }
}
