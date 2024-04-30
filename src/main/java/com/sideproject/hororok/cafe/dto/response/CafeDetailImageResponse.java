package com.sideproject.hororok.cafe.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailImageResponse {

    private List<String> imageUrls;
    private Long cursor;
    private Boolean hasNextPage;

    public static CafeDetailImageResponse of(final List<String> imageUrls, final Boolean hasNextPage) {
        return CafeDetailImageResponse.builder()
                .imageUrls(imageUrls)
                .hasNextPage(hasNextPage)
                .build();
    }

    public static CafeDetailImageResponse of(final List<String> imageUrls, final Long cursor, final Boolean hasNextPage) {
        return CafeDetailImageResponse.builder()
                .imageUrls(imageUrls)
                .cursor(cursor)
                .hasNextPage(hasNextPage)
                .build();
    }

}
