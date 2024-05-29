package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.combination.dto.response.CombinationIdResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationDetailResponse;

import java.lang.reflect.Field;

import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.카테고리_키워드_DTO;
import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.키워드_이름_리스트;
import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;

public class CombinationFixtures {

    public static final Long 조합_ID = 1L;
    public static final String 조합_이름 = "조합 이름";

    public static final String 조합_아이콘 = "Icon_unicode1";

    public static Combination 조합() {
        Combination combination = new Combination(조합_이름, 조합_아이콘, 사용자());
        setCombinationId(combination, 조합_ID);
        return combination;
    }

    public static CombinationDto 조합_DTO() {
        return new CombinationDto(조합_ID, 조합_이름, 조합_아이콘);
    }

    public static CombinationRequest 조합_생성_수정_요청() {
        return new CombinationRequest(조합_이름, 조합_아이콘, 키워드_이름_리스트);
    }

    public static CombinationDetailResponse 조합_조회_응답() {
        return CombinationDetailResponse.of(조합(), 카테고리_키워드_DTO());
    }

    public static CombinationIdResponse 조합_생성_수정_응답() {
        return CombinationIdResponse.of(조합_ID);
    }

    public static Combination setCombinationId(Combination combination, final Long id) {

        try {
            Field idField = Combination.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(combination, id);
            return combination;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
