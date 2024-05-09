package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.image.domain.Image;
import com.sideproject.hororok.image.domain.enums.ImageType;
import com.sideproject.hororok.image.dto.ImageDto;

import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰;

public class ImageFixtures {

    private static final String 이미지_URL = "이미지 URL";

    public static Image 리뷰_이미지() {
        return new Image(ImageType.REVIEW_IMAGE, 이미지_URL, 리뷰());
    }

    public static ImageDto 리뷰_이미지_DTO() {
        return ImageDto.from(리뷰_이미지());
    }
}
