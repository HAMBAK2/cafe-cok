package com.sideproject.cafe_cok.cafe.dto.response;

import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
public class CafesResponse extends RepresentationModel<CafesResponse> {

    private List<CafeDto> cafes;

    public CafesResponse(final List<CafeDto> cafes) {
        this.cafes = cafes;
    }
}
