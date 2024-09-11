package com.sideproject.cafe_cok.keword.dto;

import com.sideproject.cafe_cok.cafe.exception.NoSuchCategoryException;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@Schema(description = "카테고리 별 키워드 DTO")
public class CategoryKeywordsDto {

    @Schema(description = "목적 키워드 리스트", example = "데이트/모임")
    private List<String> purpose = new ArrayList<>();

    @Schema(description = "메뉴 키워드 리스트", example = "아메리카노")
    private List<String> menu = new ArrayList<>();

    @Schema(description = "테마 키워드 리스트", example = "뷰맛집")
    private List<String> theme = new ArrayList<>();

    @Schema(description = "시설 키워드 리스트", example = "실내화장실")
    private List<String> facility = new ArrayList<>();

    @Schema(description = "분위기 키워드 리스트", example = "조용한")
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
