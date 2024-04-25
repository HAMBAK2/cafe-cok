package com.sideproject.hororok.common.fixtures;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;

import java.util.Arrays;
import java.util.List;

public class KeywordFixtures {

    public static final String 키워드_이름 = "키워드 이름";
    public static final List<String> 키워드_이름_리스트 = Arrays.asList(키워드_이름);
    public static final List<Keyword> 키워드_리스트 = Arrays.asList(키워드());

    public static CategoryKeywordsDto 카테고리_키워드_DTO() {
        return new CategoryKeywordsDto(키워드_리스트);
    }

    public static Keyword 키워드() {
        return new Keyword(키워드_이름, Category.PURPOSE);
    }

}
