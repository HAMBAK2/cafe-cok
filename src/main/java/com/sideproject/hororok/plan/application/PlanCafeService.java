package com.sideproject.hororok.plan.application;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeImageRepository;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.PlanCafe;
import com.sideproject.hororok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.hororok.plan.domain.repository.PlanCafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanCafeService {


    private final CafeRepository cafeRepository;
    private final PlanCafeRepository planCafeRepository;
    private final CafeImageRepository cafeImageRepository;


    public void saveAll(Plan plan, List<CafeDto> cafes, PlanCafeMatchType matchType) {

        List<Long> cafeIds = cafes.stream()
                .map(cafeDto -> cafeDto.getId())
                .collect(Collectors.toList());
        List<Cafe> findCafes = cafeRepository.findByIdIn(cafeIds);
        List<PlanCafe> planCafes = findCafes.stream()
                .map(findCafe -> new PlanCafe(plan, findCafe, matchType))
                .collect(Collectors.toList());
        planCafeRepository.saveAll(planCafes);
    }

    public List<CafeDto> getCafeDtosByPlanIdAndMatchType(Long planId, PlanCafeMatchType matchType) {
        List<PlanCafe> findPlanCafes = planCafeRepository.findByPlanIdAndMatchType(planId, matchType);
        return findPlanCafes.stream()
                .map(planCafe -> CafeDto.of(
                        planCafe.getCafe(),
                        cafeImageRepository.findByCafeId(planCafe.getCafe().getId()).get(0).getImageUrl()))
                .collect(Collectors.toList());
    }
}
