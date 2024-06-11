package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.review.dto.request.ReviewCreateRequest;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewCreateResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewDetailResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewEditResponse;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.카페_아이디;
import static com.sideproject.cafe_cok.common.fixtures.ImageFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;

public class ReviewFixtures {

    public static final Long 리뷰_개수 = 2L;

    public static final Long 리뷰_ID = 1L;
    public static final Long 리뷰_이미지_ID = 1L;
    public static final String 리뷰_내용 = "리뷰 내용";
    public static final String 리뷰_특이사항 = "리뷰 특이사항";

    public static final Integer 리뷰_별점 = 5;

    public static final String 추천_메뉴 = "추천 메뉴";



    public static Review 리뷰() {
        Review review = new Review(리뷰_생성_요청(), 카페(), 사용자());
        setReviewId(review, 리뷰_ID);
        review.setCreateDate(LocalDateTime.now());
        return review;
    }


    public static CafeDetailReviewDto 카페_상세_리뷰_DTO() {
        return new CafeDetailReviewDto(리뷰(), Arrays.asList(리뷰_이미지_URL_DTO()), Arrays.asList(추천_메뉴));
    }


    public static ReviewCreateRequest 리뷰_생성_요청() {
        return new ReviewCreateRequest(카페_아이디, 리뷰_내용, 리뷰_특이사항, 키워드_이름_리스트,리뷰_별점);
    }

    public static ReviewEditRequest 리뷰_수정_요청() {
        return new ReviewEditRequest(리뷰_내용, 리뷰_특이사항, 키워드_이름_리스트, 리뷰_별점, Arrays.asList(리뷰_이미지_ID));
    }

    public static ReviewEditResponse 리뷰_수정_응답() {
        return new ReviewEditResponse(리뷰_ID);
    }

    public static ReviewCreateResponse 리뷰_생성_응답() {
        return new ReviewCreateResponse(리뷰_ID, 카페_아이디);
    }

    public static ReviewDeleteResponse 리뷰_삭제_응답() {
        return new ReviewDeleteResponse(리뷰_ID);
    }

    public static ReviewDetailResponse 리뷰_상세_응답() {
        return ReviewDetailResponse.of(리뷰(), Arrays.asList(리뷰_이미지()), 카테고리_키워드_DTO());
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
}
