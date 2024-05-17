package com.sideproject.cafe_cok.menu.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.utils.FormatConverter;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MenuDto {

    private final Long id;
    private final String name;
    private final String price;
    private final ImageDto originImage;
    private final ImageDto thumbnailImage;

    public static MenuDto from(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .build();
    }

    public static MenuDto of(final Menu menu, final Image originImage, final Image thumbnailImage) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .originImage(ImageDto.from(originImage))
                .thumbnailImage(ImageDto.from(thumbnailImage))
                .build();
    }

    public static List<MenuDto> fromList(List<Menu> menus) {
        return menus.stream()
                .map(menu -> MenuDto.from(menu))
                .collect(Collectors.toList());
    }

}
