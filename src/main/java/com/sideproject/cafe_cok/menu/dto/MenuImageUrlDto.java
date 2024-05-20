package com.sideproject.cafe_cok.menu.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.menu.domain.Menu;
import lombok.Getter;

import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Getter
public class MenuImageUrlDto {

    private String name;
    private String price;
    private String originUrl;
    private String thumbnailUrl;

    protected MenuImageUrlDto() {
    }

    public MenuImageUrlDto(final Menu menu) {
        this.name = menu.getName();
        this.price = priceConvert(menu.getPrice());
    }

    public MenuImageUrlDto(final Menu menu, final Image image) {
        this(menu);
        this.originUrl = image.getOrigin();
        this.thumbnailUrl = image.getThumbnail();
    }
}
