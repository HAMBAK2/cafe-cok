package com.sideproject.cafe_cok.admin.dto.request;

import com.sideproject.cafe_cok.admin.dto.AdminImageDto;
import com.sideproject.cafe_cok.admin.dto.AdminOperationHourDto;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminCafeUpdateRequest {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private AdminImageDto image;
    private List<AdminImageDto> otherImages;
    private List<AdminMenuRequestDto> menus;
    private List<AdminOperationHourDto> hours;
}
