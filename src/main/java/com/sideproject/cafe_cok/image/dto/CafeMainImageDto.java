package com.sideproject.cafe_cok.image.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import lombok.Builder;
import lombok.Getter;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Getter
@Builder
public class CafeMainImageDto {

    private final ImageDto origin;
    private final ImageDto medium;
    private final ImageDto thumbnail;

    private static final Integer CAFE_MAIN_ORIGIN_IDX = 0;
    private static final Integer CAFE_MAIN_MEDIUM_IDX = 1;
    private static final Integer CAFE_MAIN_THUMBNAIL_IDX = 2;

    public static CafeMainImageDto from(final List<Image> images) {
        return CafeMainImageDto.builder()
                .origin(ImageDto.from(images.get(CAFE_MAIN_ORIGIN_IDX)))
                .medium(ImageDto.from(images.get(CAFE_MAIN_MEDIUM_IDX)))
                .thumbnail(ImageDto.from(images.get(CAFE_MAIN_THUMBNAIL_IDX)))
                .build();
    }
}
