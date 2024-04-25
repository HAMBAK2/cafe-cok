package com.sideproject.hororok.keword.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.sideproject.hororok.cafe.exception.NoSuchCategoryException;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CategoryKeywordsDto {

    private List<String> purpose = new ArrayList<>();
    private List<String> menu = new ArrayList<>();
    private List<String> theme = new ArrayList<>();
    private List<String> facility = new ArrayList<>();
    private List<String> atmosphere = new ArrayList<>();

    protected CategoryKeywordsDto() {
    }

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
