package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.domain.ReviewImage;
import com.sideproject.hororok.review.dto.ReviewImageDto;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import com.sideproject.hororok.review.dto.response.ReviewCreateResponse;
import com.sideproject.hororok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.hororok.review.dto.response.ReviewDetailResponse;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.카페_아이디;
import static com.sideproject.hororok.common.fixtures.KeywordFixtures.카테고리_키워드_DTO;
import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_이름_리스트;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;

public class ReviewFixtures {

    public static final Long 리뷰_개수 = 2L;

    public static final Long 리뷰_ID = 1L;
    public static final Long 리뷰_이미지_ID = 1L;
    public static final String 리뷰_내용 = "리뷰 내용";
    public static final String 리뷰_특이사항 = "리뷰 특이사항";

    public static final Integer 리뷰_별점 = 5;

    public static final String 리뷰_이미지_URL = "리뷰 이미지 URL";


    public static Review 리뷰() {
        Review review = new Review(리뷰_내용, 리뷰_특이사항, 리뷰_별점, 카페(), 사용자());
        setReviewId(review, 리뷰_ID);
        return review;
    }

    public static ReviewImage 리뷰_이미지() {
        ReviewImage reviewImage = new ReviewImage(리뷰_이미지_URL, 리뷰());
        setReviewImageId(reviewImage, 리뷰_이미지_ID);
        return reviewImage;
    }

    public static List<ReviewImage> 리뷰_이미지_리스트() {
        return Arrays.asList(리뷰_이미지());
    }

    public static ReviewImageDto 리뷰_이미지_DTO() {
        return ReviewImageDto.from(리뷰_이미지());
    }

    public static List<ReviewImageDto> 리뷰_이미지_DTO_리스트() {
        return Arrays.asList(리뷰_이미지_DTO());
    }

    public static ReviewCreateRequest 리뷰_생성_요청() {
        return new ReviewCreateRequest(카페_아이디, 리뷰_내용, 리뷰_특이사항, 키워드_이름_리스트,리뷰_별점);
    }

    public static ReviewCreateResponse 리뷰_생성_응답() {
        return new ReviewCreateResponse(리뷰_ID);
    }

    public static ReviewDeleteResponse 리뷰_삭제_응답() {
        return new ReviewDeleteResponse(리뷰_ID);
    }

    public static ReviewDetailResponse 리뷰_상세_응답() {
        return ReviewDetailResponse.of(리뷰(), 리뷰_이미지_DTO_리스트(), 카테고리_키워드_DTO());
    }

    public static Review setReviewId(Review review, final Long id) {

        try {
            Field idField = Review.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(review, id);
            return review;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static ReviewImage setReviewImageId(ReviewImage reviewImage, final Long id) {

        try {
            Field idField = ReviewImage.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(reviewImage, id);
            return reviewImage;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }





}
