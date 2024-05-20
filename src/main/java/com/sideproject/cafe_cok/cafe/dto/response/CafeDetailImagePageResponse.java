package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CafeDetailImagePageResponse {

    private List<ImageUrlDto> imageUrls;
    private Long cursor;
    private Boolean hasNextPage;

    public static CafeDetailImagePageResponse of(final List<ImageUrlDto> imageUrls,
                                                 final Boolean hasNextPage) {
        return CafeDetailImagePageResponse.builder()
                .imageUrls(imageUrls)
                .hasNextPage(hasNextPage)
                .build();
    }

    public static CafeDetailImagePageResponse of(final List<ImageUrlDto> imageUrls,
                                                 final Long cursor, final Boolean hasNextPage) {
        return CafeDetailImagePageResponse.builder()
                .imageUrls(imageUrls)
                .cursor(cursor)
                .hasNextPage(hasNextPage)
                .build();
    }

}
