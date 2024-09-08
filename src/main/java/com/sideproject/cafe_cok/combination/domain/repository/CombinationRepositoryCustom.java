package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;

import java.util.List;

public interface CombinationRepositoryCustom {

    void update(final CombinationRequest request);

    List<CombinationDto> findByMemberId(final Long memberId);
}
