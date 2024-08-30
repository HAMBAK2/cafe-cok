package com.sideproject.cafe_cok.admin.dto.request;


import com.sideproject.cafe_cok.cafe.dto.OperationHourDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminCafeSaveRequest {

    private String name;
    private String address;
    private String phone;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long kakaoId;
    private String mainImage;
    private List<String> otherImages;
    private List<AdminMenuRequestDto> menus;
    private List<OperationHourDto> hours;
}
