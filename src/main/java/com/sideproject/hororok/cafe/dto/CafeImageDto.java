package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.domain.CafeImage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CafeImageDto {

    private Long id;
    private String imageUrl;


    public static CafeImageDto from(final CafeImage cafeImage) {
        return CafeImageDto.builder()
                .id(cafeImage.getId())
                .imageUrl(cafeImage.getImageUrl())
                .build();
    }

    public static List<CafeImageDto> fromList(final List<CafeImage> cafeImages) {
        return cafeImages.stream()
                .map(cafeImage -> CafeImageDto.from(cafeImage))
                .collect(Collectors.toList());
    }

}
