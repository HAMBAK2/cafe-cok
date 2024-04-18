package com.sideproject.hororok.favorite.dto;

import com.sideproject.hororok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkDto {

    private Long cafeId;
    private String cafeName;
    private String roadAddress;


    public static BookmarkDto from(Cafe cafe) {
        return BookmarkDto.builder()
                .cafeId(cafe.getId())
                .cafeName(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .build();
    }
}
