package com.sideproject.hororok.review.application;

import com.sideproject.hororok.utils.S3.component.S3Uploader;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.review.domain.*;
import com.sideproject.hororok.review.domain.repository.ReviewRepository;
import com.sideproject.hororok.review.dto.ReviewDetailDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.review.dto.ReviewImageInfoDto;
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

    private final S3Uploader s3Uploader;

    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
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

        Cafe cafe = cafeRepository.getById(request.getCafeId());
        cafe.addReviewCountAndCalculateStarRating(review.getStarRating());
        review.setCafe(cafe);

        //유저 저장
        Member member = memberRepository.getById(userId);
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

    
    public List<ReviewDetailDto> findReviewByCafeId(Long cafeId){

        List<Review> reviews = reviewRepository.findByCafeId(cafeId);
        List<ReviewDetailDto> reviewDetailDtos = new ArrayList<>();
        for (Review review : reviews) {

            List<CafeReviewKeyword> cafeReviewKeywords = review.getCafeReviewKeywords();
            List<KeywordDto> keywords = cafeReviewKeywords.stream()
                    .map(cafeReviewKeyword -> KeywordDto.from(cafeReviewKeyword.getKeyword()))
                    .collect(Collectors.toList());
            List<ReviewImage> reviewImages = review.getImages();
            List<ReviewImageInfoDto> reviewImageInfoDtos = reviewImages.stream()
                    .map(ReviewImageInfoDto::from)
                    .collect(Collectors.toList());


            reviewDetailDtos.add(ReviewDetailDto.of(review, reviewImageInfoDtos, keywords));
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


}
