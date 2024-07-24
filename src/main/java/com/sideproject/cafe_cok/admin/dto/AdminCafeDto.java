package com.sideproject.cafe_cok.admin.dto;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.image.dto.ImageDto;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AdminCafeDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<ImageDto> images;
    private List<AdminMenuDto> menus;
    private List<AdminOperationHourDto> hours = new ArrayList<>();

    public AdminCafeDto(final Cafe cafe) {
        this.id = cafe.getId();
        this.name = cafe.getName();
        this.address = cafe.getRoadAddress();
        this.phoneNumber = cafe.getPhoneNumber();
        this.latitude = cafe.getLatitude();
        this.longitude = cafe.getLongitude();
    }

    public AdminCafeDto(final Cafe cafe,
                        final List<ImageDto> images,
                        final List<AdminMenuDto> menus,
                        final List<AdminOperationHourDto> hours) {
        this(cafe);
        this.images = images;
        this.menus = menus;
        this.hours = hours;
    }
}
