package com.sideproject.cafe_cok.menu.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.util.FormatConverter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CafeSaveMenuDto {

    private final Long id;
    private final String name;
    private final String price;
    private final ImageDto image;

    public static CafeSaveMenuDto from(final Menu menu) {
        return CafeSaveMenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .build();
    }

    public static CafeSaveMenuDto of(final Menu menu, final Image image) {
        return CafeSaveMenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .image(ImageDto.from(image))
                .build();
    }
}
