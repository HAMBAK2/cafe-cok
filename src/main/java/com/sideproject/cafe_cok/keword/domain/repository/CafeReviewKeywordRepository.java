package com.sideproject.cafe_cok.keword.domain.repository;

import com.sideproject.cafe_cok.keword.domain.CafeReviewKeyword;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CafeReviewKeywordRepository extends JpaRepository<CafeReviewKeyword, Long> {


    List<CafeReviewKeyword> findByKeywordIn(List<Keyword> keywords);

    List<CafeReviewKeyword> findByReviewIn(List<Review> reviews);

    void deleteByReviewId(Long reviewId);



}
