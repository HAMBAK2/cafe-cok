package com.sideproject.hororok.review.repository;

import com.sideproject.hororok.keword.entity.Keyword;
import com.sideproject.hororok.review.Entity.Review;
import com.sideproject.hororok.review.dto.ReviewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCafeId(Long cafeId);

    @Query("SELECT DISTINCT k FROM Review r " +
            "JOIN r.keywords k " +
            "WHERE r.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "HAVING COUNT(k) >= :keywordCount " +
            "ORDER BY COUNT(k) DESC")
    List<Keyword> findKeywordInReviewByCafeIdWithKeywordCount(@Param("cafeId") Long cafeId, @Param("keywordCount") Integer keywordCount);

}
