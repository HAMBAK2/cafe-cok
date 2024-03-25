package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Builder
@Getter
@RequiredArgsConstructor
public class CafeReSearchDto {

    private final boolean isExist;
    private final List<Cafe> cafe;

    public static CafeReSearchDto of(boolean isExist, List<Cafe> cafe) {
        return CafeReSearchDto.builder()
                .isExist(isExist)
                .cafe(cafe)
                .build();
    }

}
