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

    private final Long id;
    private final String origin;
    private final String medium;
    private final String thumbnail;

    public static CafeMainImageDto from(final Image image) {
        return CafeMainImageDto
                .builder()
                .id(image.getId())
                .origin(image.getOrigin())
                .medium(image.getMedium())
                .thumbnail(image.getThumbnail())
                .build();
    }
}
