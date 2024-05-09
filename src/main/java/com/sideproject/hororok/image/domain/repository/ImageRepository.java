package com.sideproject.hororok.image.domain.repository;

import com.sideproject.hororok.image.domain.Image;
import com.sideproject.hororok.image.domain.enums.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.imageUrl FROM Image i " +
            "WHERE i.cafe.id = :cafeId " +
                "AND i.imageType = :imageType")
    List<String> findImageUrlsByCafeIdAndImageType(final Long cafeId, final ImageType imageType, final Pageable pageable);

    void deleteAllByIdIn(final List<Long> ids);

    List<Image> findByReviewId(final Long reviewId);

    void deleteByReviewId(final Long reviewId);

    @Query("SELECT i.imageUrl " +
            "FROM Image i " +
            "WHERE i.id IN (:ids)")
    List<String> findImageUrlByIdIn(final List<Long> ids);

    @Query("SELECT i.imageUrl FROM Review r " +
            "JOIN r.cafe c " +
            "JOIN Image i ON r.id = i.review.id " +
            "WHERE c.id = :cafeId ")
    List<String> findImageUrlByCafeId(final Long cafeId);

    @Query("SELECT i FROM Image i " +
            "JOIN i.review r " +
            "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
            "AND  i.id < :id " +
            "ORDER BY i.id DESC")
    Page<Image> findPageByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable, final Long id);

    @Query("SELECT i FROM Image i " +
            "JOIN i.review r " +
            "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
            "ORDER BY i.id DESC")
    Page<Image> findPageByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable);

    @Query("SELECT i.imageUrl FROM Image i " +
            "JOIN i.review r " +
            "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
            "ORDER BY i.id DESC")
    List<String> findImageUrlsByCafeIdOrderByIdDesc(final Long cafeId, final Pageable pageable);
}
