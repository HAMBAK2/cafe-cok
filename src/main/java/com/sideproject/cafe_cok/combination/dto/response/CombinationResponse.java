package com.sideproject.cafe_cok.combination.dto.response;


import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "조합 조회 응답")
public class CombinationResponse extends RepresentationModel<CombinationResponse> {

    @Schema(description = "조합 ID", example = "1")
    private Long id;

    @Schema(description = "조합 이름", example = "조합 이름")
    private String name;

    @Schema(description = "조합 아이콘", example = "조합 아이콘")
    private String icon;

    @Schema(description = "조합의 카테고리 별 키워드 정보")
    private CategoryKeywordsDto categoryKeywords;

    public CombinationResponse(final Combination combination,
                               final CategoryKeywordsDto categoryKeywords) {

        this.id = combination.getId();
        this.name = combination.getName();
        this.icon = combination.getIcon();
        this.categoryKeywords = categoryKeywords;

    }
}
