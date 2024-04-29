package com.sideproject.hororok.review.domain.repository;

import com.sideproject.hororok.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReviewId(Long reviewId);


    void deleteByReviewId(Long reviewId);

    void deleteAllByIdIn(List<Long> ids);

    @Query("SELECT ri.imageUrl " +
            "FROM ReviewImage ri " +
            "WHERE ri.id IN (:ids)")
    List<String> findImageUrlByIdIn(List<Long> ids);

    @Query("SELECT ri.imageUrl FROM Review r " +
            "JOIN r.cafe c " +
            "JOIN ReviewImage ri ON r.id = ri.review.id " +
            "WHERE c.id = :cafeId ")
    List<String> findImageUrlByCafeId(final Long cafeId);

    @Query("SELECT ri FROM Review r " +
            "JOIN r.cafe c " +
            "JOIN ReviewImage ri ON r.id = ri.review.id " +
            "WHERE c.id = :cafeId ")
    List<ReviewImage> findByCafeId(Long cafeId);


}
