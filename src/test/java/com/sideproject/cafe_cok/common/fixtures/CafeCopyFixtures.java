package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.admin.domain.CafeCopy;

import java.lang.reflect.Field;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.*;

public class CafeCopyFixtures {



    public static final Integer X_좌표 = 123456789;
    public static final Integer Y_좌표 = 123456789;


    public static CafeCopy 카페_카피() {

        CafeCopy cafeCopy = new CafeCopy(카페_이름, 카페_전화번호, 카페_도로명_주소, X_좌표, Y_좌표, 카페_이미지_URL);
        setCafeCopyId(cafeCopy, 카페_아이디);
        return cafeCopy;
    }


    public static CafeCopy setCafeCopyId(CafeCopy cafeCopy, final Long id) {

        try {
            Field idField = CafeCopy.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(cafeCopy, id);
            return cafeCopy;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


}
