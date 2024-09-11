package com.sideproject.cafe_cok.image.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Schema(description = "이미지 정보 DTO")
public class ImageDto {

    @Schema(description = "이미지 ID", example = "1")
    private final Long id;

    @Schema(description = "원본 이미지 URL", example = "//원본_이미지_URL")
    private final String origin;

    @Schema(description = "썸네일 이미지 URL", example = "//썸네일_이미지_URL")
    private final String thumbnail;

    public static ImageDto from(final Image image) {
        return ImageDto.builder()
                .id(image.getId())
                .origin(image.getOrigin())
                .thumbnail(image.getThumbnail())
                .build();
    }

    public static List<ImageDto> fromList(final List<Image> images) {
        return images.stream()
                .map(image -> ImageDto.from(image))
                .collect(Collectors.toList());
    }

}
