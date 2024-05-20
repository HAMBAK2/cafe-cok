package com.sideproject.cafe_cok.image.dto;

import com.sideproject.cafe_cok.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeOtherImageDto {

    private final Long id;
    private final String origin;
    private final String thumbnail;

    public static CafeOtherImageDto from(final Image image) {
        return CafeOtherImageDto
                .builder()
                .id(image.getId())
                .origin(image.getOrigin())
                .thumbnail(image.getThumbnail())
                .build();
    }
}
