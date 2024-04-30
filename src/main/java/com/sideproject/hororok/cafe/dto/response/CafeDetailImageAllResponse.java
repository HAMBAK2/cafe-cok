package com.sideproject.hororok.cafe.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailImageAllResponse {

    private List<String> imageUrls;

    public static CafeDetailImageAllResponse from(final List<String> imageUrls) {
        return CafeDetailImageAllResponse.builder()
                .imageUrls(imageUrls)
                .build();
    }

}
