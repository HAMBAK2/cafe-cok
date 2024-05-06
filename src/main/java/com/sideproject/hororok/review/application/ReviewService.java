package com.sideproject.hororok.review.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.global.error.exception.MissingRequiredValueException;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.member.dto.response.MyPageReviewResponse;
import com.sideproject.hororok.review.domain.repository.ReviewImageRepository;
import com.sideproject.hororok.review.dto.MyPageReviewDto;
import com.sideproject.hororok.review.dto.request.ReviewEditRequest;
import com.sideproject.hororok.review.dto.response.ReviewCreateResponse;
import com.sideproject.hororok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.hororok.review.dto.response.ReviewDetailResponse;
import com.sideproject.hororok.review.dto.response.ReviewEditResponse;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.review.domain.*;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.review.dto.ReviewImageDto;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import com.sideproject.hororok.utils.ListUtils;
import com.sideproject.hororok.utils.S3.component.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.hororok.utils.Constants.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final S3Uploader s3Uploader;

    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    private static final String KEYWORD = "keyword";

    @Transactional
    public ReviewCreateResponse createReview(
            final ReviewCreateRequest request, final LoginMember loginMember, final List<MultipartFile> files) {

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        findCafe.addReviewCountAndCalculateStarRating(request.getStarRating());
        Member findMember = memberRepository.getById(loginMember.getId());

        Review review =
                new Review(request.getContent(), request.getSpecialNote(), request.getStarRating(), findCafe, findMember);
        Review savedReview = reviewRepository.save(review);

        if(request.getKeywords() == null) {
            throw new MissingRequiredValueException(KEYWORD);
        } else saveByReviewAndKeywordNames(savedReview, request.getKeywords());
        if(files != null) saveByReviewAndMultipartFiles(savedReview, files);

        return new ReviewCreateResponse(savedReview.getId());
    }

    @Transactional
    public ReviewDeleteResponse delete(final Long reviewId) {

        reviewImageRepository.deleteByReviewId(reviewId);
        cafeReviewKeywordRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
        return new ReviewDeleteResponse(reviewId);
    }

    public ReviewDetailResponse detail(final Long reviewId) {
        Review findReview = reviewRepository.getById(reviewId);
        List<ReviewImageDto> reviewImages
                = ReviewImageDto.fromList(reviewImageRepository.findByReviewId(reviewId));
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));

        return ReviewDetailResponse.of(findReview, reviewImages, CategoryKeywords);
    }

    public MyPageReviewResponse getMyPageReviews(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<MyPageReviewDto> findReviewDtos = findReviews.stream().map(review -> {
            List<ReviewImageDto> findImages
                    = ReviewImageDto.fromList(reviewImageRepository.findByReviewId(review.getId()));
            List<KeywordDto> findKeywords
                    = KeywordDto.fromList(keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU));

            if(findKeywords.size() > RECOMMEND_MENU_MAX_CNT)
                findKeywords = findKeywords.subList(0, RECOMMEND_MENU_MAX_CNT);

            return MyPageReviewDto.of(review, findImages, findKeywords);
        }).collect(Collectors.toList());

        return new MyPageReviewResponse(findReviewDtos);
    }

    private void saveByReviewAndKeywordNames(final Review review, final List<String> keywordNames) {

        for (String keywordName : keywordNames) {
            Keyword findKeyword = keywordRepository.getByName(keywordName);
            CafeReviewKeyword cafeReviewKeyword = new CafeReviewKeyword(review.getCafe(), review, findKeyword);
            cafeReviewKeywordRepository.save(cafeReviewKeyword);
        }
    }

    private void changeByReviewAndKeywordNames(final Review review, final List<String> keywordNames) {
        List<String> findKeywordNames = keywordRepository.findNamesByReviewId(review.getId());
        if(!ListUtils.areListEqual(findKeywordNames, keywordNames)) {
            cafeReviewKeywordRepository.deleteByReviewId(review.getId());
            saveByReviewAndKeywordNames(review, keywordNames);
        }
    }

    @Transactional
    public ReviewEditResponse edit(final ReviewEditRequest request, final List<MultipartFile> files, final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);

        if(request.getContent() != null) findReview.changeContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.changeSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.changeStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) deleteByIds(request.getDeletedImageIds());
        if(files != null) saveByReviewAndMultipartFiles(findReview, files);
        if(request.getKeywords() == null) throw new MissingRequiredValueException(KEYWORD);
        else changeByReviewAndKeywordNames(findReview, request.getKeywords());

        return new ReviewEditResponse(reviewId);
    }

    private void saveByReviewAndMultipartFiles(final Review review, final List<MultipartFile> files) {

        List<ReviewImage> reviewImages = saveImagesObjectStorage(review, files);
        if(reviewImages.isEmpty()) return;
        for (ReviewImage reviewImage : reviewImages) reviewImageRepository.save(reviewImage);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {

        List<String> findImageUrls = reviewImageRepository.findImageUrlByIdIn(ids);
        for (String imageUrl : findImageUrls) s3Uploader.delete(imageUrl);
        reviewImageRepository.deleteAllByIdIn(ids);
    }

    private List<ReviewImage> saveImagesObjectStorage(final Review review, final List<MultipartFile> files) {

        List<ReviewImage> reviewImages = new ArrayList<>();

        if(files.isEmpty()) return reviewImages;

        for (MultipartFile file : files) {
            ReviewImage reviewImage =
                    new ReviewImage(s3Uploader.upload(file, REVIEW_IMAGE_DIR)
                            .replace(IMAGE_URL_PREFIX, ""), review);
            reviewImages.add(reviewImage);
        }
        return reviewImages;
    }
}
