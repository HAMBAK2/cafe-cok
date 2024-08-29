package com.sideproject.cafe_cok.menu.dto;

import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import lombok.Getter;

@Getter
public class MenuDetailDto {

    private Long id;
    private String name;
    private Integer price;
    private ImageDto image;

    public MenuDetailDto(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
    }

    public MenuDetailDto(final Menu menu,
                         final ImageDto image) {
        this(menu);
        this.image = image;
    }
}
