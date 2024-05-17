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

    private final String name;
    private final String price;
    private final String originImage;
    private final String thumbnailImage;

    public static MenuDto from(Menu menu) {
        return MenuDto.builder()
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .build();
    }

    public static MenuDto of(final Menu menu, final String originImage, final String thumbnailImage) {
        return MenuDto.builder()
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .originImage(originImage)
                .thumbnailImage(thumbnailImage)
                .build();
    }

}
