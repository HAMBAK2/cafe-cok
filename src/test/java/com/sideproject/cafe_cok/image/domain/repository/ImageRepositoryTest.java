package com.sideproject.cafe_cok.image.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.dto.ImageUrlCursorDto;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.image.exception.NoSuchImageException;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.TestConstants.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("Cafe, ImageType 기반으로 Image를 조회한다.")
    void find_image_by_cafe_image_type() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image savedImage = imageRepository.save(image);

        //when
        Image findImage = imageRepository.getImageByCafeAndImageType(savedCafe, ImageType.CAFE);

        //then
        assertThat(findImage).isEqualTo(savedImage);
        assertThat(findImage.getCafe()).isEqualTo(savedCafe);
        assertThat(findImage.getOrigin()).isEqualTo(savedImage.getOrigin());
        assertThat(findImage.getThumbnail()).isEqualTo(savedImage.getThumbnail());
    }

    @Test
    @DisplayName("Cafe, ImageType 기반으로 Image를 조회 시 잘못된 Cafe, Image로 조회하면 에러를 발생시킨다.")
    void find_image_by_non_existent_cafe_image_type() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);

        //when & then
        assertThatExceptionOfType(NoSuchImageException.class)
                .isThrownBy(() -> imageRepository.getImageByCafeAndImageType(savedCafe, ImageType.CAFE))
                .withMessage("입력하신 Cafe, ImageType에 해당하는 이미지가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이미지 id의 리스트로 해당하는 이미지를 삭제한다.")
    void delete_all_by_id_in() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image1 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image image2 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        List<Long> idList = Arrays.asList(savedImage1.getId(), savedImage2.getId());

        //when
        imageRepository.deleteAllByIdIn(idList);
        boolean result1 = imageRepository.existsById(savedImage1.getId());
        boolean result2 = imageRepository.existsById(savedImage2.getId());

        //then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("reviewId를 기반으로 Image의 리스트를 조회한다.")
    void find_by_review_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
        Review savedReview = reviewRepository.save(review);
        Image image1 = new Image(ImageType.REVIEW, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe, savedReview);
        Image image2 = new Image(ImageType.REVIEW, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe, savedReview);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);

        //when
        List<Image> findList = imageRepository.findByReviewId(savedReview.getId());

        //then
        assertThat(findList).hasSize(2);
        assertThat(findList).extracting("origin")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList).extracting("thumbnail")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }

    @Test
    @DisplayName("menu를 기반으로 Image의 리스트를 조회한다.")
    void find_by_menu() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Menu menu = new Menu(MENU_NAME_1, MENU_PRICE_1, savedCafe);
        Menu savedMenu = menuRepository.save(menu);
        Image image1 = new Image(ImageType.MENU, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe, savedMenu);
        Image image2 = new Image(ImageType.MENU, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe, savedMenu);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);

        //when
        List<Image> findList = imageRepository.findByMenu(menu);

        //then
        assertThat(findList).hasSize(2);
        assertThat(findList).extracting("origin")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList).extracting("thumbnail")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }


    @Test
    @DisplayName("이미지 id의 리스트로 해당하는 이미지를 조회한다.")
    void find_all_by_id_in() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image1 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image image2 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        List<Long> idList = Arrays.asList(savedImage1.getId(), savedImage2.getId());

        //when
        List<Image> findList = imageRepository.findAllByIdIn(idList);
        boolean result1 = imageRepository.existsById(savedImage1.getId());
        boolean result2 = imageRepository.existsById(savedImage2.getId());

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(findList).hasSize(2);
        assertThat(findList).extracting("origin")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList).extracting("thumbnail")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }

    @Test
    @DisplayName("cafeId, Pageable 기반으로 ImageUrlDto의 리스트를 조회한다(페이징)")
    void find_cafe_image_url_dto_list_by_cafe_id_pageable() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image1 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image image2 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        PageRequest pageable1 = PageRequest.of(0, IMAGE_PAGE_CNT);
        PageRequest pageable2 = PageRequest.of(1, IMAGE_PAGE_CNT);

        //when
        List<ImageUrlDto> findList1 = imageRepository.findCafeImageUrlDtoListByCafeId(savedCafe.getId(), pageable1);
        List<ImageUrlDto> findList2 = imageRepository.findCafeImageUrlDtoListByCafeId(savedCafe.getId(), pageable2);

        //then
        assertThat(findList1).hasSize(2);
        assertThat(findList2).hasSize(0);
        assertThat(findList1).extracting("originUrl")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList1).extracting("thumbnailUrl")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }


    @Test
    @DisplayName("reviewId를 기반으로 ImageUrlDto의 리스트를 조회한다.")
    void find_image_url_dto_list_by_review_id() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
        Review savedReview = reviewRepository.save(review);
        Image image1 = new Image(ImageType.REVIEW, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe, savedReview);
        Image image2 = new Image(ImageType.REVIEW, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe, savedReview);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);

        //when
        List<ImageUrlDto> findList = imageRepository.findImageUrlDtoListByReviewId(savedReview.getId());

        //then
        assertThat(findList).hasSize(2);
        assertThat(findList).extracting("originUrl")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList).extracting("thumbnailUrl")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }

    @Test
    @DisplayName("cafeId와 ImageType을 기반으로 ImageUrlDto의 리스트를 조회한다")
    void find_cafe_image_url_dto_list_by_cafe_id_image_type() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image1 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image image2 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);

        //when
        List<ImageUrlDto> findList = imageRepository.findImageUrlDtoListByCafeIdImageType(savedCafe.getId(), ImageType.CAFE);

        //then
        assertThat(findList).hasSize(2);
        assertThat(findList).extracting("originUrl")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList).extracting("thumbnailUrl")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }

    @Test
    @DisplayName("cafeId, ImageType, Pageable 기반으로 ImageUrlDto의 리스트를 조회한다(페이징)")
    void find_cafe_image_url_dto_list_by_cafe_id_image_type_pageable() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image1 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image image2 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        PageRequest pageable1 = PageRequest.of(0, IMAGE_PAGE_CNT);
        PageRequest pageable2 = PageRequest.of(1, IMAGE_PAGE_CNT);

        //when
        List<ImageUrlDto> findList1 =
                imageRepository.findImageUrlDtoListByCafeIdImageType(savedCafe.getId(), ImageType.CAFE, pageable1);
        List<ImageUrlDto> findList2 =
                imageRepository.findImageUrlDtoListByCafeIdImageType(savedCafe.getId(), ImageType.CAFE, pageable2);

        //then
        assertThat(findList1).hasSize(2);
        assertThat(findList2).hasSize(0);
        assertThat(findList1).extracting("originUrl")
                .containsExactlyInAnyOrder(image1.getOrigin(), image2.getOrigin());
        assertThat(findList1).extracting("thumbnailUrl")
                .containsExactlyInAnyOrder(image1.getThumbnail(), image2.getThumbnail());
    }

    @Test
    @DisplayName("reviewId, pageable을 기반으로 ImageUrlDto의 정렬된(내림차순) 리스트를 조회한다.")
    void find_image_url_dto_list_by_review_id_pageable_sort() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Member member = new Member(MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_SOCIAL_TYPE);
        Member savedMember = memberRepository.save(member);
        Review review = new Review(REVIEW_CONTENT, REVIEW_SPECIAL_NOTE, REVIEW_STAR_RATING, savedCafe, savedMember);
        Review savedReview = reviewRepository.save(review);
        Image image1 = new Image(ImageType.REVIEW, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe, savedReview);
        Image image2 = new Image(ImageType.REVIEW, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe, savedReview);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageable1 = PageRequest.of(0, IMAGE_PAGE_CNT, sort);
        PageRequest pageable2 = PageRequest.of(1, IMAGE_PAGE_CNT, sort);

        //when
        List<ImageUrlDto> findList1 = imageRepository.findImageUrlDtoListByReviewId(savedReview.getId(), pageable1);
        List<ImageUrlDto> findList2 = imageRepository.findImageUrlDtoListByReviewId(savedReview.getId(), pageable2);

        //then
        assertThat(findList1).hasSize(2);
        assertThat(findList2).hasSize(0);
        assertThat(findList1).extracting("originUrl")
                .containsExactly(image2.getOrigin(), image1.getOrigin());
        assertThat(findList1).extracting("thumbnailUrl")
                .containsExactly(image2.getThumbnail(), image1.getThumbnail());
    }

    @Test
    @DisplayName("cafeId, ImageType, Pageable, cursor 기반으로 ImageUrlCursorDto의 리스트를 조회한다(페이징)")
    void find_cafe_image_url_dto_list_by_cafe_id_image_type_pageable_cursor() {

        //given
        Cafe cafe = new Cafe(CAFE_NAME, CAFE_PHONE_NUMBER, CAFE_ROAD_ADDRESS, CAFE_LONGITUDE, CAFE_LATITUDE);
        Cafe savedCafe = cafeRepository.save(cafe);
        Image image1 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_1, IMAGE_THUMBNAIL_URL_1, savedCafe);
        Image image2 = new Image(ImageType.CAFE, IMAGE_ORIGIN_URL_2, IMAGE_THUMBNAIL_URL_2, savedCafe);
        Image savedImage1 = imageRepository.save(image1);
        Image savedImage2 = imageRepository.save(image2);
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageable1 = PageRequest.of(0, IMAGE_PAGE_CNT, sort);
        PageRequest pageable2 = PageRequest.of(1, IMAGE_PAGE_CNT, sort);

        //when
        List<ImageUrlCursorDto> findList1 =
                imageRepository.findImageUrlCursorDtoListByCafeIdAndImageType(savedCafe.getId(),  null, ImageType.CAFE, pageable1);
        List<ImageUrlCursorDto> findList2 =
                imageRepository.findImageUrlCursorDtoListByCafeIdAndImageType(savedCafe.getId(), null, ImageType.CAFE, pageable2);

        //then
        assertThat(findList1).hasSize(2);
        assertThat(findList2).hasSize(0);
        assertThat(findList1).extracting("originUrl")
                .containsExactly(image2.getOrigin(), image1.getOrigin());
        assertThat(findList1).extracting("thumbnailUrl")
                .containsExactly(image2.getThumbnail(), image1.getThumbnail());
    }



}