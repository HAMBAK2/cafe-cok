package com.sideproject.hororok.review.application;

import com.sideproject.hororok.S3.component.S3Uploader;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeRepository;
import com.sideproject.hororok.keword.application.KeywordService;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.member.domain.MemberRepository;
import com.sideproject.hororok.review.domain.*;
import com.sideproject.hororok.review.dto.ReviewDetail;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.review.dto.ReviewImageInfo;
import com.sideproject.hororok.review.dto.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;
    private final MemberRepository memberRepository;
    private final CafeRepository cafeRepository;
    private final KeywordRepository keywordRepository;
    private final KeywordService keywordService;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    private final String REVIEW_IMAGE_URL_PREFIX = "https:";

    @Transactional
    public void createReview(ReviewCreateRequest request, Long userId, List<MultipartFile> files) throws IOException {


        List<ReviewImage> reviewImages = new ArrayList<>();
        if(files != null) {
            reviewImages = saveImagesObjectStorage(files);
        }

        //2. 이미지 url을 받아와서 리뷰 저장
        Review review = new Review();
        review.setContent(request.getContent());
        review.setSpecialNote(request.getSpecialNote());
        review.setStarRating(request.getStarRating());
        review.setImages(reviewImages);

        Cafe cafe = cafeRepository.findById(request.getCafeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid cafe id"));
        cafe.addReviewCountAndCalculateStarRating(review.getStarRating());
        review.setCafe(cafe);

        //유저 저장
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        review.setMember(member);

        for (ReviewImage reviewImage : reviewImages) {
            reviewImage.setReview(review);
        }
        review.getImages().addAll(reviewImages);

        Review savedReview = reviewRepository.save(review);

        //카테고리 키워드 저장
        List<String> keywordNames = request.getKeywords();

        for (String keywordName : keywordNames) {
            Keyword findKeyword = keywordRepository.findByName(keywordName)
                    .orElseThrow(() -> new IllegalArgumentException("잘못된 키워드 이름입니다."));

            CafeReviewKeyword cafeReviewKeyword = new CafeReviewKeyword();
            cafeReviewKeyword.setReview(savedReview);
            cafeReviewKeyword.setKeyword(findKeyword);
            cafeReviewKeyword.setCafe(cafe);
            cafeReviewKeywordRepository.save(cafeReviewKeyword);
        }
    }

    private List<ReviewImage> saveImagesObjectStorage(List<MultipartFile> files) throws IOException {
        List<ReviewImage> reviewImages = new ArrayList<>();
        for (MultipartFile file : files) {
            ReviewImage reviewImage =
                    new ReviewImage(s3Uploader.upload(file, "review")
                            .replace(REVIEW_IMAGE_URL_PREFIX, ""));
            reviewImages.add(reviewImage);
        }

        return reviewImages;
    }

    
    public List<ReviewDetail> findReviewByCafeId(Long cafeId){

        List<Review> reviews = reviewRepository.findByCafeId(cafeId);
        List<ReviewDetail> reviewDetails = new ArrayList<>();
        for (Review review : reviews) {

            List<CafeReviewKeyword> cafeReviewKeywords = review.getCafeReviewKeywords();
            List<KeywordInfo> keywords = keywordService.getKeywordInfosByCafeReviewKeywords(cafeReviewKeywords);
            List<ReviewImage> reviewImages = review.getImages();
            List<ReviewImageInfo> reviewImageInfos = reviewImages.stream()
                    .map(ReviewImageInfo::from)
                    .collect(Collectors.toList());


            reviewDetails.add(ReviewDetail.of(review, reviewImageInfos, keywords));
        }

        return reviewDetails;
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


}
