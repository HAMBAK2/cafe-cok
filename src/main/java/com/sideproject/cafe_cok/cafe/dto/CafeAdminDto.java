package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.menu.dto.MenuDetailDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CafeAdminDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<ImageDto> images;
    private List<MenuDetailDto> menus;
    private List<CafeOperationHourDto> hours = new ArrayList<>();

    @Builder
    public CafeAdminDto(final Long id,
                        final String name,
                        final String address,
                        final String phoneNumber,
                        final BigDecimal latitude,
                        final BigDecimal longitude,
                        final List<ImageDto> images,
                        final List<MenuDetailDto> menus,
                        final List<CafeOperationHourDto> hours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = images;
        this.menus = menus;
        this.hours = hours;
    }
}
