package com.sideproject.hororok.image.domain.repository;

import com.sideproject.hororok.image.domain.Image;
import com.sideproject.hororok.image.domain.enums.ImageType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.imageUrl FROM Image i " +
            "WHERE i.cafe.id = :cafeId " +
                "AND i.imageType = :imageType")
    List<String> findImageUrlsByCafeIdAndImageType(final Long cafeId, final ImageType imageType, final Pageable pageable);
}
