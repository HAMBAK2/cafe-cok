package com.sideproject.hororok.plan.application;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.domain.repository.CafeRepository;
import com.sideproject.hororok.cafe.dto.CafeDto;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.PlanCafe;
import com.sideproject.hororok.plan.domain.repository.PlanCafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanCafeService {

    private final PlanCafeRepository planCafeRepository;
    private final CafeRepository cafeRepository;


    public void saveAll(Plan plan, List<CafeDto> cafes) {

        List<Long> cafeIds = cafes.stream()
                .map(cafeDto -> cafeDto.getId())
                .collect(Collectors.toList());
        List<Cafe> findCafes = cafeRepository.findByIdIn(cafeIds);
        List<PlanCafe> planCafes = findCafes.stream()
                .map(findCafe -> new PlanCafe(plan, findCafe))
                .collect(Collectors.toList());
        planCafeRepository.saveAll(planCafes);
    }




}
