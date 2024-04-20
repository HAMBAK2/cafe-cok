package com.sideproject.hororok.bookmark.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class BookmarkDto {

    private Long cafeId;
    private String cafeName;
    private String roadAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;


    public static BookmarkDto from(Cafe cafe) {
        return BookmarkDto.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .latitude(cafe.getLatitude())
                .longitude(cafe.getLongitude())
                .build();
    }
}
