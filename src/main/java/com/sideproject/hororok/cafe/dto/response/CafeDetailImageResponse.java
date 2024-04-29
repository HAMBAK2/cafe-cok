package com.sideproject.hororok.cafe.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailImageResponse {

    private List<String> imageUrls;

    public static CafeDetailImageResponse from(final List<String> imageUrls) {
        return CafeDetailImageResponse.builder()
                .imageUrls(imageUrls)
                .build();
    }

}
