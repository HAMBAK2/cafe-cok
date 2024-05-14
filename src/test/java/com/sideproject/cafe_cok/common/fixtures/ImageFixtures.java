package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.dto.ImageDto;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.cafe_cok.common.fixtures.ReviewFixtures.리뷰;

public class ImageFixtures {

    private static final String 이미지_URL = "이미지 URL";

    public static Image 리뷰_이미지() {
        return new Image(ImageType.REVIEW_THUMBNAIL, 이미지_URL, 카페(), 리뷰());
    }

    public static ImageDto 리뷰_이미지_DTO() {
        return ImageDto.from(리뷰_이미지());
    }
}
