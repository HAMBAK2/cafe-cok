package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.menu.dto.MenuDetailDto;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.dto.ImageDto;
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

    public CafeAdminDto(final Cafe cafe) {
        this.id = cafe.getId();
        this.name = cafe.getName();
        this.address = cafe.getRoadAddress();
        this.phoneNumber = cafe.getPhoneNumber();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
    }

    public CafeAdminDto(final Cafe cafe,
                        final List<ImageDto> images,
                        final List<MenuDetailDto> menus,
                        final List<CafeOperationHourDto> hours) {
        this(cafe);
        this.images = images;
        this.menus = menus;
        this.hours = hours;
    }
}
