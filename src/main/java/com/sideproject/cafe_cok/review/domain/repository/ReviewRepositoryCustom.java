package com.sideproject.cafe_cok.review.domain.repository;

import com.sideproject.cafe_cok.review.domain.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {

    List<Review> findByCafeId(final Long cafeId,
                              final Pageable pageable);
}
