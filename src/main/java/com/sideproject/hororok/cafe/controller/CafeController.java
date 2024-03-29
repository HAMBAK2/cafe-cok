package com.sideproject.hororok.cafe.controller;

import com.sideproject.hororok.cafe.cond.CafeCategorySearchCond;
import com.sideproject.hororok.cafe.cond.CafeSearchCond;
import com.sideproject.hororok.cafe.cond.CreatePlanSearchCond;
import com.sideproject.hororok.cafe.dto.*;
import com.sideproject.hororok.cafe.service.CafePlanService;
import com.sideproject.hororok.cafe.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;
    private final CafePlanService cafePlanService;

    @GetMapping("/detail/{cafeId}")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    @Parameter(description = "카페의 ID")
    public ResponseEntity<CafeDetailDto> detail(@PathVariable Long cafeId){

        return ResponseEntity.ok(cafeService.findCafeDetail(cafeId));
    }


    @GetMapping("/search/re")
    @Operation(summary = "특정 지점에서 카페를 재검색 하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    public ResponseEntity<CafeReSearchDto> searchRe(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        return ResponseEntity.ok(cafeService.findWithinRadius(CafeSearchCond.of(latitude, longitude)));
    }

    @GetMapping("/search/bar")
    @Operation(summary = "검색창에 검색을 했을 때 동작하는 기능")
    @Parameter(description = "현재 위치의 경도 위도 값")
    @ApiResponse(description = "선택한 카페의 상세 정보를 전달, 카페가 존재하지 않는 경우 cafes, keywordsByCategory 정보 존재 나머지 X, \n카페가 존재하는 경우 반대 (exist 값은 항상 존재)")
    public ResponseEntity<CafeBarSearchDto> searchBar(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {
        return ResponseEntity.ok(cafeService.barSearch(CafeSearchCond.of(latitude, longitude)));
    }

    @GetMapping("/search/category")
    @Operation(summary = "선택한 키워드와 현재 위치를 기준으로 검색")
    @Parameter(description = "현재 위치의 경도 위도 값, 선태한 키워드")
    public ResponseEntity<CafeCategorySearchDto> searchCategory(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam List<String> keywords) {

        return ResponseEntity.ok(cafeService.categorySearch(CafeCategorySearchCond.of(latitude, longitude, keywords)));
    }

    @GetMapping("/plans")
    @Operation(summary = "계획하기를 통해 선택한 항목에 대한 결과를 제공")
    @Parameter(description = "방문위치(좌표), 몇분거리, 방문일자(요일 하나, 시간은 범위), 키워드 5개")
    public ResponseEntity<CreatePlanDto> createPlan(
            @RequestParam BigDecimal latitude, @RequestParam BigDecimal longitude,
            @RequestParam Integer minutes, @RequestParam String date,
            @RequestParam LocalTime startTime, @RequestParam LocalTime endTime, @RequestParam List<String> keywords
            ) {

        return ResponseEntity.ok(cafePlanService.createPlans(CreatePlanSearchCond.of(latitude, longitude, minutes,
                date, startTime, endTime, keywords)));
    }
}
