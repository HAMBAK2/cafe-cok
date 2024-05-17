package com.sideproject.cafe_cok.menu.dto.response;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuDto;
import com.sideproject.cafe_cok.utils.FormatConverter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CafeSaveMenuResponse {

    private final Long id;
    private final String name;
    private final String price;
    private final ImageDto originImage;
    private final ImageDto thumbnailImage;

    public static CafeSaveMenuResponse from(Menu menu) {
        return CafeSaveMenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .build();
    }

    public static CafeSaveMenuResponse of(final Menu menu, final Image originImage, final Image thumbnailImage) {
        return CafeSaveMenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(FormatConverter.priceConvert(menu.getPrice()))
                .originImage(ImageDto.from(originImage))
                .thumbnailImage(ImageDto.from(thumbnailImage))
                .build();
    }
}
