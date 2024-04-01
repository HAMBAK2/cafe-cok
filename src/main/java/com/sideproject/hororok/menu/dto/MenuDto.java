package com.sideproject.hororok.menu.dto;

import com.sideproject.hororok.menu.entity.Menu;
import com.sideproject.hororok.utils.converter.FormatConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuDto {

    private final String name;
    private final String price;
    private final String imageUrl;

    public static MenuDto from(Menu menu) {
        return MenuDto.builder()
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .imageUrl(menu.getImageUrl())
                .build();
    }
}
