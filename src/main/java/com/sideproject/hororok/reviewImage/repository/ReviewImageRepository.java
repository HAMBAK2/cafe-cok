package com.sideproject.hororok.reviewImage.repository;

import com.sideproject.hororok.reviewImage.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {


    @Query(
            "select rv.imageUrl from ReviewImage rv " +
            "join rv.review r " +
            "join r.cafe c " +
            "where c.id = :cafeId ")
    List<String> findReviewImageUrlsByCafeId(Long cafeId);
}
