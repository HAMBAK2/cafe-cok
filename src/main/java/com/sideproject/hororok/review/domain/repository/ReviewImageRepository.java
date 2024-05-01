package com.sideproject.hororok.review.domain.repository;

import com.sideproject.hororok.review.domain.ReviewImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReviewId(Long reviewId);

    List<ReviewImage> findByReviewId(final Long reviewId, final Pageable pageable);


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


    @Query("SELECT ri.imageUrl FROM ReviewImage ri " +
            "JOIN ri.review r " +
            "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
            "ORDER BY ri.id DESC")
    List<String> findImageUrlsByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable);

    @Query("SELECT ri FROM ReviewImage ri " +
            "JOIN ri.review r " +
            "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
            "ORDER BY ri.id DESC")
    Page<ReviewImage> findPageByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable);

    @Query("SELECT ri FROM ReviewImage ri " +
                "JOIN ri.review r " +
                "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
                "AND  ri.id < :id " +
            "ORDER BY ri.id DESC")
    Page<ReviewImage> findPageByCafeIdOrderByIdDesc(
            final Long cafeId, final Pageable pageable, final Long id);

}
