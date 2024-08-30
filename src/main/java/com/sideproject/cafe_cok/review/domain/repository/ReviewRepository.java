package com.sideproject.cafe_cok.review.domain.repository;

import com.sideproject.cafe_cok.review.exception.NoSuchReviewException;
import com.sideproject.cafe_cok.review.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByMemberId(final Long memberId);

    default Review getById(final Long reviewId) {
        return findById(reviewId)
                .orElseThrow(() ->
                        new NoSuchReviewException("[ID : " + reviewId + "] 에 해당하는 리뷰가 존재하지 않습니다."));
    }
}
