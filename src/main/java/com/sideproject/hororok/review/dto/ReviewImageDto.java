package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ReviewImageDto {

    private final Long id;
    private final String imageUrl;

    public static ReviewImageDto from(ReviewImage reviewImage) {
        return ReviewImageDto.builder()
                .id(reviewImage.getId())
                .imageUrl(reviewImage.getImageUrl())
                .build();
    }
}
