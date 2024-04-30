package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.bookmark.domain.BookmarkFolder;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.CafeImage;
import com.sideproject.hororok.cafe.domain.enums.OpenStatus;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.cafe.dto.response.CafeDetailBasicInfoResponse;
import com.sideproject.hororok.cafe.dto.response.CafeDetailMenuResponse;
import com.sideproject.hororok.cafe.dto.response.CafeDetailTopResponse;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_DTO_리스트;
import static com.sideproject.hororok.common.fixtures.KeywordFixtures.키워드_카운트_DTO;
import static com.sideproject.hororok.common.fixtures.MenuFixtures.메뉴_DTO_리스트;
import static com.sideproject.hororok.common.fixtures.ReviewFixtures.*;

public class CafeFixtures {

    public static final Long 카페_아이디 = 1L;
    public static final String 카페_이름 = "카페 이름";
    public static final String 카페_전화번호 = "010-1234-5678";
    public static final String 카페_도로명_주소 = "OO시 OO구 OO동";
    public static final BigDecimal 카페_위도 = getRandomBigDecimal(0, 90);
    public static final BigDecimal 카페_경도 = getRandomBigDecimal(0, 180);

    public static final OpenStatus 영업_여부 = OpenStatus.OPEN;

    public static List<String> 영업_시간_리스트 = Arrays.asList("영업 시간");
    public static List<String> 휴무일_리스트 = Arrays.asList("휴무일");

    public static final String 카페_이미지_URL = "//카페이미지";


    public static CafeDetailMenuResponse 카페_상세_메뉴_응답() {
        return CafeDetailMenuResponse.from(메뉴_DTO_리스트());
    }

    public static CafeDetailBasicInfoResponse 카페_상세_기본_정보_응답() {
        return CafeDetailBasicInfoResponse
                .of(카페(), 영업_여부, 영업_시간_리스트, 휴무일_리스트, 메뉴_DTO_리스트(), Arrays.asList(카페_이미지_URL, 리뷰_이미지_URL),
                        Arrays.asList(키워드_카운트_DTO()), Arrays.asList(카페_상세_리뷰_DTO()));
    }

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
        return CafeDetailTopResponse.of(카페(), 카페_이미지_URL, 리뷰_개수, 키워드_DTO_리스트());
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
