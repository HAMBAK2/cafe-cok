package com.sideproject.hororok.image.dto;


import com.sideproject.hororok.image.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ImageDto {

    private final Long id;
    private final String imageUrl;

    public static ImageDto from(Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
