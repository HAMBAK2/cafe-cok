package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.combination.dto.CombinationDto;

import java.lang.reflect.Field;

import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;

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
        return CombinationDto.from(조합());
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
