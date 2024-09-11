package com.sideproject.cafe_cok.combination.dto.response;


import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "조합 목록 조회 응답")
public class CombinationListResponse extends RepresentationModel<CombinationListResponse> {

    @Schema(description = "조합 정보의 리스트")
    private List<CombinationDto> combinations = new ArrayList<>();

    public CombinationListResponse(final List<CombinationDto> combinations) {
        this.combinations = combinations;
    }
}
