package com.sideproject.hororok.menu.dto;

import com.sideproject.hororok.menu.domain.Menu;
import com.sideproject.hororok.utils.converter.FormatConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class MenuInfo {

    private final Long id;
    private final String name;
    private final String price;
    private final String imageUrl;

    public static MenuInfo from(Menu menu) {
        return MenuInfo.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .imageUrl(menu.getImageUrl())
                .build();
    }
}
