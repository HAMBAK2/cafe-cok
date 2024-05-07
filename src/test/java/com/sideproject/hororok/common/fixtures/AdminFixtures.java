package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.hororok.admin.dto.response.AdminCafeSaveResponse;

import static com.sideproject.hororok.common.fixtures.CafeCopyFixtures.*;
import static com.sideproject.hororok.common.fixtures.CafeFixtures.*;

public class AdminFixtures {



    public static AdminCafeSaveRequest 카페_저장_요청() {
        return new AdminCafeSaveRequest(카페_이름, 카페_도로명_주소, X_좌표, Y_좌표, 카페_전화번호);
    }


    public static AdminCafeSaveResponse 카페_저장_응답() {
        return AdminCafeSaveResponse.of(카페_카피());
    }

}
