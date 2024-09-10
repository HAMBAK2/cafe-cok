package com.sideproject.cafe_cok.menu.dto;

import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuDetailDto {

    private Long id;
    private String name;
    private Integer price;
    private ImageDto image;

    @Builder
    public MenuDetailDto(final Long id,
                         final String name,
                         final Integer price,
                         final ImageDto image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }
}
