package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.image.domain.Image;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.dto.CafeMainImageDto;
import com.sideproject.cafe_cok.image.dto.CafeOtherImageDto;
import com.sideproject.cafe_cok.menu.dto.CafeSaveMenuDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.ImageFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.MenuFixtures.메뉴;

public class AdminFixtures {


    private static final String 메뉴_이름 = "메뉴 이름";
    private static final Integer 메뉴_가격 = 10000;
    private static final String 메뉴_이미지 = "Base64로 인코딩된 이미지 정보";
    public static final BigDecimal X_좌표 = new BigDecimal(123456789);
    public static final BigDecimal Y_좌표 = new BigDecimal(123456789);

    public static AdminCafeSaveRequest 카페_저장_요청() {
        return new AdminCafeSaveRequest(카페_이름, 카페_도로명_주소, X_좌표, Y_좌표, 카페_전화번호,
                Arrays.asList(메뉴_저장_요청()), 카페_운영_시간());
    }

    public static List<List<String>> 카페_운영_시간() {
        List<List<String>> hours = new ArrayList<>();
        hours.add(new ArrayList<>());
        return hours;
    }

    public static AdminCafeSaveResponse 카페_저장_응답() {
        return AdminCafeSaveResponse.of(
                카페(), 카페_메인_이미지_DTO(), 카페_나머지_이미지_DTO_리스트(), 카페_메뉴_응답_리스트(), 카페_운영_시간());
    }

    public static CafeMainImageDto 카페_메인_이미지_DTO() {
        return CafeMainImageDto.from(카페_메인_이미지(ImageType.CAFE_MAIN, 카페()));
    }

    public static List<CafeOtherImageDto> 카페_나머지_이미지_DTO_리스트() {
        Image 카페_이미지 = 카페_이미지(ImageType.CAFE, 카페());
        return Arrays.asList(CafeOtherImageDto.from(카페_이미지));
    }

    public static List<CafeSaveMenuDto> 카페_메뉴_응답_리스트() {
        return Arrays.asList(CafeSaveMenuDto.of(메뉴(), 메뉴_이미지(ImageType.MENU, 카페(), 메뉴())));

    }

    public static AdminMenuSaveRequest 메뉴_저장_요청() {
        return new AdminMenuSaveRequest(메뉴_이름, 메뉴_가격, 메뉴_이미지);
    }

}
