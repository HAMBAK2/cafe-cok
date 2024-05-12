package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.dto.request.AdminMenuSaveRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;

import java.util.Arrays;

import static com.sideproject.cafe_cok.common.fixtures.CafeCopyFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.*;

public class AdminFixtures {


    private static final String 메뉴_이름 = "메뉴 이름";
    private static final Integer 메뉴_가격 = 10000;
    private static final String 메뉴_이미지 = "Base64로 인코딩된 이미지 정보";

    public static AdminCafeSaveRequest 카페_저장_요청() {
        return new AdminCafeSaveRequest(카페_이름, 카페_도로명_주소, X_좌표, Y_좌표, 카페_전화번호, Arrays.asList(메뉴_저장_요청()));
    }


    public static AdminCafeSaveResponse 카페_저장_응답() {
        return AdminCafeSaveResponse.of(카페_카피());
    }

    public static AdminMenuSaveRequest 메뉴_저장_요청() {
        return new AdminMenuSaveRequest(메뉴_이름, 메뉴_가격, 메뉴_이미지);
    }

}
