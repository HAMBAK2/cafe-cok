package com.sideproject.cafe_cok.admin.dto.response;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminCafeSaveResponse {

    private Long id;
    private String name;
    private String roadAddress;
    private Integer mapx;
    private Integer mapy;
    private String telephone;
    private String mainImage;

    public static AdminCafeSaveResponse of(final Cafe cafe) {
        return AdminCafeSaveResponse.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .mapx(cafe.getMapx())
                .mapy(cafe.getMapy())
                .telephone(cafe.getPhoneNumber())
                .mainImage(cafe.getMainImage())
                .build();
    }
}
