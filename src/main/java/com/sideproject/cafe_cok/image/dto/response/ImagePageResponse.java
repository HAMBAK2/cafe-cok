package com.sideproject.cafe_cok.image.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ImagePageResponse {

    private List<ImageUrlDto> imageUrls;
    private Long cursor;
    private Boolean hasNextPage;


    public ImagePageResponse(final List<ImageUrlDto> imageUrls,
                             final Boolean hasNextPage) {
        this.imageUrls = imageUrls;
        this.hasNextPage = hasNextPage;
    }


    public ImagePageResponse(final List<ImageUrlDto> imageUrls,
                             final Long cursor,
                             final Boolean hasNextPage) {
        this(imageUrls, hasNextPage);
        this.cursor = cursor;
    }
}
