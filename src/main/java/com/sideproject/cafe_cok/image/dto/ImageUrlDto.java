package com.sideproject.cafe_cok.image.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Schema(description = "이미지 정보 DTO")
public class ImageUrlDto {

    @Schema(description = "원본 이미지 URL", example = "//원본_이미지_URL")
    private String originUrl;

    @Schema(description = "썸네일 이미지 URL", example = "//썸네일_이미지_URL")
    private String thumbnailUrl;

    @QueryProjection
    public ImageUrlDto(final String originUrl,
                       final String thumbnailUrl) {
        this.originUrl = originUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
