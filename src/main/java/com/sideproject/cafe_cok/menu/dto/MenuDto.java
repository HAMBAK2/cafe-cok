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

    public static MenuDto from(final Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .build();
    }
}
