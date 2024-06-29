package com.sideproject.cafe_cok.admin.dto.response;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.dto.CafeMainImageDto;
import com.sideproject.cafe_cok.image.dto.CafeOtherImageDto;
import com.sideproject.cafe_cok.menu.dto.CafeSaveMenuDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class AdminCafeSaveResponse {

    private Long id;
    private String name;
    private String roadAddress;
    private BigDecimal mapx;
    private BigDecimal mapy;
    private String telephone;
    private CafeMainImageDto mainImage;
    private List<CafeOtherImageDto> otherImages;
    private List<CafeSaveMenuDto> menus;
    private List<List<String>> hours;

    public static AdminCafeSaveResponse of(final Cafe cafe,
                                           final CafeMainImageDto mainImage,
                                           final List<CafeOtherImageDto> otherImages,
                                           final List<CafeSaveMenuDto> menus,
                                           final List<List<String>> hours) {
        return AdminCafeSaveResponse.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .roadAddress(cafe.getRoadAddress())
                .mapx(cafe.getLongitude())
                .mapy(cafe.getLatitude())
                .telephone(cafe.getPhoneNumber())
                .mainImage(mainImage)
                .otherImages(otherImages)
                .menus(menus)
                .hours(hours)
                .build();
    }
}
