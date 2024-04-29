package com.sideproject.hororok.review.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.keword.application.CafeReviewKeywordService;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.member.dto.response.MyPageReviewResponse;
import com.sideproject.hororok.review.domain.repository.ReviewImageRepository;
import com.sideproject.hororok.review.dto.CafeDetailReviewDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sideproject.hororok.utils.Constants.RECOMMEND_MENU_MAX_CNT;


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

    private final KeywordService keywordService;
    private final ReviewImageService reviewImageService;
    private final CafeReviewKeywordService cafeReviewKeywordService;

    @Transactional
    public ReviewCreateResponse createReview(
            final ReviewCreateRequest request, final LoginMember loginMember, final List<MultipartFile> files) {

        Cafe findCafe = cafeRepository.getById(request.getCafeId());
        findCafe.addReviewCountAndCalculateStarRating(request.getStarRating());
        Member findMember = memberRepository.getById(loginMember.getId());

        Review review =
                new Review(request.getContent(), request.getSpecialNote(), request.getStarRating(), findCafe, findMember);
        Review savedReview = reviewRepository.save(review);

        cafeReviewKeywordService.saveByReviewAndKeywordNames(savedReview, request.getKeywords());
        reviewImageService.saveByReviewAndMultipartFiles(savedReview, files);

        return new ReviewCreateResponse(savedReview.getId());
    }

    public List<ReviewDetailResponse> getReviewDetailResponses(Long cafeId){

        List<Review> reviews = reviewRepository.findByCafeId(cafeId);
        List<ReviewDetailResponse> reviewDetailResponses = new ArrayList<>();
        for (Review review : reviews) {
            CategoryKeywordsDto categoryKeywords = keywordService.getCategoryKeywords(review.getId());
            List<ReviewImageDto> images = reviewImageService.getReviewImageDtosByReviewId(review.getId());
            reviewDetailResponses.add(ReviewDetailResponse.of(review, images, categoryKeywords));
        }
        return reviewDetailResponses;
    }

    public MyPageReviewResponse getMyPageReviews(final LoginMember loginMember) {

        List<Review> findReviews = reviewRepository.findByMemberId(loginMember.getId());
        List<MyPageReviewDto> findReviewDtos = findReviews.stream().map(review -> {
            List<ReviewImageDto> findImages
                    = ReviewImageDto.fromList(reviewImageRepository.findByReviewId(review.getId()));
            List<KeywordDto> findKeywords
                    = KeywordDto.fromList(keywordRepository.findByReviewIdAndCategory(review.getId(), Category.MENU)
                    .subList(0, RECOMMEND_MENU_MAX_CNT));
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

    public ReviewDetailResponse detail(final Long reviewId) {
        Review findReview = reviewRepository.getById(reviewId);
        List<ReviewImageDto> reviewImages
                = ReviewImageDto.fromList(reviewImageRepository.findByReviewId(reviewId));
        CategoryKeywordsDto CategoryKeywords = new CategoryKeywordsDto(keywordRepository.findByReviewId(reviewId));

        return ReviewDetailResponse.of(findReview, reviewImages, CategoryKeywords);
    }

    @Transactional
    public ReviewEditResponse edit(final ReviewEditRequest request, final List<MultipartFile> files, final Long reviewId) {

        Review findReview = reviewRepository.getById(reviewId);

        if(request.getContent() != null) findReview.changeContent(request.getContent());
        if(request.getSpecialNote() != null) findReview.changeSpecialNote(request.getSpecialNote());
        if(request.getStarRating() != null) findReview.changeStarRating(request.getStarRating());
        if(request.getDeletedImageIds() != null) reviewImageService.deleteByIds(request.getDeletedImageIds());
        if(files != null) reviewImageService.saveByReviewAndMultipartFiles(findReview, files);
        if(request.getKeywords() != null)
            cafeReviewKeywordService.changeByReviewAndKeywordNames(findReview, request.getKeywords());

        return new ReviewEditResponse(reviewId);
    }
}
