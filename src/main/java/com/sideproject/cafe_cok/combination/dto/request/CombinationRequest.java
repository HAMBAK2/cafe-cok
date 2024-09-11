package com.sideproject.cafe_cok.combination.dto.request;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "조합 저장/수정 요청")
public class CombinationRequest {

    @Schema(description = "조합 이름", example = "조합 이름")
    private String name;

    @Schema(description = "조합 아이콘", example = "조합 아이콘")
    private String icon;

    @Schema(description = "조합 키워드 리스트", example = "데이트/모임")
    private List<String> keywords;

    public CombinationRequest(final String name,
                              final String icon,
                              final List<String> keywords) {
        this.name = name;
        this.icon = icon;
        this.keywords = keywords;
    }

    public Combination toEntity(final Member member) {
        return Combination.builder()
                .name(this.name)
                .icon(this.icon)
                .member(member)
                .build();
    }
}
