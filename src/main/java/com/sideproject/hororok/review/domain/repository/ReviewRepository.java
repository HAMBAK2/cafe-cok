package com.sideproject.hororok.review.domain.repository;

import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCafeId(Long cafeId);

    List<Review> findByMemberId(Long memberId);

    @Query("SELECT ri FROM Review r JOIN r.cafe c JOIN r.images ri " +
            "WHERE c.id = :cafeId "
    )
    List<ReviewImage> findReviewImagesByCafeId(Long cafeId);

    @Query("SELECT COUNT (r) FROM Review r WHERE r.member.id = :memberId")
    Long countReviewsByMemberId(Long memberId);
}
