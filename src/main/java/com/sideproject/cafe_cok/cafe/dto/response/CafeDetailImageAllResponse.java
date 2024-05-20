package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailImageAllResponse {

    private List<ImageUrlDto> imageUrls;

    public static CafeDetailImageAllResponse from(final List<ImageUrlDto> imageUrls) {
        return CafeDetailImageAllResponse.builder()
                .imageUrls(imageUrls)
                .build();
    }

}
