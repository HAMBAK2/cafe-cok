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
    @DisplayName("memberId 기반으로 review 리스트를 조회한다.")
    void find_by_member_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review1 = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
        Review review2 = new Review(REVIEW_CONTENT_2, REVIEW_SPECIAL_NOTE_2, REVIEW_STAR_RATING_2, savedCafe, savedMember);
        Review savedReview1 = reviewRepository.save(review1);
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
    @DisplayName("reviewId를 기반으로 리뷰를 조회한다.")
    void get_by_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
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
    @DisplayName("cafeId, pageable 기반으로 id 기준 정렬된 리뷰 리스트를 조회한다.")
    void find_by_cafe_id_pageable_order_by_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review1 = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
        Review review2 = new Review(REVIEW_CONTENT_2, REVIEW_SPECIAL_NOTE_2, REVIEW_STAR_RATING_2, savedCafe, savedMember);
        Review savedReview1 = reviewRepository.save(review1);
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

    @Test
    @DisplayName("cafeId, cursor, pageable 기반으로 id 기준 정렬된 리뷰 리스트를 조회한다.")
    void find_by_cafe_id_cursor_pageable_order_by_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE, CAFE_KAKAO_ID);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review1 = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
        Review review2 = new Review(REVIEW_CONTENT_2, REVIEW_SPECIAL_NOTE_2, REVIEW_STAR_RATING_2, savedCafe, savedMember);
        Review savedReview1 = reviewRepository.save(review1);
        Review savedReview2 = reviewRepository.save(review2);
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageable1 = PageRequest.of(0, REVIEW_PAGE_CNT, sort);
        PageRequest pageable2 = PageRequest.of(1, REVIEW_PAGE_CNT, sort);

        //when
        List<Review> findReviews1 = reviewRepository.findByCafeId(savedCafe.getId(), null, pageable1);
        List<Review> findReviews2 = reviewRepository.findByCafeId(savedCafe.getId(), null, pageable2);

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