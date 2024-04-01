package com.sideproject.hororok.review.repository;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.review.Entity.Review;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @LogTrace
    List<Review> findByCafeId(Long cafeId);

    @LogTrace
    @Query("SELECT DISTINCT k FROM Review r " +
            "JOIN r.keywords k " +
            "WHERE r.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "ORDER BY COUNT(k) DESC ")
    List<Keyword> findKeywordInReviewByCafeIdOrderByDesc(@Param("cafeId") Long cafeId);

    @LogTrace
    @Query("SELECT r.cafe FROM Review r JOIN r.keywords k " +
            "WHERE k.name IN :keywords " +
            "GROUP BY r.cafe " +
            "HAVING COUNT(DISTINCT k.name) = COUNT(:keywords)")
    List<Cafe> findCafeWithKeywordsInReview(List<String> keywords);


    @LogTrace
    @Query("SELECT ri FROM Review r JOIN r.cafe c JOIN r.images ri " +
            "WHERE c.id = :cafeId "
    )
    List<ReviewImage> findReviewImagesByCafeId(Long cafeId);

}
