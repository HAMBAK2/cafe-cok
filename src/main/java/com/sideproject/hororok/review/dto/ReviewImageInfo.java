package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class ReviewImageInfo {

    private final Long id;
    private final String imageUrl;

    public static ReviewImageInfo from(ReviewImage reviewImage) {
        return ReviewImageInfo.builder()
                .id(reviewImage.getId())
                .imageUrl(reviewImage.getImageUrl())
                .build();
    }
}
