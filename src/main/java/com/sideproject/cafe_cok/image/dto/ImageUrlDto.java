package com.sideproject.cafe_cok.image.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrlDto {

    private String originUrl;
    private String thumbnailUrl;

    @QueryProjection
    public ImageUrlDto(final String originUrl,
                       final String thumbnailUrl) {
        this.originUrl = originUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
