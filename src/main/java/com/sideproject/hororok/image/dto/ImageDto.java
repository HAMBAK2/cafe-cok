package com.sideproject.hororok.image.dto;

import com.sideproject.hororok.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ImageDto {

    private final Long id;
    private final String imageUrl;

    public static ImageDto from(final Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }

    public static List<ImageDto> fromList(final List<Image> images) {
        return images.stream()
                .map(image -> ImageDto.from(image))
                .collect(Collectors.toList());
    }

}
