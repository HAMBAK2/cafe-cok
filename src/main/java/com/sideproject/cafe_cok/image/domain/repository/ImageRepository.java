package com.sideproject.cafe_cok.image.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.exception.NoSuchImageException;
import com.sideproject.cafe_cok.menu.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.imageUrl FROM Image i " +
            "WHERE i.cafe.id = :cafeId " +
                "AND i.imageType = :imageType")
    List<String> findImageUrlsByCafeIdAndImageType(final Long cafeId, final ImageType imageType, final Pageable pageable);

    Optional<Image> findImageByCafeAndImageType(final Cafe cafe, final ImageType imageType);

    default Image getImageByCafeAndImageType(final Cafe cafe, final ImageType imageType) {
        return findImageByCafeAndImageType(cafe, imageType)
                .orElseThrow(NoSuchImageException::new);
    }

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
            "WHERE c.id = :cafeId " +
                "AND i.imageType = :imageType")
    List<String> findImageUrlByCafeIdAndImageType(final Long cafeId, final ImageType imageType);

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
                "AND i.imageType = :imageType " +
            "ORDER BY i.id DESC")
    Page<Image> findPageByCafeIdAndImageTypeOrderByIdDesc
            (final Long cafeId, final ImageType imageType, final Pageable pageable);

    @Query("SELECT i.imageUrl FROM Image i " +
            "JOIN i.review r " +
            "JOIN r.cafe c " +
            "WHERE c.id = :cafeId " +
                "AND i.imageType = :imageType " +
            "ORDER BY i.id DESC")
    List<String> findImageUrlsByCafeIdAndImageTypeOrderByIdDescOrderByIdDesc
            (final Long cafeId, final ImageType imageType, final Pageable pageable);

    Optional<Image> findImageByMenuAndImageType(final Menu menu, final ImageType imageType);

    default Image getImageByMenuAndImageType(final Menu menu, final ImageType imageType) {
        return findImageByMenuAndImageType(menu, imageType)
                .orElseThrow(NoSuchImageException::new);
    }
}
