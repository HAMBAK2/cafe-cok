package com.sideproject.cafe_cok.review.domain.repository;

import com.sideproject.cafe_cok.review.exception.NoSuchReviewException;
import com.sideproject.cafe_cok.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByCafeId(Long cafeId);

    List<Review> findByMemberId(Long memberId);

    List<Review> findByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable);

    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.cafe.id = :cafeId " +
                "AND r.id < :cursor " +
            "ORDER BY r.id DESC")
    Page<Review> findByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable, final Long cursor);

    @Query("SELECT COUNT (r) FROM Review r WHERE r.member.id = :memberId")
    Long countReviewsByMemberId(Long memberId);

    @Query("SELECT COUNT (r) " +
            "FROM Review r " +
            "WHERE r.cafe.id = :cafeId")
    Long countReviewByCafeId(final Long cafeId);

    default Review getById(Long reviewId) {
        return findById(reviewId)
                .orElseThrow(NoSuchReviewException::new);
    }
}
