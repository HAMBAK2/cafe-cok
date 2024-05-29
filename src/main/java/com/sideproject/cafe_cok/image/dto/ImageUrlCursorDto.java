package com.sideproject.cafe_cok.image.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUrlCursorDto {

    private String originUrl;
    private String thumbnailUrl;
    private Long cursor;

    @QueryProjection
    public ImageUrlCursorDto(final String originUrl,
                             final String thumbnailUrl,
                             final Long cursor) {
        this.originUrl = originUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.cursor = cursor;
    }
}
