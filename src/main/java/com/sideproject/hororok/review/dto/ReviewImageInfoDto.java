package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewImageInfoDto {

    private final Long id;
    private final String imageUrl;

    public static ReviewImageInfoDto from(ReviewImage reviewImage) {
        return ReviewImageInfoDto.builder()
                .id(reviewImage.getId())
                .imageUrl(reviewImage.getImageUrl())
                .build();
    }
}
