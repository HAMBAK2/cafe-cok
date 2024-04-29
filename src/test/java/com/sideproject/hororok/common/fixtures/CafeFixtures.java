package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeImage;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.cafe.dto.response.CafeDetailTopResponse;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_DTO_리스트;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.리뷰_개수;

public class CafeFixtures {

    public static final Long 카페_아이디 = 1L;
    public static final String 카페_이름 = "카페 이름";
    public static final String 카페_전화번호 = "010-1234-5678";
    public static final String 카페_도로명_주소 = "OO시 OO구 OO동";
    public static final BigDecimal 카페_위도 = getRandomBigDecimal(0, 90);
    public static final BigDecimal 카페_경도 = getRandomBigDecimal(0, 180);

    public static final String 카페_이미지_URL = "//카페이미지";

    public static Cafe 카페() {
        Cafe cafe = new Cafe(카페_이름, 카페_전화번호, 카페_도로명_주소, 카페_위도, 카페_경도);
        setId(cafe, 카페_아이디);

        return cafe;
    }

    public static CafeDto 카페_DTO() {
        return CafeDto.of(카페(), 카페_이미지_URL);
    }

    public static List<CafeDto> 카페_DTO_리스트() {
        return Arrays.asList(카페_DTO());
    }

    public static CafeDetailTopResponse 카페_상세_상단_응답() {
        return CafeDetailTopResponse.of(카페(), 리뷰_개수, 키워드_DTO_리스트());
    }

    public static BigDecimal getRandomBigDecimal(int min, int max) {
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
