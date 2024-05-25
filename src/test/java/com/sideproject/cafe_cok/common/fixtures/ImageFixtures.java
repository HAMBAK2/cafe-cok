package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.menu.domain.Menu;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.cafe_cok.common.fixtures.ReviewFixtures.리뷰;

public class ImageFixtures {

    public static final Long 이미지_ID = 1L;
    public static final String 이미지_URL = "이미지 URL";
    public static final String 원본_이미지_URL = "원본_이미지_URL";
    public static final String 중간_이미지_URL = "중간_이미지_URL";
    public static final String 썸네일_이미지_URL = "썸네일_이미지_URL";

    public static Image 리뷰_이미지() {
        return new Image(ImageType.REVIEW, 원본_이미지_URL, 썸네일_이미지_URL, 카페(), 리뷰());
    }

    public static Image 카페_메인_이미지(final ImageType imageType, final Cafe cafe) {
        Image image = new Image(imageType, 원본_이미지_URL, 썸네일_이미지_URL, 중간_이미지_URL, cafe);
        setImageId(image, 이미지_ID);
        return image;
    }

    public static Image 카페_이미지(final ImageType imageType, final Cafe cafe) {
        Image image = new Image(imageType, 원본_이미지_URL, 썸네일_이미지_URL, cafe);
        setImageId(image, 이미지_ID);
        return image;
    }


    public static Image 메뉴_이미지(final ImageType imageType, final Cafe cafe, final Menu menu) {
        Image image = new Image(imageType, 원본_이미지_URL, 썸네일_이미지_URL, cafe, menu);
        setImageId(image, 이미지_ID);
        return image;
    }

    public static ImageUrlDto 리뷰_이미지_URL_DTO() {
        return ImageUrlDto.from(리뷰_이미지());
    }

    public static ImageUrlDto 카페_이미지_URL_DTO(){
        return ImageUrlDto.from(카페_이미지(ImageType.CAFE, 카페()));
    }

    public static Image setImageId(Image image, final Long id) {

        try {
            Field idField = Image.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(image, id);
            return image;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
