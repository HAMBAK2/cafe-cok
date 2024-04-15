package com.sideproject.hororok.review.domain;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.keword.domain.Keyword;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @LogTrace
    List<Review> findByCafeId(Long cafeId);

    @LogTrace
    @Query("SELECT ri FROM Review r JOIN r.cafe c JOIN r.images ri " +
            "WHERE c.id = :cafeId "
    )
    List<ReviewImage> findReviewImagesByCafeId(Long cafeId);

}
