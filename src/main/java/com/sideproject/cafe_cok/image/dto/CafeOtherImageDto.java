package com.sideproject.cafe_cok.image.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeOtherImageDto {

    private final ImageDto origin;
    private final ImageDto thumbnail;

    private static final Integer CAFE_ORIGIN_IDX = 0;
    private static final Integer CAFE_THUMBNAIL_IDX = 1;

    public static CafeOtherImageDto from(final List<Image> images) {
        return CafeOtherImageDto.builder()
                .origin(ImageDto.from(images.get(CAFE_ORIGIN_IDX)))
                .thumbnail(ImageDto.from(images.get(CAFE_THUMBNAIL_IDX)))
                .build();
    }
}
