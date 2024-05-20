package com.sideproject.cafe_cok.image.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageUrlDto {

    private final String originUrl;
    private final String thumbnailUrl;


    public static ImageUrlDto from(final Image image) {
        return ImageUrlDto.builder()
                .originUrl(image.getOrigin())
                .thumbnailUrl(image.getThumbnail())
                .build();
    }
}
