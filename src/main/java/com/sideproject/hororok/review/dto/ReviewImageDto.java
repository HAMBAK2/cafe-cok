package com.sideproject.hororok.review.dto;

import com.sideproject.hororok.image.domain.Image;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReviewImageDto {

    private final Long id;
    private final String imageUrl;

    public static ReviewImageDto from(Image image) {
        return ReviewImageDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }

    public static List<ReviewImageDto> fromList(List<Image> images) {
        return images.stream()
                .map(image -> ReviewImageDto.from(image))
                .collect(Collectors.toList());
    }
}
