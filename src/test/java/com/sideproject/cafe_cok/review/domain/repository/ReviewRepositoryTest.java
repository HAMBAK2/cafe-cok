package com.sideproject.cafe_cok.review.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeException;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.exception.NoSuchReviewException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.sideproject.cafe_cok.constant.TestConstants.*;
import static com.sideproject.cafe_cok.constant.TestConstants.REVIEW_STAR_RATING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Test
    void 회원_ID로_리뷰_목록을_조회한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();;
        Cafe savedCafe = cafeRepository.save(cafe);

        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        Review review1 = Review.builder()
                .content(REVIEW_CONTENT)
                .specialNote(REVIEW_SPECIAL_NOTE)
                .starRating(REVIEW_STAR_RATING)
                .cafe(savedCafe)
                .member(savedMember)
                .build();
        Review savedReview1 = reviewRepository.save(review1);

        Review review2 = Review.builder()
                .content(REVIEW_CONTENT_2)
                .specialNote(REVIEW_SPECIAL_NOTE_2)
                .starRating(REVIEW_STAR_RATING_2)
                .cafe(savedCafe)
                .member(savedMember)
                .build();
        Review savedReview2 = reviewRepository.save(review2);

        //when
        List<Review> findReviews = reviewRepository.findByMemberId(savedMember.getId());

        //then
        assertThat(findReviews).hasSize(2);
        assertThat(findReviews).extracting("content")
                .containsExactlyInAnyOrder(REVIEW_CONTENT, REVIEW_CONTENT_2);
        assertThat(findReviews).extracting("specialNote")
                .containsExactlyInAnyOrder(REVIEW_SPECIAL_NOTE, REVIEW_SPECIAL_NOTE_2);
    }

    @Test
    void 리뷰_ID로_리뷰를_조회한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();
        Cafe savedCafe = cafeRepository.save(cafe);

        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        Review review = Review.builder()
                .content(REVIEW_CONTENT)
                .specialNote(REVIEW_SPECIAL_NOTE)
                .starRating(REVIEW_STAR_RATING)
                .cafe(savedCafe)
                .member(savedMember)
                .build();
        Review savedReview = reviewRepository.save(review);

        //when
        Review findReview = reviewRepository.getById(savedReview.getId());

        //then
        assertThat(findReview).isEqualTo(savedReview);
        assertThat(findReview.getMember()).isEqualTo(savedMember);
        assertThat(findReview.getCafe()).isEqualTo(savedCafe);
        assertThat(findReview.getContent()).isEqualTo(REVIEW_CONTENT);
        assertThat(findReview.getSpecialNote()).isEqualTo(REVIEW_SPECIAL_NOTE);
    }

    @Test
    @DisplayName("reviewId를 기반으로 리뷰를 조회 시 존재하지 않을 경우 에러를 반환한다.")
    void get_by_non_existent_id() {

        //when & then
        assertThatExceptionOfType(NoSuchReviewException.class)
                .isThrownBy(() -> reviewRepository.getById(NON_EXISTENT_ID))
                .withMessage("[ID : " + NON_EXISTENT_ID + "] 에 해당하는 리뷰가 존재하지 않습니다.");
    }

    @Test
    void 카페_ID_pageable을_사용하여_정렬된_리뷰_목록을_조회한다() {

        //given
        Cafe cafe = Cafe.builder()
                .name(CAFE_NAME)
                .phoneNumber(CAFE_PHONE_NUMBER)
                .roadAddress(CAFE_ROAD_ADDRESS)
                .longitude(CAFE_LONGITUDE)
                .latitude(CAFE_LATITUDE)
                .kakaoId(CAFE_KAKAO_ID)
                .build();;
        Cafe savedCafe = cafeRepository.save(cafe);

        Member member = Member.builder()
                .email(MEMBER_EMAIL)
                .nickname(MEMBER_NICKNAME)
                .socialType(MEMBER_SOCIAL_TYPE)
                .build();
        Member savedMember = memberRepository.save(member);

        Review review1 = Review.builder()
                .content(REVIEW_CONTENT)
                .specialNote(REVIEW_SPECIAL_NOTE)
                .starRating(REVIEW_STAR_RATING)
                .cafe(savedCafe)
                .member(savedMember)
                .build();
        Review savedReview1 = reviewRepository.save(review1);

        Review review2 = Review.builder()
                .content(REVIEW_CONTENT_2)
                .specialNote(REVIEW_SPECIAL_NOTE_2)
                .starRating(REVIEW_STAR_RATING_2)
                .cafe(savedCafe)
                .member(savedMember)
                .build();
        Review savedReview2 = reviewRepository.save(review2);

        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageable1 = PageRequest.of(0, REVIEW_PAGE_CNT, sort);
        PageRequest pageable2 = PageRequest.of(1, REVIEW_PAGE_CNT, sort);

        //when
        List<Review> findReviews1 = reviewRepository.findByCafeId(savedCafe.getId(), pageable1);
        List<Review> findReviews2 = reviewRepository.findByCafeId(savedCafe.getId(), pageable2);

        //then
        assertThat(findReviews1).hasSize(2);
        assertThat(findReviews2).hasSize(0);
        assertThat(findReviews1).isSortedAccordingTo((target1, target2) -> Long.compare(target2.getId(), target1.getId()));
        assertThat(findReviews1).extracting("content")
                .containsExactlyInAnyOrder(REVIEW_CONTENT, REVIEW_CONTENT_2);
        assertThat(findReviews1).extracting("specialNote")
                .containsExactlyInAnyOrder(REVIEW_SPECIAL_NOTE, REVIEW_SPECIAL_NOTE_2);
    }
}