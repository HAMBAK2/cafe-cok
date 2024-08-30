package com.sideproject.cafe_cok.image.domain.repository;

import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.dto.ImageUrlCursorDto;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageRepositoryCustom {

    List<ImageUrlDto> findCafeImageUrlDtoListByCafeId(final Long cafeId,
                                                      final Pageable pageable);

    List<ImageUrlDto> findImageUrlDtoListByReviewId(final Long reviewId);
    List<ImageUrlDto> findImageUrlDtoListByReviewId(final Long reviewId,
                                                    final Pageable pageable);

    List<ImageUrlDto> findImageUrlDtoListByCafeIdImageType(final Long cafeId,
                                                           final ImageType imageType);

    List<ImageUrlDto> findImageUrlDtoListByCafeIdImageType(final Long cafeId,
                                                           final ImageType imageType,
                                                           final Pageable pageable);

    List<ImageUrlCursorDto> findImageUrlCursorDtoListByCafeIdAndImageType(final Long cafeId,
                                                                          final Long cursor,
                                                                          final ImageType imageType,
                                                                          final Pageable pageable);
}
