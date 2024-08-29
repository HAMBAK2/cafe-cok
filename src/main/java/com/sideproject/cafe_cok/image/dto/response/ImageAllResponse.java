package com.sideproject.cafe_cok.image.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ImageAllResponse {

    private List<ImageUrlDto> imageUrls;

    public static ImageAllResponse from(final List<ImageUrlDto> imageUrls) {
        return ImageAllResponse.builder()
                .imageUrls(imageUrls)
                .build();
    }

}
