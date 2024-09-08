package com.sideproject.cafe_cok.review.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.cafe.domain.repository.CafeRepository;
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
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.review.dto.response.ReviewsResponse;
import com.sideproject.cafe_cok.review.domain.Review;
import com.sideproject.cafe_cok.review.domain.repository.ReviewRepository;
import com.sideproject.cafe_cok.review.dto.ReviewDto;
import com.sideproject.cafe_cok.review.dto.request.ReviewEditRequest;
import com.sideproject.cafe_cok.review.dto.response.ReviewSaveResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewIdResponse;
import com.sideproject.cafe_cok.review.dto.response.ReviewResponse;
import com.sideproject.cafe_cok.util.ListUtil;
import com.sideproject.cafe_cok.util.S3.component.S3Uploader;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.review.dto.request.ReviewSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.cafe_cok.util.Constants.*;
import static com.sideproject.cafe_cok.util.FormatConverter.*;

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

    @Transactional
    public ReviewSaveResponse save(final ReviewSaveRequest request,
                                   final LoginMember loginMember,
                                   final List<MultipartFile> files) {

        List<String> savedImageUrls = uploadImageToS3(files);

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        findCafe.addReviewCountAndCalculateStarRating(request.getStarRating());
        Member findMember = memberRepository.getById(loginMember.getId());

        Review review = new Review(request, findCafe, findMember);
        Review savedReview = reviewRepository.save(review);

        if(request.getKeywords() == null) throw new MissingRequiredValueException("keyword");
        else saveByReviewAndKeywordNames(savedReview, request.getKeywords());
        saveReviewImages(savedReview, savedImageUrls);

        return new ReviewSaveResponse(savedReview.getId(), savedReview.getCafe().getId());
    }

    @Transactional
    public ReviewIdResponse delete(final Long reviewId) {

        reviewRepository.deleteById(reviewId);
        return new ReviewIdResponse(reviewId);
    }

    public ReviewResponse find(final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);
        List<Image> findReviewImages = imageRepository.findByReviewId(reviewId);
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));
        return ReviewResponse.of(findReview, findReviewImages, CategoryKeywords);
    }

    @Transactional
    public ReviewIdResponse update(final ReviewEditRequest request,
                                   final List<MultipartFile> files,
                                   final Long reviewId) {

        List<String> savedImageUrls = uploadImageToS3(files);

        Review findReview = reviewRepository.getById(reviewId);
        if(request.getContent() != null) findReview.setContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.setSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.setStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) deleteByIds(request.getDeletedImageIds());

        if(request.getKeywords() == null) throw new MissingRequiredValueException("keyword");
        else changeByReviewAndKeywordNames(findReview, request.getKeywords());

        saveReviewImages(findReview, savedImageUrls);

        return new ReviewIdResponse(reviewId);
    }

    public ReviewsResponse findList(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<ReviewDto> findReviewDtoList = findReviews.stream().map(review -> {
            List<ImageUrlDto> findImageUrlDtoList
                    = imageRepository.findImageUrlDtoListByReviewId(review.getId());
            List<KeywordDto> findKeywords = keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU);
            if(findKeywords.size() > RECOMMEND_MENU_MAX_CNT)
                findKeywords = findKeywords.subList(0, RECOMMEND_MENU_MAX_CNT);
            return new ReviewDto(review, findImageUrlDtoList, findKeywords);
        }).collect(Collectors.toList());

        return new ReviewsResponse(findReviewDtoList);
    }

    private void saveByReviewAndKeywordNames(final Review review,
                                             final List<String> keywordNames) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywordNames);
        List<CafeReviewKeyword> cafeReviewKeywords = findKeywords.stream()
                .map(keyword -> CafeReviewKeyword.builder()
                        .cafe(review.getCafe())
                        .review(review)
                        .keyword(keyword)
                        .build())
                .collect(Collectors.toList());
        cafeReviewKeywordRepository.saveAll(cafeReviewKeywords);
    }

    private void changeByReviewAndKeywordNames(final Review review,
                                               final List<String> keywordNames) {
        List<String> findKeywordNames = keywordRepository.findNamesByReviewId(review.getId());
        if(!ListUtil.areListEqual(findKeywordNames, keywordNames)) {
            cafeReviewKeywordRepository.deleteByReviewId(review.getId());
            saveByReviewAndKeywordNames(review, keywordNames);
        }
    }

    private void saveReviewImages(final Review review,
                                  final List<String> imageUrls) {

        if(imageUrls == null || imageUrls.isEmpty()) return;

        Cafe cafe = review.getCafe();
        List<Image> reviewImages = imageUrls.stream()
                .map(originUrl -> {
                    String thumbnailUrl = changePath(originUrl, REVIEW_ORIGIN_IMAGE_DIR, REVIEW_THUMBNAIL_IMAGE_DIR);
                    return Image.builder()
                            .imageType(ImageType.REVIEW)
                            .origin(originUrl)
                            .thumbnail(thumbnailUrl)
                            .cafe(cafe)
                            .review(review)
                            .build();
                })
                .collect(Collectors.toList());
        List<Image> savedImages = imageRepository.saveAll(reviewImages);
    }

    @Transactional
    public void deleteByIds(final List<Long> ids) {

        List<Image> findImages = imageRepository.findAllByIdIn(ids);
        for (Image findImage : findImages) {
            s3Uploader.delete(findImage.getOrigin());
            s3Uploader.delete(findImage.getThumbnail());
        }
        imageRepository.deleteAllByIdIn(ids);
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
}
