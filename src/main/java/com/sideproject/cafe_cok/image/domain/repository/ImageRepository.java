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

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {

    Optional<Image> findImageByCafeAndImageType(final Cafe cafe, final ImageType imageType);

    default Image getImageByCafeAndImageType(final Cafe cafe, final ImageType imageType) {
        return findImageByCafeAndImageType(cafe, imageType)
                .orElseThrow(NoSuchImageException::new);
    }

    List<Image> findImageByCafe(final Cafe cafe);

    void deleteAllByIdIn(final List<Long> ids);

    List<Image> findByReviewIdAndImageType(final Long reviewId, final ImageType imageType);

    List<Image> findByMenu(final Menu menu);

    @Query("SELECT i " +
            "FROM Image i " +
            "WHERE i.id IN (:ids)")
    List<Image> findImageByIdIn(final List<Long> ids);
}
