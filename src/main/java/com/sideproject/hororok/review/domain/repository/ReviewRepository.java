package com.sideproject.hororok.review.domain.repository;

import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.domain.ReviewImage;
import com.sideproject.hororok.review.exception.NoSuchReviewException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCafeId(Long cafeId);

    List<Review> findByMemberId(Long memberId);


    @Query("SELECT COUNT (r) FROM Review r WHERE r.member.id = :memberId")
    Long countReviewsByMemberId(Long memberId);

    default Review getById(Long reviewId) {
        return findById(reviewId)
                .orElseThrow(NoSuchReviewException::new);
    }
}
