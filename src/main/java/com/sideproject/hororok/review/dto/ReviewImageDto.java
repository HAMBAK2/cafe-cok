package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.review.domain.ReviewImage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReviewImageDto {

    private final Long id;
    private final String imageUrl;

    public static ReviewImageDto from(ReviewImage reviewImage) {
        return ReviewImageDto.builder()
                .id(reviewImage.getId())
                .imageUrl(reviewImage.getImageUrl())
                .build();
    }

    public static List<ReviewImageDto> fromList(List<ReviewImage> reviewImages) {
        return reviewImages.stream()
                .map(reviewImage -> ReviewImageDto.from(reviewImage))
                .collect(Collectors.toList());
    }
}
