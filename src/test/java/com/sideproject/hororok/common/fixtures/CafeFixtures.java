package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.cafe.domain.Cafe;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Random;

public class CafeFixtures {

    public static final Long 카페_아이디 = 1L;
    public static final String 카페_이름 = "카페 이름";
    public static final String 카페_전화번호 = "000-0000-0000";
    public static final String 카페_도로명_주소 = "OO시 OO구 OO동";
    public static final BigDecimal 카페_위도 = getRandomBigDecimal(-90, 90);
    public static final BigDecimal 카페_경도 = getRandomBigDecimal(-180, 180);

    public static Cafe 카페() {

        Cafe cafe = new Cafe(카페_이름, 카페_전화번호, 카페_도로명_주소, 카페_위도, 카페_경도);
        setId(cafe, 카페_아이디);

        return cafe;
    }


    private static BigDecimal getRandomBigDecimal(int min, int max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return BigDecimal.valueOf(shifted);
    }

    public static Cafe setId(Cafe cafe, final Long id) {

        try {
            Field idField = Cafe.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(cafe, id);
            return cafe;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
