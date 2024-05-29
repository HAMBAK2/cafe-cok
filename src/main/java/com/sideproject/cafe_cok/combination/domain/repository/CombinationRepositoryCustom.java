package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.dto.CombinationDto;

import java.util.List;

public interface CombinationRepositoryCustom {

    List<CombinationDto> findDtoByMemberId(final Long memberId);
}
