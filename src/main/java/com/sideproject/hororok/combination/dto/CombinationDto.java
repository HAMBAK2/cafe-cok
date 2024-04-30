package com.sideproject.hororok.combination.dto;


import com.sideproject.hororok.combination.domain.Combination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CombinationDto {

    private Long id;
    private String name;
    private String icon;


    public static CombinationDto from(final Combination combination) {
        return CombinationDto.builder()
                .id(combination.getId())
                .name(combination.getName())
                .icon(combination.getIcon())
                .build();
    }

    public static List<CombinationDto> fromList(final List<Combination> combinations) {
        return combinations.stream()
                .map(combination -> CombinationDto.from(combination))
                .collect(Collectors.toList());
    }

}
