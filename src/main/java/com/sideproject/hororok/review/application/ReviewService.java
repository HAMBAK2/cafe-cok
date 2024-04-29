package com.sideproject.hororok.review.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.keword.application.CafeReviewKeywordService;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.member.dto.response.MyPageReviewResponse;
import com.sideproject.hororok.review.domain.repository.ReviewImageRepository;
import com.sideproject.hororok.review.dto.MyPageReviewDto;
import com.sideproject.hororok.review.dto.request.ReviewEditRequest;
import com.sideproject.hororok.review.dto.response.ReviewDeleteResponse;
import com.sideproject.hororok.review.dto.response.ReviewEditGetResponse;
import com.sideproject.hororok.review.dto.response.ReviewEditPatchResponse;
import com.sideproject.hororok.utils.S3.component.S3Uploader;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.review.domain.*;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import com.sideproject.hororok.review.dto.ReviewDetailDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.review.dto.ReviewImageDto;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    private final ReviewImageService reviewImageService;
    private final CafeReviewKeywordService cafeReviewKeywordService;

    @Transactional
    public void createReview(ReviewCreateRequest request, Long userId, List<MultipartFile> files) {

        Review review = new Review();
        review.setContent(request.getContent());
        review.setSpecialNote(request.getSpecialNote());
        review.setStarRating(request.getStarRating());

        Cafe cafe = cafeRepository.getById(request.getCafeId());
        cafe.addReviewCountAndCalculateStarRating(review.getStarRating());
        review.setCafe(cafe);

        Member member = memberRepository.getById(userId);
        review.setMember(member);
        Review savedReview = reviewRepository.save(review);

        cafeReviewKeywordService.saveByReviewAndKeywordNames(savedReview, request.getKeywords());
        reviewImageService.saveByReviewAndMultipartFiles(savedReview, files);
    }

    public List<ReviewDetailDto> findReviewByCafeId(Long cafeId){

        List<Review> reviews = reviewRepository.findByCafeId(cafeId);
        List<ReviewDetailDto> reviewDetailDtos = new ArrayList<>();
        for (Review review : reviews) {

            List<CafeReviewKeyword> cafeReviewKeywords = cafeReviewKeywordRepository.findByReviewId(review.getId());
            List<KeywordDto> keywords = cafeReviewKeywords.stream()
                    .map(cafeReviewKeyword -> KeywordDto.from(cafeReviewKeyword.getKeyword()))
                    .collect(Collectors.toList());
            List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(review.getId());
            List<ReviewImageDto> reviewImageDtos = reviewImages.stream()
                    .map(ReviewImageDto::from)
                    .collect(Collectors.toList());


            reviewDetailDtos.add(ReviewDetailDto.of(review, reviewImageDtos, keywords));
        }

        return reviewDetailDtos;
    }

    public List<ReviewImage> findReviewImagesByCafeId(Long cafeId) {
        return reviewRepository.findReviewImagesByCafeId(cafeId);
    }

    
    public List<String> getReviewImageUrlsByCafeId(Long cafeId) {

        List<ReviewImage> reviewImagesByCafeId = findReviewImagesByCafeId(cafeId);
        List<String> reviewImageUrls = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImagesByCafeId) {
            reviewImageUrls.add(reviewImage.getImageUrl());
        }

        return reviewImageUrls;
    }

    public MyPageReviewResponse getMyPageReviews(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<MyPageReviewDto> findReviewDtos = findReviews.stream().map(review -> {
            List<ReviewImageDto> findImages
                    = ReviewImageDto.fromList(reviewImageRepository.findByReviewId(review.getId()));
            List<KeywordDto> findKeywords
                    = KeywordDto.fromList(keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU));
            return MyPageReviewDto.of(review, findImages, findKeywords);
        }).collect(Collectors.toList());

        return new MyPageReviewResponse(findReviewDtos);
    }

    @Transactional
    public ReviewDeleteResponse delete(final Long reviewId) {

        reviewImageRepository.deleteByReviewId(reviewId);
        cafeReviewKeywordRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
        return new ReviewDeleteResponse(reviewId);
    }

    public ReviewEditGetResponse editGet(final Long reviewId) {
        Review findReview = reviewRepository.getById(reviewId);
        List<ReviewImageDto> reviewImages
                = ReviewImageDto.fromList(reviewImageRepository.findByReviewId(reviewId));
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));

        return ReviewEditGetResponse.of(findReview, reviewImages, CategoryKeywords);
    }

    @Transactional
    public ReviewEditPatchResponse editPatch(final ReviewEditRequest request, final List<MultipartFile> files, final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);

        if(request.getContent() != null) findReview.changeContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.changeSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.changeStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) reviewImageService.deleteByIds(request.getDeletedImageIds());
        if(files != null) reviewImageService.saveByReviewAndMultipartFiles(findReview, files);
        if(request.getKeywords() != null)
            cafeReviewKeywordService.changeByReviewAndKeywordNames(findReview, request.getKeywords());

        return new ReviewEditPatchResponse(reviewId);
    }
}
