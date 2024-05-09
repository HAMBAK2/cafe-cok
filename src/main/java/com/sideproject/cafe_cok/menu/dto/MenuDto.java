package com.sideproject.cafe_cok.menu.dto;

import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.utils.FormatConverter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
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

    public static List<MenuDto> fromList(List<Menu> menus) {
        return menus.stream()
                .map(menu -> MenuDto.from(menu))
                .collect(Collectors.toList());
    }

}
