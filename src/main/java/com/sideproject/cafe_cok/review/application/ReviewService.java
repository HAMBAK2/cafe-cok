package com.sideproject.cafe_cok.review.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
import com.sideproject.cafe_cok.review.dto.response.ReviewAllResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewPageResponse;
import com.sideproject.cafe_cok.global.error.exception.MissingRequiredValueException;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.domain.repository.ImageRepository;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.keword.domain.CafeReviewKeyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.review.dto.CafeDetailReviewDto;
import com.sideproject.cafe_cok.review.dto.response.ReviewListResponse;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.review.dto.ReviewDto;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewSaveResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewIdResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewResponse;
import com.sideproject.cafe_cok.utils.ListUtils;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.FormatConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final S3Uploader s3Uploader;

    private final CafeRepository cafeRepository;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    private static final String KEYWORD = "keyword";
    public static final Integer CAFE_DETAIL_REVIEW_CNT = 5;
    private static final Boolean HAS_NEXT_PAGE = true;
    public static final Integer ALL_LIST_CNT = Integer.MAX_VALUE;

    @Transactional
    public ReviewSaveResponse save(final ReviewCreateRequest request,
                                   final LoginMember loginMember,
                                   final List<MultipartFile> files) {

        List<String> savedImageUrls = uploadImageToS3(files);

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        findCafe.addReviewCountAndCalculateStarRating(request.getStarRating());
        Member findMember = memberRepository.getById(loginMember.getId());

        Review review = new Review(request, findCafe, findMember);
        Review savedReview = reviewRepository.save(review);

        if(request.getKeywords() == null) throw new MissingRequiredValueException(KEYWORD);
        else saveByReviewAndKeywordNames(savedReview, request.getKeywords());
        saveReviewImages(savedReview, savedImageUrls);

        return new ReviewSaveResponse(savedReview.getId(), savedReview.getCafe().getId());
    }

    @Transactional
    public ReviewIdResponse delete(final Long reviewId) {

        reviewRepository.deleteById(reviewId);
        return new ReviewIdResponse(reviewId);
    }

    public ReviewResponse detail(final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);
        List<Image> findReviewImages = imageRepository.findByReviewIdAndImageType(reviewId, ImageType.REVIEW);
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));
        return ReviewResponse.of(findReview, findReviewImages, CategoryKeywords);
    }

    @Transactional
    public ReviewIdResponse edit(final ReviewEditRequest request,
                                   final List<MultipartFile> files,
                                   final Long reviewId) {

        List<String> savedImageUrls = uploadImageToS3(files);

        Review findReview = reviewRepository.getById(reviewId);
        if(request.getContent() != null) findReview.setContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.setSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.setStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) deleteByIds(request.getDeletedImageIds());

        if(request.getKeywords() == null) throw new MissingRequiredValueException(KEYWORD);
        else changeByReviewAndKeywordNames(findReview, request.getKeywords());

        saveReviewImages(findReview, savedImageUrls);

        return new ReviewIdResponse(reviewId);
    }

    public ReviewListResponse getReviews(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<ReviewDto> findReviewDtoList = findReviews.stream().map(review -> {
            List<ImageUrlDto> findImageUrlDtoList
                    = imageRepository.findImageUrlDtoListByReviewIdAndImageType(review.getId(), ImageType.REVIEW);
            List<KeywordDto> findKeywords = keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU);
            if(findKeywords.size() > RECOMMEND_MENU_MAX_CNT)
                findKeywords = findKeywords.subList(0, RECOMMEND_MENU_MAX_CNT);
            return new ReviewDto(review, findImageUrlDtoList, findKeywords);
        }).collect(Collectors.toList());

        return new ReviewListResponse(findReviewDtoList);
    }

    private void saveByReviewAndKeywordNames(final Review review,
                                             final List<String> keywordNames) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywordNames);
        List<CafeReviewKeyword> cafeReviewKeywords = findKeywords.stream()
                .map(keyword -> new CafeReviewKeyword(review.getCafe(), review, keyword))
                .collect(Collectors.toList());
        cafeReviewKeywordRepository.saveAll(cafeReviewKeywords);
    }

    private void changeByReviewAndKeywordNames(final Review review,
                                               final List<String> keywordNames) {
        List<String> findKeywordNames = keywordRepository.findNamesByReviewId(review.getId());
        if(!ListUtils.areListEqual(findKeywordNames, keywordNames)) {
            cafeReviewKeywordRepository.deleteByReviewId(review.getId());
            saveByReviewAndKeywordNames(review, keywordNames);
        }
    }

    private void saveReviewImages(final Review review,
                                  final List<String> imageUrls) {

        if(imageUrls == null || imageUrls.isEmpty()) return;

        Cafe cafe = review.getCafe();
        List<Image> reviewImages = imageUrls.stream()
                .map(imageUrl -> {
                    String convertedUrl = changePath(imageUrl, REVIEW_ORIGIN_IMAGE_DIR, REVIEW_THUMBNAIL_IMAGE_DIR);
                    return new Image(
                            ImageType.REVIEW,
                            imageUrl,
                            convertedUrl,
                            cafe,
                            review);
                })
                .collect(Collectors.toList());
        List<Image> savedImages = imageRepository.saveAll(reviewImages);
    }

    @Transactional
    public void deleteByIds(final List<Long> ids) {

        List<Image> findImages = imageRepository.findImageByIdIn(ids);
        for (Image findImage : findImages) {
            s3Uploader.delete(findImage.getOrigin());
            s3Uploader.delete(findImage.getThumbnail());
        }
        imageRepository.deleteAllByIdIn(ids);
    }

    public ReviewPageResponse findByCafeId(final Long cafeId,
                                           final Long cursor) {

        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews;

        List<Review> findReviews = reviewRepository
                .findByCafeIdAndCursorOrderByIdDesc(
                        cafeId,
                        cursor,
                        PageRequest.of(0, CAFE_DETAIL_REVIEW_CNT));
        reviews = convertReviewsToCafeDetailReviewDtoList(cafeId, findReviews);

        if(reviews.size() == CAFE_DETAIL_REVIEW_CNT) {
            Long newCursor = reviews.get(reviews.size() - 1).getId();;
            return ReviewPageResponse.of(userChoiceKeywords, reviews, newCursor, HAS_NEXT_PAGE);
        }

        return ReviewPageResponse.of(userChoiceKeywords, reviews);
    }

    public ReviewAllResponse findByCafeIdAll(final Long cafeId) {
        List<KeywordCountDto> userChoiceKeywords = getUserChoiceKeywordCounts(cafeId);
        List<CafeDetailReviewDto> reviews = getCafeDetailReviewDtoList(cafeId, ALL_LIST_CNT);;
        return ReviewAllResponse.of(userChoiceKeywords, reviews);
    }

    private List<CafeDetailReviewDto> getCafeDetailReviewDtoList(final Long cafeId,
                                                                 final Integer reviewCnt) {
        Pageable pageable = PageRequest.of(0, reviewCnt);
        List<Review> reviews = reviewRepository.findByCafeIdOrderByIdDesc(cafeId, pageable);
        return convertReviewsToCafeDetailReviewDtoList(cafeId, reviews);
    }

    private List<KeywordCountDto> getUserChoiceKeywordCounts(final Long cafeId) {
        return keywordRepository.findKeywordCountDtoListByCafeIdOrderByCountDesc(
                cafeId, PageRequest.of(0, USER_CHOICE_KEYWORD_CNT));
    }


    private List<String> uploadImageToS3(final List<MultipartFile> files) {

        if(files == null || files.isEmpty()) return null;

        List<String> savedImageUrls = files.stream()
                .map(file -> s3Uploader.upload(file, REVIEW_ORIGIN_IMAGE_DIR))
                .collect(Collectors.toList());
        if(savedImageUrls.isEmpty()) return savedImageUrls;
        s3Uploader.isExistObject(savedImageUrls);
        return savedImageUrls;
    }

    private List<CafeDetailReviewDto> convertReviewsToCafeDetailReviewDtoList(final Long cafeId,
                                                                              final List<Review> reviews) {
        List<CafeDetailReviewDto> cafeDetailReviewDtoList = new ArrayList<>();
        for (Review review : reviews) {

            Pageable pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_KEYWORD_CNT);
            List<String> recommendMenus = keywordRepository
                    .findNamesByReviewIdAndCategory(review.getId(), Category.MENU, pageable);

            pageable = PageRequest.of(0, CAFE_DETAIL_BASIC_INFO_REVIEW_IMG_CNT);
            List<ImageUrlDto> findImageUrlDtoList = imageRepository
                    .findImageUrlDtoListByCafeIdAndReviewIdOrderByIdDesc(cafeId, review.getId(), pageable);

            cafeDetailReviewDtoList.add(new CafeDetailReviewDto(review, findImageUrlDtoList, recommendMenus));
        }
        return cafeDetailReviewDtoList;
    }
}
