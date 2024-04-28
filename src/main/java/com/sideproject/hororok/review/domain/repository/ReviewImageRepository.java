package com.sideproject.hororok.review.domain.repository;

import com.sideproject.hororok.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReviewId(Long reviewId);


    void deleteByReviewId(Long reviewId);

}
