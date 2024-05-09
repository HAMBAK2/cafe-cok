package com.sideproject.cafe_cok.admin.dto.response;

import com.sideproject.cafe_cok.admin.domain.CafeCopy;
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

    public static AdminCafeSaveResponse of(final CafeCopy cafeCopy) {
        return AdminCafeSaveResponse.builder()
                .id(cafeCopy.getId())
                .name(cafeCopy.getName())
                .roadAddress(cafeCopy.getRoadAddress())
                .mapx(cafeCopy.getMapx())
                .mapy(cafeCopy.getMapy())
                .telephone(cafeCopy.getPhoneNumber())
                .mainImage(cafeCopy.getMainImage())
                .build();
    }
}
