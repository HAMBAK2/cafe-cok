package com.sideproject.cafe_cok.image.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ImageUrlDto> fromList(final List<Image> images) {
        return images.stream()
                .map(image -> ImageUrlDto.from(image))
                .collect(Collectors.toList());
    }

}
