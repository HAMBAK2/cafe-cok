package com.sideproject.hororok.review.service;

import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.review.Entity.Review;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDto> findReviewByCafeId(Long cafeId){

        List<Review> reviews = reviewRepository.findByCafeId(cafeId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Review review : reviews) {
            reviewDtos.add(ReviewDto.from(review));
        }

        return reviewDtos;
    }

    public List<KeywordDto> findKeywordInReviewByCafeIdWithKeywordCount(Long cafeId, Integer keywordCount) {

        List<Keyword> keywords = reviewRepository.findKeywordInReviewByCafeIdWithKeywordCount(cafeId, keywordCount);
        List<KeywordDto> keywordDtoList = new ArrayList<>();
        for (Keyword keyword : keywords) {
            keywordDtoList.add(KeywordDto.from(keyword));
        }

        return keywordDtoList;
    }
}
