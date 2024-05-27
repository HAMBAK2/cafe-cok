package com.sideproject.cafe_cok.keword.dto;

import com.sideproject.cafe_cok.cafe.exception.NoSuchCategoryException;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryKeywordsDto {

    private List<String> purpose = new ArrayList<>();
    private List<String> menu = new ArrayList<>();
    private List<String> theme = new ArrayList<>();
    private List<String> facility = new ArrayList<>();
    private List<String> atmosphere = new ArrayList<>();

    public CategoryKeywordsDto(final List<Keyword> keywords) {

        for (Keyword keyword : keywords) {
            switch (keyword.getCategory()){
                case PURPOSE -> purpose.add(keyword.getName());
                case MENU -> menu.add(keyword.getName());
                case THEME -> theme.add(keyword.getName());
                case FACILITY -> facility.add(keyword.getName());
                case ATMOSPHERE -> atmosphere.add(keyword.getName());
                default -> throw new NoSuchCategoryException();
            }
        }
    }
}
