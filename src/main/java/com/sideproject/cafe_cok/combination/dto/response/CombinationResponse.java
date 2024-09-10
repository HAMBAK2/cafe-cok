package com.sideproject.cafe_cok.combination.dto.response;


import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class CombinationResponse extends RepresentationModel<CombinationResponse> {

    private Long id;
    private String name;
    private String icon;
    private CategoryKeywordsDto categoryKeywords;

    public CombinationResponse(final Combination combination,
                               final CategoryKeywordsDto categoryKeywords) {

        this.id = combination.getId();
        this.name = combination.getName();
        this.icon = combination.getIcon();
        this.categoryKeywords = categoryKeywords;

    }
}
