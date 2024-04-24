package com.sideproject.hororok.menu.dto;

import com.sideproject.hororok.menu.domain.Menu;
import com.sideproject.hororok.utils.converter.FormatConverter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuDto {

    private final Long id;
    private final String name;
    private final String price;
    private final String imageUrl;

    public static MenuDto from(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .imageUrl(menu.getImageUrl())
                .build();
    }
}
