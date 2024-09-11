package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 조회 응답")
public class CafesResponse extends RepresentationModel<CafesResponse> {

    @Schema(description = "카페 정보의 리스트")
    private List<CafeDto> cafes;

    public CafesResponse(final List<CafeDto> cafes) {
        this.cafes = cafes;
    }
}
