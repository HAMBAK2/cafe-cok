package com.sideproject.cafe_cok.image.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ImagesResponse {

    private List<ImageUrlDto> imageUrls;

    public static ImagesResponse from(final List<ImageUrlDto> imageUrls) {
        return ImagesResponse.builder()
                .imageUrls(imageUrls)
                .build();
    }

}
